package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by xielc on 2018/4/23.
 */
@Data
public class UserLoginDto  implements Serializable{

    private static final long serialVersionUID = -3462866220128654353L;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "密码")
    private String password;
}
