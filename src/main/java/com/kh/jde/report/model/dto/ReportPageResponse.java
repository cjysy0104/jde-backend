package com.kh.jde.report.model.dto;

import java.util.List;

import com.kh.jde.common.page.PageInfo;

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
public class ReportPageResponse<T> {
	
	private List<T> list;
	private PageInfo pageInfo;
}
