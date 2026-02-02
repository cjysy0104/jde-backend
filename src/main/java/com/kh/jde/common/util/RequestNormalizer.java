package com.kh.jde.common.util;

import org.springframework.stereotype.Component;

import com.kh.jde.review.model.dto.ScrollRequest;

@Component
public class RequestNormalizer {

	public ScrollRequest applyScroll(ScrollRequest scroll, int defaultSize) {
        if (scroll == null) scroll = new ScrollRequest();

        Integer reqSize = scroll.getSize();
        int size = (reqSize == null || reqSize <= 0) ? defaultSize : reqSize;

        scroll.setSize(size);
        scroll.setSizePlusOne(size + 1);
        return scroll;
    }
}
