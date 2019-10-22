package com.resto.msgc.backend.card.constants;

/**
 * Created by disvenk.dai on 2018-04-22 15:14
 */
public class RedChoose {

    public static String redChoose(Integer type){
        String redType = "";
        switch (type){
            case 0 :
                redType="评论红包";
                break;
            case 1:
                redType="分享红包";
                break;
            case 2:
                redType="退菜红包";
                break;
            case 3:
                redType="第三方储值余额";
                break;
            case 4:
                redType="消费返利余额";
                break;
            default:
                redType="";
                break;
        }

        return redType;
    }

}
