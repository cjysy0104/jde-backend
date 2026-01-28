package com.kh.jde.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.kh.jde.configuration.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity // 최신문법 사용하기 위해 
@RequiredArgsConstructor
public class SecurityConfigure {
	
	private final JwtFilter jwtFilter;
	
	@Value("${app.server.url}")
	private String serverUrl;

	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity.formLogin(AbstractHttpConfigurer::disable)
						   .csrf(AbstractHttpConfigurer::disable)
						   .cors(Customizer.withDefaults())
						   .authorizeHttpRequests(requests -> {
							   requests.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
							   requests.requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/members", "/api/auth/refresh").permitAll(); // 누구나 허용할 기능 
							   requests.requestMatchers(HttpMethod.GET, "/api/reviews/**", "/api/comments/**", "/api/uploads/**", "/api/members", "/api/members/email", "/api/members/nickname", "/api/restaurants/**").permitAll(); // 게시글 전체조회 및 상세조회는 아무나
							   
							   requests.requestMatchers(HttpMethod.PUT, "/api/members", "/api/reviews/**", "/api/comments/**").authenticated(); // 테이블의 행 전체 수정, 부분 수정(전체수정일지 부분 수정일지 알 수 없을 때), 인증 필요한 기능
							   requests.requestMatchers(HttpMethod.PATCH, "/api/members/**", "/api/comments/**", "/api/reviews/**").authenticated(); // 테이블의 행 중 부분만 수정

							   requests.requestMatchers(HttpMethod.DELETE, "/api/bookmarks/**", "/api/members/**", "/api/reviews/**", "/api/comments/**", "/api/reviewLikes/**", "/api/commentLikes/**").authenticated(); // 삭제, 인증 필요한 기능
							   requests.requestMatchers(HttpMethod.GET, "/api/bookmarks/**", "/api/members/**").authenticated();
							   requests.requestMatchers(HttpMethod.POST, "/api/bookmarks/**", "/api/reviews/**", "/api/comments/**", "/api/auth/logout", "/api/reviewLikes/**", "/api/commentLikes/**", "/api/members/**").authenticated(); // 게시글 작성 및 신고 시 로그인 필요
							   
							   requests.requestMatchers("/api/admin/**").hasRole("ADMIN"); // 관리자 권한이 필요한 요청 (신고 조회, 상세 조회, 처리)

							   // 관리자 권한이 필요한 요청으로 연결 시 사용, DB상에 권한 컬럼에 ROLE_ADMIN이 있다면 패스, 아니면 아웃
						   })
						   /*
						    * sessionManagement : 세션을 어떻게 관리할것인지 지정
						    * sessionCreationPolicy : 세션 사용정책을 설정
						    */
						   .sessionManagement(manager -> 
								   				manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
						   .build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() { // reactive 없는 걸로 import
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(serverUrl, "http://localhost:5173"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Arrays java.utill에 있는 걸로 import
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
}