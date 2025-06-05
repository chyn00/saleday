package com.commerce.saleday.common.exception;

public class SaleDayException extends RuntimeException {
  private final ExceptionCode exceptionCode;

  public SaleDayException(ExceptionCode exceptionCode) {
    this.exceptionCode = exceptionCode;
  }

  public ExceptionCode getExceptionCode() {
    return exceptionCode;
  }
}
