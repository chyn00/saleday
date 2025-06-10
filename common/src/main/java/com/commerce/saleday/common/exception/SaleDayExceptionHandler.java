package com.commerce.saleday.common.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice//api return 시 response body 사용
public class SaleDayExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(SaleDayExceptionHandler.class);
  private static final String CONTEXT_TYPE = "application/json;charset=UTF-8";

  @ExceptionHandler(value = SaleDayException.class)
  public void saleDayExceptionHandle(HttpServletResponse response, SaleDayException saleDayException){
    response.setContentType(CONTEXT_TYPE);
    log.error(saleDayException.getExceptionCode().getMessage());

    HttpStatus status = saleDayException.getExceptionCode().getHttpStatus();
    response.setStatus(status.value());
  }

}
