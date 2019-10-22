package com.resto.msgc.backend.card.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bruce on 2017-12-26 15:28
 */
@Data
public class WeekAndTimeDto implements Serializable{

    private static final long serialVersionUID = 8846751180057937659L;

    private String[] weekArray;
    private String timeArray;

}
