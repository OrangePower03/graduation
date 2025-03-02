package com.example.graduationFrontend.controller;

import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.domain.dto.user.RoleDTO;
import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import com.example.graduationFrontend.domain.vo.user.RoleVO;
import com.example.graduationFrontend.exception.ErrorException;
import com.example.graduationFrontend.utils.DataUtils;
import okhttp3.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    @GetMapping("/role")
    public String operatorRolePage(HttpSession session, Model model) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/role", token, null);
        ResponseResult<List<RoleVO>> result = sendRequestAsList(request, RoleVO.class);
        if (result.isSuccess()) {
            return toRolePage(model, result.getData());
        }
        throw new ErrorException(result);
    }

    @PostMapping("/role/list")
    public String handleRoleSearch(HttpSession session, RoleDTO roleDTO, Model model) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/role", token, Map.of("name", roleDTO.getName()));
        ResponseResult<List<RoleVO>> result = sendRequestAsList(request, RoleVO.class);
        if (result.isSuccess()) {
            return toRolePage(model, result.getData());
        }
        throw new ErrorException(result);
    }

    @PostMapping("/role/update")
    public String handleRoleUpdate(HttpSession session, RoleVO role, Model model) {
        String token = DataUtils.getUserToken(session);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(role.getName());
        roleDTO.setPermissionKey(role.getPermissionKey());
        Request request = buildRequest("/role/" + role.getId(), token, null, HttpMethod.PUT, roleDTO);
        ResponseResult<List<RoleVO>> result = sendRequestAsList(request, RoleVO.class);
        if (result.isSuccess()) {
            return toRolePage(model, result.getData());
        }
        throw new ErrorException(result);
    }

    @PostMapping("/role/delete/{id}")
    public String handleRoleDelete(HttpSession session, @PathVariable Long id, Model model) {
        String token = DataUtils.getUserToken(session);
        Request request = buildRequest("/role/" + id, token, null, HttpMethod.DELETE, null);
        ResponseResult<List<RoleVO>> result = sendRequestAsList(request, RoleVO.class);
        if (result.isSuccess()) {
            return toRolePage(model, result.getData());
        }
        throw new ErrorException(result);
    }

    @PostMapping("/role/add")
    public String handleRoleAdd(HttpSession session, RoleDTO addRoleDTO, Model model) {
        String token = DataUtils.getUserToken(session);
        Request request = buildRequest("/role", token, null, HttpMethod.POST, addRoleDTO);
        ResponseResult<List<RoleVO>> result = sendRequestAsList(request, RoleVO.class);
        if (result.isSuccess()) {
            return toRolePage(model, result.getData());
        }
        throw new ErrorException(result);
    }

    private String toRolePage(Model model, List<RoleVO> roles) {
        model.addAttribute("roles", roles);
        model.addAttribute("roleDTO", new RoleDTO());
        model.addAttribute("addRoleDTO", new RoleDTO());
        return "/admin/role";
    }
}
