package com.resto.msgc.backend.card.responseEntity;

import org.springframework.http.HttpStatus;

public class RestResponseEntity extends BaseResponseEntity {
    public Object data;

    public RestResponseEntity(RestResponseEntity 页码不能为空, HttpStatus ok) {
    }

    public RestResponseEntity(int responseStatus, String message, Object data,String expMsg) {
        super(responseStatus, message,expMsg);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
