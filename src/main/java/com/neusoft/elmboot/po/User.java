package com.neusoft.elmboot.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String password;
    private String userName;
    private Integer userSex;
    private String userImg;
    private Integer delTag;
    private Integer userType; // 0: 正常用户, 1: 管理员
    private Integer isVip;    // 0: 普通用户, 1: VIP会员
    private Double redEnvelope; // 红包余额
    private Integer goldCoins;  // 金币余额
}
