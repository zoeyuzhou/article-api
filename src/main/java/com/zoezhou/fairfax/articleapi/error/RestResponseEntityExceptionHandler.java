package com.zoezhou.fairfax.articleapi.error;

import com.zoezhou.fairfax.articleapi.exception.ArticleResouceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.NestedServletException;

public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    public RestResponseEntityExceptionHandler() {
        super();
    }

    // API
    // 400
    @ExceptionHandler({ DataIntegrityViolationException.class, IllegalArgumentException.class})
    @ResponseBody
    public ResponseEntity handleBadRequest(final RuntimeException ex, final WebRequest request) {
        final String bodyOfResponse = "Please check the input";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 404
    @ExceptionHandler({ArticleResouceNotFoundException.class})
    public ResponseEntity handleNotFoundequest(final RuntimeException ex, final WebRequest request) {
        final String bodyOfResponse = "Cannot find requested article";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 500
    @ExceptionHandler({ NullPointerException.class, IllegalStateException.class})
    public ResponseEntity handleInternal(final RuntimeException ex, final WebRequest request) {
        final String bodyOfResponse = "Please check the input";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }


}
