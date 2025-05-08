package com.example.graduationFrontend.controller;

import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.constants.UserConstants;
import com.example.graduationFrontend.domain.dto.indicator.ElderDTO;
import com.example.graduationFrontend.domain.vo.common.PageVO;
import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import com.example.graduationFrontend.domain.vo.indicator.ElderIndicatorDetailVO;
import com.example.graduationFrontend.domain.vo.indicator.PatchElderIndicatorVO;
import com.example.graduationFrontend.domain.vo.user.PersonVO;
import com.example.graduationFrontend.domain.vo.user.UserInfoVO;
import com.example.graduationFrontend.exception.ErrorException;
import com.example.graduationFrontend.utils.DataUtils;
import com.example.graduationFrontend.utils.DateUtils;
import okhttp3.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
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
        Request request = buildGetRequest("/indicator/elder/" + id, token, 1, null);
        ResponseResult<PatchElderIndicatorVO> result = sendRequestAsPatch(request);
        return toElderIndicatorPage(model, result, -1);
    }

    @PostMapping("/indicator/{elderId}")
    public String getElderIndicatorPage(HttpSession session, ElderDTO elderDTO, Model model, @PathVariable Long elderId) {
        return getElderIndicatorPage(session, elderDTO, model, elderId, 1);
    }

    @GetMapping("/indicator/{elderId}/delete/{id}")
    public String deleteElderIndicator(HttpSession session, ElderDTO elderDTO, Model model, @PathVariable("elderId") Long elderId, @PathVariable("id") Long id) {
        String token = DataUtils.getUserToken(session);
        Request request = buildRequest("/indicator/elder/" + elderId + "/" + id, token, null, HttpMethod.DELETE, null);
        ResponseResult<Void> result = sendRequest(request);
        if (result.isSuccess()) {
            return getElderIndicatorPage(session, elderDTO, model, elderId, 1);
        }
        throw new ErrorException(result);
    }

    @GetMapping("/indicator/{elderId}/{pageNum}")
    public String getElderIndicatorPage(HttpSession session, ElderDTO elderDTO, Model model, @PathVariable Long elderId, @PathVariable Integer pageNum) {
        String token = DataUtils.getUserToken(session);
        Map<String, String> params = new HashMap<>();
        if (elderDTO.getNormal() != -1) {
            params.put("normal", String.valueOf(elderDTO.getNormal()));
        }
        if (!StringUtils.isEmpty(elderDTO.getStartTime())) {
            params.put("startTime", elderDTO.getStartTime());
        }

        Request request = buildGetRequest("/indicator/elder/" + elderId, token, pageNum, params);
        ResponseResult<PatchElderIndicatorVO> result = sendRequestAsPatch(request);
        return toElderIndicatorPage(model, result, elderDTO.getNormal());
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

    @GetMapping("/indicator/detail/{elderId}")
    @ResponseBody
    public ResponseResult<List<ElderIndicatorDetailVO>> getElderIndicatorDetail(HttpSession session, @PathVariable Long elderId, @RequestParam(value = "checkTime") String checkTime) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/indicator/elder/detail/" + elderId, token, Map.of("checkTime", checkTime));
        return sendRequestAsList(request, ElderIndicatorDetailVO.class);
    }



    @GetMapping("/elder/exact")
    @ResponseBody
    public ResponseResult<PersonVO> getExactPerson(@RequestParam(value = "idNumber") String idNumber,
                                                   @RequestParam(value = "name") String name, HttpSession session) {
        String token = DataUtils.getUserToken(session);
        Request request = buildGetRequest("/person/select", token, Map.of("idNumber", idNumber, "name", name));
        return sendRequest(request, PersonVO.class);
    }

    @PostMapping("/elder/delete/{relationId}")
    public String deleteElder(@PathVariable("relationId") Long relationId, HttpSession session) {
        String token = DataUtils.getUserToken(session);
        Request request = buildRequest("/person/" + relationId, token, null, HttpMethod.DELETE, null);
        ResponseResult<UserInfoVO> result = sendRequest(request, UserInfoVO.class);
        if (result.isSuccess()) {
            return "redirect:/youngster/elder/0";
        }
        throw new ErrorException(result);
    }

    private String toElderPage(Model model, ResponseResult<List<PersonVO>> result, Integer status) {
        if (result.isSuccess()) {
            model.addAttribute("elders", result.getData());
            model.addAttribute("status", status);
            return "/youngster/elder";
        }
        throw new ErrorException(result);
    }

    private String toElderIndicatorPage(Model model, ResponseResult<PatchElderIndicatorVO> result, Integer normal) {
        if (!result.isSuccess()) {
            throw new ErrorException(result);
        }
        int total = result.getData().getAllIndicator().getTotal();
        int pageSize = result.getData().getAllIndicator().getPageSize();
        int size = (int) Math.ceil((double) total / pageSize);
        Long elderId = result.getData().getElderId();
        ElderDTO elderDTO = new ElderDTO();
        model.addAttribute("elderId", elderId);
        model.addAttribute("elderDTO", elderDTO);
        model.addAttribute("normal", normal);
        model.addAttribute("indicators", result.getData().getAllIndicator().getRows());
        model.addAttribute("total", total);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("current", result.getData().getAllIndicator().getCurrent());
        model.addAttribute("size", size);
        return "/youngster/indicator";
    }
}
