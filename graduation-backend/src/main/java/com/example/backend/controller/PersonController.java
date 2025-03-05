package com.example.backend.controller;

import com.example.backend.constants.UserConstants;
import com.example.backend.domain.vo.indicator.ElderIndicatorDetailVO;
import com.example.backend.domain.vo.user.PersonVO;
import com.example.backend.domain.vo.user.UserInfoVO;
import com.example.backend.service.SysUserService;
import com.example.backend.utils.object.ObjectUtils;
import com.example.backend.utils.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;



@RestController
@RequestMapping("/person")
public class PersonController extends BaseController {
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
    @GetMapping("/{status}")
    public ResponseResult<List<PersonVO>> listRelationPerson(@PathVariable(value = "status") Integer status) {
        return ok(personService.listRelationPerson(ObjectUtils.requireNonNullElse(status, UserConstants.RELATION_STATUS_NORMAL)));
    }

    @PreAuthorize("@MA.isYoungster()")
    @GetMapping("/select")
    public ResponseResult<PersonVO> getExactPerson(@RequestParam(value = "idNumber") String idNumber,
                                                   @RequestParam(value = "name") String name) {

        return ok(personService.getExactPerson(idNumber, name));
    }



}
