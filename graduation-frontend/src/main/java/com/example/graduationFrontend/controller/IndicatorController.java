package com.example.graduationFrontend.controller;

import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.domain.dto.indicator.IndicatorDTO;
import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import com.example.graduationFrontend.domain.vo.indicator.IndicatorVO;
import com.example.graduationFrontend.exception.ErrorException;
import com.example.graduationFrontend.utils.DataUtils;
import okhttp3.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/indicator")
public class IndicatorController extends BaseController {
    @GetMapping
    public String indicatorPage(HttpSession session, Model model) {
        Request request = buildGetRequest("/indicator", DataUtils.getUserToken(session), null);
        ResponseResult<List<IndicatorVO>> result = sendRequestAsList(request, IndicatorVO.class);
        if (result.isSuccess()) {
            model.addAttribute("indicators", result.getData());
            model.addAttribute("indicatorDTO", new IndicatorDTO());
            return "admin/indicator";
        }
        throw new ErrorException(result);
    }

    @PostMapping("/add")
    public String addIndicator(IndicatorDTO indicatorDTO, HttpSession session, Model model) {
        Request request = buildRequest("/indicator", DataUtils.getUserToken(session), null, HttpMethod.POST, indicatorDTO);
        ResponseResult<IndicatorVO> result = sendRequest(request, IndicatorVO.class);
        if (result.isSuccess()) {
            return indicatorPage(session, model);
        }
        throw new ErrorException(result);
    }

    @PostMapping("/delete/{id}")
    public String deleteIndicator(@PathVariable Long id, HttpSession session, Model model) {
        Request request = buildRequest("/indicator/" + id, DataUtils.getUserToken(session), null, HttpMethod.DELETE, null);
        ResponseResult<IndicatorVO> result = sendRequest(request, IndicatorVO.class);
        if (result.isSuccess()) {
            return indicatorPage(session, model);
        }
        throw new ErrorException(result);
    }

    @PostMapping("/update")
    public String updateIndicator(IndicatorVO indicator, HttpSession session, Model model) {
        IndicatorDTO indicatorDTO = new IndicatorDTO();
        indicatorDTO.setName(indicator.getName());
        indicatorDTO.setUnit(indicator.getUnit());
        indicatorDTO.setStandardRange(indicator.getStandardRange());
        Request request = buildRequest("/indicator/" + indicator.getId(), DataUtils.getUserToken(session), null, HttpMethod.PUT, indicatorDTO);
        ResponseResult<IndicatorVO> result = sendRequest(request, IndicatorVO.class);
        if (result.isSuccess()) {
            return indicatorPage(session, model);
        }
        throw new ErrorException(result);
    }



}
