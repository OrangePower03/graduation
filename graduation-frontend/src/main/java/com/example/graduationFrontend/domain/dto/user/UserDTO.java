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
public class UserDTO extends BaseDTO {
    private String username;

    private String name;

    private String phone;

    private Long roleId;

    private int pageSize = 10;

    private int pageNum = 1;
}
