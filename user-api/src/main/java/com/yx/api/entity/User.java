package com.yx.api.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private int sex;          //1 男, 2女
    @NotNull(message = "telNo不能为空",groups = Create.class)
    private String telNo;
    private String qq;
    private String email;
    private String address;
    private int state;        //1 账号可用, 2不可用
    private Timestamp createTime;//账号创建时间

}
