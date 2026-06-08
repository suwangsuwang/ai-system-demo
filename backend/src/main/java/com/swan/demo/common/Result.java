package com.swan.demo.common;

public class Result<T> {

    private boolean success;
    private int code;
    private String message;
    private T data;

    public static <T>  Result<T> ok(T data) {
         Result<T> r = new Result<>();
         r.success = true;
         r.code = 200;
         r.message = "OK";
         r.data = data;
         return r;
    }

    public static <T> Result <T>  fail(String message) {
        Result<T> r = new Result<>();
        r.success = false;
        r.code = 500;
        r.message = message;
        return r;
    }

    // getter/setter
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
