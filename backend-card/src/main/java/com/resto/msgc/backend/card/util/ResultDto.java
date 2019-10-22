package com.resto.msgc.backend.card.util;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ResultDto implements Serializable {

    private static final long serialVersionUID = 6288374846131788743L;

    @ApiModelProperty(value = "返回信息")
    private String message;

    @ApiModelProperty(value = "状态码")
    private int statusCode;

    @ApiModelProperty(value = "是否成功")
    private boolean success;

    @ApiModelProperty(value = "返回数据")
    private Object data;

    public ResultDto() {
    }

    public static ResultDto getSuccess() {
        return new ResultDto(Constant.TRUE);
    }

    public static ResultDto getSuccess(String message) {
        return new ResultDto(message, Constant.SUCCESS_CODE, Constant.TRUE);
    }

    public static ResultDto getSuccess(Object data) {
        return new ResultDto(Constant.MESSAGE_OK, Constant.SUCCESS_CODE, Constant.TRUE, data);
    }

    public static ResultDto getError(String message) {
        return new ResultDto(message, Constant.ERROR_CODE, Constant.FALSE);
    }

    public ResultDto(String message, int statusCode, boolean success, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.success = success;
        this.data = data;
    }

    public ResultDto(String message, int statusCode, boolean success) {
        this.message = message;
        this.statusCode = statusCode;
        this.success = success;
    }

    public ResultDto(boolean isSuccess) {
        this.success = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
