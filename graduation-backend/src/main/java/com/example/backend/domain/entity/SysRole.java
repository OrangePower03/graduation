package com.example.backend.domain.entity;

import java.util.Date;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * (SysRole)表实体类
 *
 * @author makejava
 * @since 2025-02-15 21:12:44
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role")
public class SysRole extends BaseEntity {
    //角色名称
    private String name;

    //权限名
    private String permissionKey;
}

