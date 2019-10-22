package com.resto.msgc.backend.card.dcrf;

import java.io.Serializable;

/**
 * Created by KONATA on 2018/3/1.
 */
public class CardResultDto implements Serializable{
    private boolean success;
    private String message;

    public CardResultDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
