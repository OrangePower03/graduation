package com.example.backend.domain.dto.user;

import com.example.backend.domain.dto.BaseDTO;
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

    private String phone;
}
