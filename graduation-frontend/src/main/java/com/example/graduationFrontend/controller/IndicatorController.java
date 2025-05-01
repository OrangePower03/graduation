package com.example.graduationFrontend.controller;

import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.domain.dto.indicator.AddIndicatorDTO;
import com.example.graduationFrontend.domain.dto.indicator.IndicatorDTO;
import com.example.graduationFrontend.domain.dto.indicator.UpdateIndicatorDTO;
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
import java.util.Objects;

@Controller
@RequestMapping("/indicator")
public class IndicatorController extends BaseController {
    @GetMapping
    public String indicatorPage(HttpSession session, Model model) {
        Request request = buildGetRequest("/indicator", DataUtils.getUserToken(session), null);
        ResponseResult<List<IndicatorVO>> result = sendRequestAsList(request, IndicatorVO.class);
        if (result.isSuccess()) {
            model.addAttribute("indicators", result.getData());
            model.addAttribute("addIndicatorDTO", new AddIndicatorDTO());
            return "admin/indicator";
        }
        throw new ErrorException(result);
    }

    @PostMapping("/add")
    public String addIndicator(AddIndicatorDTO indicator, HttpSession session, Model model) {
        IndicatorDTO indicatorDTO = new IndicatorDTO();
        indicatorDTO.setName(indicator.getName());
        indicatorDTO.setUnit(indicator.getUnit());
        StringBuilder sb = new StringBuilder();
        boolean man = rangeValid(indicator.getManStandardRangeL(), indicator.getManStandardRangeR());
        boolean woman = rangeValid(indicator.getWomanStandardRangeL(), indicator.getWomanStandardRangeR());
        if (!man && !woman) {
            throw new ErrorException("请输入正确的标准范围");
        }
        else if (man && woman) {
            sb.append(indicator.getManStandardRangeL())
                    .append(" ~ ")
                    .append(indicator.getManStandardRangeR())
                    .append(";")
                    .append(indicator.getWomanStandardRangeL())
                    .append(" ~ ")
                    .append(indicator.getWomanStandardRangeR());
        }
        else {
            if (man) {
                sb.append(indicator.getManStandardRangeL())
                        .append(" ~ ")
                        .append(indicator.getManStandardRangeR());
            }
            else {
                sb.append(indicator.getWomanStandardRangeL())
                        .append(" ~ ")
                        .append(indicator.getWomanStandardRangeR());
            }
        }
        indicatorDTO.setStandardRange(sb.toString());
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
    public String updateIndicator(UpdateIndicatorDTO indicator, HttpSession session, Model model) {
        IndicatorDTO indicatorDTO = new IndicatorDTO();
        indicatorDTO.setName(indicator.getName());
        indicatorDTO.setUnit(indicator.getUnit());
        StringBuilder sb = new StringBuilder();
        boolean man = rangeValid(indicator.getManRangeL(), indicator.getManRangeR());
        boolean woman = rangeValid(indicator.getWomanRangeL(), indicator.getWomanRangeR());
        boolean all = rangeValid(indicator.getRangeL(), indicator.getRangeR());
        if (man && woman) {
            sb.append(indicator.getManRangeL())
                    .append(" ~ ")
                    .append(indicator.getManRangeR())
                    .append(";")
                    .append(indicator.getWomanRangeL())
                    .append(" ~ ")
                    .append(indicator.getWomanRangeR());
        }
        else if (all) {
            sb.append(indicator.getRangeL())
                    .append(" ~ ")
                    .append(indicator.getRangeR());
        }
        indicatorDTO.setStandardRange(sb.toString());
        Request request = buildRequest("/indicator/" + indicator.getId(), DataUtils.getUserToken(session), null, HttpMethod.PUT, indicatorDTO);
        ResponseResult<IndicatorVO> result = sendRequest(request, IndicatorVO.class);
        if (result.isSuccess()) {
            return indicatorPage(session, model);
        }
        throw new ErrorException(result);
    }


    private boolean rangeValid(Double l, Double r) {
        return Objects.nonNull(l) && Objects.nonNull(r) && l < r;
    }


}
