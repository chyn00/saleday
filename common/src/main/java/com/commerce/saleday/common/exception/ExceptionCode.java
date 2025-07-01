package com.commerce.saleday.common.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionCode {

  CONFLICT("이미 존재하는 데이터입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  NO_SUCH_DATA("존재하지 않는 데이터입니다.", HttpStatus.BAD_REQUEST),
  OUT_OF_STOCK("재고가 부족합니다.", HttpStatus.CONFLICT);

  private final String message;
  private final HttpStatus httpStatus;

  ExceptionCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

}
