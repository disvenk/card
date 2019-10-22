package com.resto.msgc.backend.card.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by xielc on 2018/4/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbCardCompanyDto implements Serializable {

    private static final long serialVersionUID = 8939745984116292543L;

    @ApiModelProperty(value = "公司id")
    private Long  companyId;

    @ApiModelProperty(value = "公司名称")
    private String  companyName;

    @ApiModelProperty(value = "员工数量")
    private Integer employeesNum;
}
