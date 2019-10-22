package com.resto.msgc.backend.card.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "tb_customer")
public class TbCustomer implements Serializable {

    private static final long serialVersionUID = 6971875787517630311L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ApiModelProperty("用户微信Id")
    @Column(name = "wechat_id")
    private String wechatId;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("电话")
    private String telephone;

    @ApiModelProperty("头像")
    @Column(name = "head_photo")
    private String headPhoto;

    @ApiModelProperty("默认自提点ID")
    @Column(name = "default_delivery_point")
    private Integer defaultDeliveryPoint;

    @Column(name = "is_bind_phone")
    private Boolean isBindPhone;

    @ApiModelProperty("注册时间")
    @Column(name = "regiest_time")
    private Date regiestTime;

    @ApiModelProperty("第一次下单时间")
    @Column(name = "first_order_time")
    private Date firstOrderTime;

    @ApiModelProperty("最后一次登录时间")
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "brand_id")
    private String brandId;

    @ApiModelProperty("性别")
    private Integer sex;

    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("国籍")
    private String country;

    @ApiModelProperty("最后一次订单的店铺")
    @Column(name = "last_order_shop")
    private String lastOrderShop;

    @ApiModelProperty("最新查看的通知时间")
    @Column(name = "new_notice_time")
    private Date newNoticeTime;

    @ApiModelProperty("分享人id")
    @Column(name = "share_customer")
    private String shareCustomer;

    @ApiModelProperty("注册店铺的id")
    @Column(name = "register_shop_id")
    private String registerShopId;

    @ApiModelProperty("记录生成时是否关注  1为关注 0为未关注  null为不在等位红包模式")
    @Column(name = "is_now_register")
    private Integer isNowRegister;

    @ApiModelProperty("是否点击过邀请链接")
    @Column(name = "is_share")
    private Boolean isShare;

    private Date birthday;

    @ApiModelProperty("关联customer_detail")
    @Column(name = "customer_detail_id")
    private String customerDetailId;

    @ApiModelProperty("注册时间")
    @Column(name = "bind_phone_time")
    private Date bindPhoneTime;

    @ApiModelProperty("注册店铺")
    @Column(name = "bind_phone_shop")
    private String bindPhoneShop;

    @ApiModelProperty("用户第一次进入系统时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("领取过的实时优惠卷Id")
    @Column(name = "real_time_coupon_ids")
    private String realTimeCouponIds;

    @ApiModelProperty("是否关注  0 未关注  1 关注")
    private Boolean subscribe;

    @ApiModelProperty("桌号继续  进入店铺后销毁")
    @Column(name = "last_table_number")
    private String lastTableNumber;

    @ApiModelProperty("领取过的生日优惠卷Id")
    @Column(name = "birthday_coupon_ids")
    private String birthdayCouponIds;

    @ApiModelProperty("领取过的分享优惠卷Id")
    @Column(name = "share_coupon_ids")
    private String shareCouponIds;

    @ApiModelProperty("会员序号")
    @Column(name = "serial_number")
    private Long serialNumber;

    @ApiModelProperty("第一次点击过的分享链接")
    @Column(name = "share_link")
    private String shareLink;

    @Column(name = "card_id")
    private String cardId;

    private String code;

}