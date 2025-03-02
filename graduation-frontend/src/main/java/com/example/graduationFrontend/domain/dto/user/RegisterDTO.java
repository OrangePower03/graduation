package com.example.graduationFrontend.domain.dto.user;

import com.example.graduationFrontend.domain.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO extends BaseDTO {
    private String username;

    private String password;

    private String repeatPassword;

    private String phone;

    private String name;

    private String idNumber;
}
