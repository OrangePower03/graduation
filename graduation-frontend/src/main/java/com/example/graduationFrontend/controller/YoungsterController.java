package com.example.graduationFrontend.controller;

import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.constants.UserConstants;
import com.example.graduationFrontend.domain.vo.common.PageVO;
import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import com.example.graduationFrontend.domain.vo.indicator.PatchElderIndicatorVO;
import com.example.graduationFrontend.domain.vo.user.PersonVO;
import com.example.graduationFrontend.domain.vo.user.UserInfoVO;
import com.example.graduationFrontend.exception.ErrorException;
import com.example.graduationFrontend.utils.DataUtils;
import okhttp3.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/youngster")
public class YoungsterController extends BaseController {
    @GetMapping("/elder/{status}")
    public String getElderPage(HttpSession session, Model model, @PathVariable Integer status) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/person/" + status, token, null);
        ResponseResult<List<PersonVO>> result = sendRequestAsList(request, PersonVO.class);
        return toElderPage(model, result, status);
    }

    @GetMapping("/indicator/{id}")
    public String getElderIndicatorPage(HttpSession session, @PathVariable("id") Long id, Model model) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/indicator/elder/" + id, token, null);
        ResponseResult<PatchElderIndicatorVO> result = sendRequest(request, PatchElderIndicatorVO.class);
        return toElderIndicatorPage(model, result);
    }

    @GetMapping("/elder/bind/{id}")
    public String bindElder(HttpSession session, @PathVariable("id") Long id) {
        String token = DataUtils.getUserToken(session);
        Request request = buildRequest("/person/" + id, token, null, HttpMethod.POST, null);
        ResponseResult<UserInfoVO> result = sendRequest(request, UserInfoVO.class);
        if (result.isSuccess()) {
            return "redirect:/youngster/elder/1";
        }
        throw new ErrorException(result);
    }


    @GetMapping("/elder/exact")
    @ResponseBody
    public ResponseResult<PersonVO> getExactPerson(@RequestParam(value = "idNumber") String idNumber,
                                                   @RequestParam(value = "name") String name, HttpSession session) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/person/select", token, Map.of("idNumber", idNumber, "name", name));
        return sendRequest(request, PersonVO.class);
    }

    private String toElderPage(Model model, ResponseResult<List<PersonVO>> result, Integer status) {
        if (result.isSuccess()) {
            model.addAttribute("elders", result.getData());
            model.addAttribute("status", status);
            return "/youngster/elder";
        }
        throw new ErrorException(result);
    }

    private String toElderIndicatorPage(Model model, ResponseResult<PatchElderIndicatorVO> result) {
        int total = result.getData().getAllIndicator().getTotal();
        int pageSize = result.getData().getAllIndicator().getPageSize();
        int size = (int) Math.ceil((double) total / pageSize);
        model.addAttribute("elderId", result.getData().getElderId());
        model.addAttribute("indicators", result.getData().getAllIndicator().getRows());
        model.addAttribute("total", total);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("current", result.getData().getAllIndicator().getCurrent());
        model.addAttribute("size", size);
        return "/youngster/indicator";
    }
}
