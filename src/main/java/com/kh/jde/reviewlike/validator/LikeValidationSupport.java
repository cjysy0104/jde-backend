package com.kh.jde.reviewlike.validator;

import org.springframework.stereotype.Component;

@Component
public class LikeValidationSupport {

    public void validateTargetExists(boolean exists, String notFoundMessage) {
        if (!exists) throw new IllegalArgumentException(notFoundMessage);
    }

    public void validateNotAlreadyLiked(boolean alreadyLiked, String duplicatedMessage) {
        if (alreadyLiked) throw new IllegalStateException(duplicatedMessage);
    }

    public void validateAlreadyLiked(boolean alreadyLiked, String notLikedMessage) {
        if (!alreadyLiked) throw new IllegalStateException(notLikedMessage);
    }

    public void validateDbAffected(int affected, String failMessage) {
        if (affected <= 0) throw new IllegalStateException(failMessage);
    }
}
