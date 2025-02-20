package com.example.backend.domain.entity;

import java.util.Date;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2025-02-15 21:11:16
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class SysUser extends BaseEntity {
    //用户名
    private String username;

    //密码
    private String password;

    //用户的真实姓名
    private String name;

    //用户的身份证号
    private String idNumber;

    //用户的手机
    private String phone;

    //用户的年龄
    private Integer age;

    //用户的性别，0男，1女
    private Integer sex;

    //角色id外键
    private Long roleId;

    //用户状态，0正常，1停用，2监控
    private Integer status;
}

