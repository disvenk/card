package com.resto.msgc.backend.card.responseEntity;

public class BaseResponseEntity {
    public int stateCode;    //业务响应状态，非HTTP状态码，HTTP状态码在 ResponseEntity 中指定
    public String message;   //消息
    public String expMsg;

    public BaseResponseEntity() {

    }

    public BaseResponseEntity(int stateCode, String message,String expMsg) {
        this.stateCode = stateCode;
        this.message = message;
        this.expMsg = expMsg;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExpMsg() {
        return expMsg;
    }

    public void setExpMsg(String expMsg) {
        this.expMsg = expMsg;
    }
}
