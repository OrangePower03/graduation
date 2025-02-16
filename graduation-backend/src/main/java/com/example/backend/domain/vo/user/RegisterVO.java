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
public class RegisterVO extends BaseVO {
    private String username;

    private String password;

    private String phone;

    private String name;

    private String idNumber;

    private Integer age;
}
