package com.example.backend.domain.vo.user;

import com.example.backend.domain.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO extends BaseVO {
    private Long id;

    private String name;

    private String permissionKey;

    private Date createTime;
}
