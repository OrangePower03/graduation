package com.example.backend.domain.vo.user;

import com.example.backend.domain.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO extends BaseVO {
    private Long id;

    private String username;

    private String name;

    private String idNumber;

    private String phone;

    private Integer age;

    private Long roleId;

    private String roleName;

    private String token;
}
