package com.kh.jde.admin.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RankResponse {
	
	private String policyCode;
	private String policyName;
	private int topN;
	private int periodDays;
	private int policyId;
	private String status;
	private String description;
	private Date createdAt;
	private Date updatedAt;

}
