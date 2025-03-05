package com.example.graduationFrontend.controller;

import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.domain.dto.indicator.PatchElderIndicatorDTO;
import com.example.graduationFrontend.domain.dto.user.RoleDTO;
import com.example.graduationFrontend.domain.dto.user.UserDTO;
import com.example.graduationFrontend.domain.vo.common.PageVO;
import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import com.example.graduationFrontend.domain.vo.indicator.IndicatorVO;
import com.example.graduationFrontend.domain.vo.user.ListUserVO;
import com.example.graduationFrontend.domain.vo.user.RoleVO;
import com.example.graduationFrontend.domain.vo.user.UserInfoVO;
import com.example.graduationFrontend.exception.ErrorException;
import com.example.graduationFrontend.utils.DataUtils;
import okhttp3.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
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


    @GetMapping("/user")
    public String operatorUserPage(HttpSession session, Model model) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/user", token, 1, PageVO.DEFAULT_PAGE_SIZE, null);
        ResponseResult<PageVO<ListUserVO>> result = sendRequestAsPage(request, ListUserVO.class);
        return toUserPage(session, model, new UserDTO(), result.getData());
    }


    @PostMapping("/user/list")
    public String handleUserSearch(HttpSession session, UserDTO userDTO, Model model) {
        String token = DataUtils.getUserToken(session);

        Request request = buildGetRequest("/user", token, 1, PageVO.DEFAULT_PAGE_SIZE, buildParam(userDTO));
        ResponseResult<PageVO<ListUserVO>> result = sendRequestAsPage(request, ListUserVO.class);
        return toUserPage(session, model, userDTO, result.getData());
    }

    @GetMapping("/user/list/{pageNum}")
    public String handleUserSearch(HttpSession session, UserDTO userDTO, Model model, @PathVariable int pageNum) {
        String token = DataUtils.getUserToken(session);

        Request request = buildGetRequest("/user", token, pageNum, PageVO.DEFAULT_PAGE_SIZE, buildParam(userDTO));
        ResponseResult<PageVO<ListUserVO>> result = sendRequestAsPage(request, ListUserVO.class);
        return toUserPage(session, model, userDTO, result.getData());
    }


    @PostMapping("/user/delete/{id}")
    public String handleDeleteUser(HttpSession session, @PathVariable Long id, UserDTO userDTO, Model model) {
        String token = DataUtils.getUserToken(session);

        Request request = buildRequestWithPage("/user/" + id, token, null, HttpMethod.DELETE, null);
        ResponseResult<PageVO<ListUserVO>> result = sendRequestAsPage(request, ListUserVO.class);
        return toUserPage(session, model, userDTO, result.getData());
    }

    @GetMapping("/indicator/list")
    @ResponseBody
    public List<IndicatorVO> handleIndicatorList(HttpSession session) {
        Request request = buildGetRequest("/indicator", DataUtils.getUserToken(session), null);
        ResponseResult<List<IndicatorVO>> result = sendRequestAsList(request, IndicatorVO.class);
        if (result.isSuccess()) {
            return result.getData();
        }
        throw new ErrorException(result);
    }

    @PostMapping("/indicator")
    @ResponseBody
    public ResponseResult<Integer> handleIndicatorList(@RequestBody PatchElderIndicatorDTO indicatorDTO, HttpSession session) {
        Request request = buildRequest("/indicator/elder", DataUtils.getUserToken(session), null, HttpMethod.POST, indicatorDTO);
        return sendRequest(request, Integer.class);
    }




    private String toUserPage(HttpSession session, Model model, UserDTO userDTO, PageVO<ListUserVO> users) {

        Request request = buildGetRequest("/role", DataUtils.getUserToken(session), null);
        ResponseResult<List<RoleVO>> result = sendRequestAsList(request, RoleVO.class);
        if (result.isSuccess()) {
            model.addAttribute("users", users.getRows());
            model.addAttribute("total", users.getTotal());
            model.addAttribute("current", users.getCurrent());
            model.addAttribute("size", (int) Math.ceil((double) users.getTotal() / users.getPageSize()));
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("roles", result.getData());
            return "/admin/user";
        }
        throw new ErrorException(result);
    }

}
