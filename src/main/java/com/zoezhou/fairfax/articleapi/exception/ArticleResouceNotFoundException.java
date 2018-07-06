package com.zoezhou.fairfax.articleapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleResouceNotFoundException extends RuntimeException {
    public ArticleResouceNotFoundException() {
        super();
    }

    public ArticleResouceNotFoundException( String errMessage ) {
        super(errMessage);
    }
}
