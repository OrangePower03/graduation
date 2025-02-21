package com.example.backend.controller;

import com.example.backend.domain.dto.indicator.PatchElderIndicatorDTO;
import com.example.backend.domain.vo.PageVO;
import com.example.backend.domain.vo.indicator.ElderIndicatorDetailVO;
import com.example.backend.domain.vo.indicator.IndicatorVO;
import com.example.backend.domain.vo.indicator.PatchElderIndicatorVO;
import com.example.backend.service.ElderIndicatorService;
import com.example.backend.service.IndicatorService;
import com.example.backend.utils.security.SecurityUtils;
import com.example.backend.utils.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/indicator")
public class IndicatorController extends BaseController {
    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private ElderIndicatorService elderIndicatorService;

    @PreAuthorize("@MA.isAdmin()")
    @GetMapping
    public ResponseResult<List<IndicatorVO>> listIndicator() {
        return ok(indicatorService.listIndicator());
    }

    @PreAuthorize("@MA.isAdmin()")
    @PostMapping("/elder")
    public ResponseResult<Integer> addElderIndicators(@RequestBody PatchElderIndicatorDTO indicators) {
        return ok(elderIndicatorService.addElderIndicators(indicators));
    }

    @PreAuthorize("@MA.isYoungster()")
    @GetMapping("/elder/{id}")
    public ResponseResult<PatchElderIndicatorVO> getElderIndicators(@PathVariable("id") Long elderId, Date startTime) {
        return ok(elderIndicatorService.getElderIndicators(elderId, startTime));
    }

    @PreAuthorize("@MA.isElder()")
    @GetMapping("/elder")
    public ResponseResult<PatchElderIndicatorVO> getElderIndicators(Date startTime) {
        return ok(elderIndicatorService.getElderIndicators(SecurityUtils.getUserId(), startTime));
    }

    @PreAuthorize("@MA.isYoungster()")
    @GetMapping("/elder/detail/{elderId}")
    public ResponseResult<List<ElderIndicatorDetailVO>> getElderIndicatorDetail(@PathVariable Long elderId, @RequestParam(value = "checkTime") Date checkTime) {
        return ok(elderIndicatorService.getElderIndicatorDetail(elderId, checkTime));
    }
}
