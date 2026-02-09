package com.kh.jde.review.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileUpdateResult {
	private final List<ReviewFileDTO> filesToKeep;
	private final List<ReviewFileDTO> filesToDelete;
}
