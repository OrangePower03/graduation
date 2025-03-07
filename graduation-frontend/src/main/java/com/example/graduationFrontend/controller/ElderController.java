package com.example.graduationFrontend.controller;

import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.domain.dto.indicator.ElderDTO;
import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import com.example.graduationFrontend.domain.vo.indicator.ElderIndicatorDetailVO;
import com.example.graduationFrontend.domain.vo.indicator.PatchElderIndicatorVO;
import com.example.graduationFrontend.domain.vo.user.PersonVO;
import com.example.graduationFrontend.domain.vo.user.UserInfoVO;
import com.example.graduationFrontend.exception.ErrorException;
import com.example.graduationFrontend.utils.DataUtils;
import okhttp3.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/elder")
public class ElderController extends BaseController {
    @GetMapping("/youngster/{status}")
    public String getYoungsterPage(HttpSession session, Model model, @PathVariable Integer status) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/person/" + status, token, null);
        ResponseResult<List<PersonVO>> result = sendRequestAsList(request, PersonVO.class);
        return toYoungsterPage(model, result, status);
    }

    @GetMapping("/bind/{youngsterId}")
    public String bindYoungster(HttpSession session, @PathVariable Long youngsterId) {
        String token = DataUtils.getUserToken(session);
        Request request = buildRequest("/person/" + youngsterId, token, null, HttpMethod.POST, null);
        ResponseResult<UserInfoVO> result = sendRequest(request, UserInfoVO.class);
        if (result.isSuccess()) {
            return "redirect:/elder/youngster/0";
        }
        throw new ErrorException(result);
    }

    @PostMapping("/youngster/delete/{youngsterId}")
    public String deleteYoungster(HttpSession session, @PathVariable Long youngsterId) {
        String token = DataUtils.getUserToken(session);
        Request request = buildRequest("/person/" + youngsterId, token, null, HttpMethod.DELETE, null);
        ResponseResult<UserInfoVO> result = sendRequest(request, UserInfoVO.class);
        if (result.isSuccess()) {
            return "redirect:/elder/youngster/0";
        }
        throw new ErrorException(result);
    }

    @GetMapping("/indicator")
    public String getElderIndicatorPage(HttpSession session, Model model, ElderDTO elderDTO) {
        return getElderIndicatorPage(session, elderDTO, model, 1);
    }

    @GetMapping("/indicator/{pageNum}")
    public String getElderIndicatorPage(HttpSession session, ElderDTO elderDTO, Model model, @PathVariable Integer pageNum) {
        String token = DataUtils.getUserToken(session);
        Map<String, String> params = new HashMap<>();
        if (elderDTO.getNormal() != -1) {
            params.put("normal", String.valueOf(elderDTO.getNormal()));
        }
        if (!StringUtils.isEmpty(elderDTO.getStartTime())) {
            params.put("startTime", elderDTO.getStartTime());
        }

        Request request = buildGetRequest("/indicator/elder", token, pageNum, params);
        ResponseResult<PatchElderIndicatorVO> result = sendRequestAsPatch(request);
        return toIndicatorPage(model, result, elderDTO, elderDTO.getNormal());
    }

    @GetMapping("/indicator/detail")
    @ResponseBody
    public ResponseResult<List<ElderIndicatorDetailVO>> getIndicatorDetail(HttpSession session, @RequestParam(value = "checkTime") String checkTime) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/indicator/elder/detail", token, Map.of("checkTime", checkTime));
        return sendRequestAsList(request, ElderIndicatorDetailVO.class);
    }

    private String toYoungsterPage(Model model, ResponseResult<List<PersonVO>> result, Integer status) {
        if (result.isSuccess()) {
            model.addAttribute("youngsters", result.getData());
            model.addAttribute("status", status);
            return "elder/youngster";
        }
        throw new ErrorException(result);
    }

    private String toIndicatorPage(Model model, ResponseResult<PatchElderIndicatorVO> result, ElderDTO elderDTO, Integer normal) {
        if (!result.isSuccess()) {
            throw new ErrorException(result);
        }
        int total = result.getData().getAllIndicator().getTotal();
        int pageSize = result.getData().getAllIndicator().getPageSize();
        int size = (int) Math.ceil((double) total / pageSize);
        Long elderId = result.getData().getElderId();
        model.addAttribute("elderId", elderId);
        model.addAttribute("elderDTO", elderDTO);
        model.addAttribute("normal", normal);
        model.addAttribute("indicators", result.getData().getAllIndicator().getRows());
        model.addAttribute("total", total);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("current", result.getData().getAllIndicator().getCurrent());
        model.addAttribute("size", size);
        return "/elder/indicator";
    }
}
