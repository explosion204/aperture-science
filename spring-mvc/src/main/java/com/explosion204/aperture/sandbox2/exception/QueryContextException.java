package com.explosion204.aperture.sandbox2.exception;

public class QueryContextException extends RuntimeException {
  public QueryContextException(String message) {
    super(message);
  }

  public QueryContextException(String message, Throwable cause) {
    super(message, cause);
  }
}
