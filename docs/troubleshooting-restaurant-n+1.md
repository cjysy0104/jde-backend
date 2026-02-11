# 트러블 슈팅: 레스토랑 목록 조회 시 N+1 쿼리 발생

## 상황
레스토랑 목록 API를 만들 때, 각 레스토랑마다 "대표 썸네일(좋아요가 가장 많은 리뷰의 이미지)"을 보여줘야 했습니다. 처음에는 레스토랑 목록을 먼저 조회한 뒤, 각 레스토랑마다 썸네일을 따로 조회하는 방식으로 구현했는데, 레스토랑이 20개면 쿼리가 1 + 20번 나가서 N+1 문제가 생겼습니다.

## 원인
목록 1번 + 레스토랑 하나마다 썸네일 1번씩 조회하다 보니, 데이터가 많아질수록 DB 요청 수가 크게 늘어났습니다.

### 원인 코드

**Service – 목록 조회 후 반복문으로 썸네일 조회 (N+1 발생)**

```java
@Override
@Transactional(readOnly = true)
public List<RestaurantListDTO> getRestaurantList(RestaurantQueryDTO request) {
    RestaurantQueryDTO normalized = normalizeRequest(request);
    
    // 1번 쿼리: 레스토랑 목록만 조회
    List<RestaurantListDTO> restaurants = restaurantMapper.selectRestaurantList(normalized);
    
    // N번 쿼리: 각 레스토랑마다 썸네일 따로 조회 → N+1 발생
    for (RestaurantListDTO restaurant : restaurants) {
        String thumbnailUrl = restaurantMapper.selectThumbnailByRestaurantNo(restaurant.getRestaurantNo());
        restaurant.setThumbnailUrl(thumbnailUrl);
    }
    
    return restaurants;
}
```

**Mapper XML – 목록은 썸네일 없이 조회**

```xml
<select id="selectRestaurantList" resultType="com.kh.jde.restaurant.model.dto.RestaurantListDTO">
    SELECT
           RT.RESTAURANT_NO restaurantNo
         , RT.NORMAL_NAME normalName
         , RT.ADDRESS address
         , RT.LATITUDE latitude
         , RT.LONGITUDE longitude
      FROM TB_RESTAURANT RT
     ORDER BY RT.RESTAURANT_NO DESC
     FETCH FIRST #{scroll.sizePlusOne} ROWS ONLY
</select>

<!-- 레스토랑별로 1번씩 호출됨 → N번 쿼리 -->
<select id="selectThumbnailByRestaurantNo" resultType="string">
    SELECT FILE_URL
      FROM (
          SELECT RVF.FILE_URL, NVL(LC.LIKE_COUNT, 0) LIKE_COUNT
            FROM TB_REVIEW RV
            JOIN TB_REVIEW_FILE RVF ON RV.REVIEW_NO = RVF.REVIEW_NO AND RVF.IS_THUMNAIL = 'Y'
            LEFT JOIN (SELECT REVIEW_NO, COUNT(*) LIKE_COUNT FROM TB_REVIEW_LIKE GROUP BY REVIEW_NO) LC
              ON LC.REVIEW_NO = RV.REVIEW_NO
           WHERE RV.STATUS = 'Y' AND RV.RESTAURANT_NO = #{restaurantNo}
           ORDER BY NVL(LC.LIKE_COUNT, 0) DESC, RV.REVIEW_NO DESC
      )
     WHERE ROWNUM = 1
</select>
```

→ 목록 20건 조회 시 **총 21번** 쿼리 실행 (1 + 20)

---

## 해결
MyBatis 매퍼에서 레스토랑 한 행을 조회할 때, **스칼라 서브쿼리**로 해당 레스토랑의 대표 썸네일 URL을 한 번에 가져오도록 수정했습니다. 목록 조회 1번으로 레스토랑 정보와 썸네일을 같이 가져오게 했습니다.

### 해결 코드

**Service – 반복문 제거, Mapper 한 번만 호출**

```java
@Override
@Transactional(readOnly = true)
public List<RestaurantListDTO> getRestaurantList(RestaurantQueryDTO request) {
    RestaurantQueryDTO normalized = normalizeRequest(request);
    return restaurantMapper.selectRestaurantList(normalized);
}
```

**Mapper XML – SELECT 절에 스칼라 서브쿼리로 썸네일 한 번에 조회**

```xml
<select id="selectRestaurantList" parameterType="com.kh.jde.restaurant.model.dto.RestaurantQueryDTO" resultType="com.kh.jde.restaurant.model.dto.RestaurantListDTO">
    SELECT
           RT.RESTAURANT_NO restaurantNo
         , RT.NORMAL_NAME normalName
         , RT.ADDRESS address
         , RT.LATITUDE latitude
         , RT.LONGITUDE longitude
         , (
              SELECT FILE_URL
                FROM (
                    SELECT
                           RVF.FILE_URL
                         , NVL(LC.LIKE_COUNT, 0) LIKE_COUNT
                      FROM TB_REVIEW RV
                      JOIN TB_REVIEW_FILE RVF
                        ON RV.REVIEW_NO = RVF.REVIEW_NO AND RVF.IS_THUMNAIL = 'Y'
                      LEFT JOIN (
                          SELECT REVIEW_NO, COUNT(*) LIKE_COUNT
                            FROM TB_REVIEW_LIKE
                           GROUP BY REVIEW_NO
                      ) LC ON LC.REVIEW_NO = RV.REVIEW_NO
                     WHERE RV.STATUS = 'Y' AND RV.RESTAURANT_NO = RT.RESTAURANT_NO
                     ORDER BY NVL(LC.LIKE_COUNT, 0) DESC, RV.REVIEW_NO DESC
                )
               WHERE ROWNUM = 1
          ) thumbnailUrl
      FROM TB_RESTAURANT RT
     <where>
         <if test="cursor != null">
             RT.RESTAURANT_NO &lt; #{cursor}
         </if>
     </where>
     ORDER BY RT.RESTAURANT_NO DESC
     FETCH FIRST #{scroll.sizePlusOne} ROWS ONLY
</select>
```

→ 목록 20건 조회 시 **1번** 쿼리만 실행. `selectThumbnailByRestaurantNo`는 제거했습니다.

---

## 결과
- 이전: 목록 20개 조회 시 쿼리 **21번** (1 + 20)
- 수정 후: 쿼리 **1번**으로 동일 결과
- DB 부하 감소, 응답 속도 체감 개선
- 설계 단계에서 N+1 가능성을 미리 점검하는 습관을 갖게 됨
