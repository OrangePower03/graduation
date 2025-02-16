package com.example.backend.controller;

import com.example.backend.domain.vo.PageVO;
import com.example.backend.domain.vo.user.PersonVO;
import com.example.backend.domain.vo.user.UserInfoVO;
import com.example.backend.service.SysUserService;
import com.example.backend.utils.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.backend.utils.web.ResponseResult.ok;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private SysUserService personService;

    @PreAuthorize("@MA.isElderOrYoungster()")
    @PostMapping("/{relationId}")
    public ResponseResult<UserInfoVO> addPersonRelation(@PathVariable Long relationId) {
        return ok(personService.addPersonRelation(relationId));
    }

    @PreAuthorize("@MA.isElderOrYoungster()")
    @DeleteMapping("/{relationId}")
    public ResponseResult<UserInfoVO> removePersonRelation(@PathVariable Long relationId) {
        return ok(personService.removePersonRelation(relationId));
    }

    @PreAuthorize("@MA.isElderOrYoungster()")
    @GetMapping
    public ResponseResult<PageVO<PersonVO>> listRelationPerson() {
        return ok(personService.listRelationPerson());
    }
}
