package com.example.backend.controller;

import com.example.backend.domain.dto.indicator.PatchElderIndicatorDTO;
import com.example.backend.domain.vo.indicator.IndicatorVO;
import com.example.backend.service.IndicatorService;
import com.example.backend.utils.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/indicator")
public class IndicatorController extends BaseController {
    @Autowired
    private IndicatorService indicatorService;

    @PreAuthorize("@MA.isAdmin()")
    @GetMapping
    public ResponseResult<List<IndicatorVO>> listIndicator() {
        return ok(indicatorService.listIndicator());
    }

    @PreAuthorize("@MA.isAdmin()")
    @PostMapping("/elder")
    public ResponseResult<Void> addElderIndicators(@RequestBody PatchElderIndicatorDTO indicators) {
        indicatorService.addElderIndicators(indicators);
        return ok();
    }
}
