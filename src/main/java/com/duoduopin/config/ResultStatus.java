package com.duoduopin.config;

/**
 * 结果状态
 *
 * @author z217
 * @date 2021/01/07
 */
public enum ResultStatus {
  SUCCESS(100, "success"),
  UNKNOWN(-1000, "unknown problem"),
  USERNAME_OR_PASSWORD_ERROR(-1001, "Username or password error"),
  USER_NOT_FOUND(-1002, "User not found"),
  USER_NOT_LOGIN(-1003, "User not login"),
  USERNAME_EXIST(-1004, "Username already exists"),
  UNAUTHORIZED(-1005, "Insufficient authority"),
  BILL_NOT_FOUND(-1006, "Share bill not found"),
  BILL_ILLEGAL(-1007, "Share bill illegal"),
  JOIN_FAILED(-1008, "Join team failed"),
  DUPLICATE_JOIN(-1009, "Join team duplicated");
  private int code;
  private String message;
  
  ResultStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }
  
  public int getCode() {
    return code;
  }
  
  public void setCode(int code) {
    this.code = code;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
}
