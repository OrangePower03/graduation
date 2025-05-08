package com.example.backend.controller;

import com.example.backend.domain.dto.indicator.IndicatorDTO;
import com.example.backend.domain.dto.indicator.PatchElderIndicatorDTO;
import com.example.backend.domain.vo.PageVO;
import com.example.backend.domain.vo.indicator.ElderIndicatorDetailVO;
import com.example.backend.domain.vo.indicator.IndicatorVO;
import com.example.backend.domain.vo.indicator.PatchElderIndicatorVO;
import com.example.backend.service.ElderIndicatorService;
import com.example.backend.service.IndicatorService;
import com.example.backend.utils.object.DateUtils;
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
    @PostMapping
    public ResponseResult<IndicatorVO> addIndicator(@RequestBody IndicatorDTO indicatorDTO) {
        return ok(indicatorService.addIndicator(indicatorDTO));
    }

    @PreAuthorize("@MA.isAdmin()")
    @PutMapping("/{id}")
    public ResponseResult<IndicatorVO> updateIndicator(@RequestBody IndicatorDTO indicatorDTO, @PathVariable("id") Long id) {
        return ok(indicatorService.updateIndicator(indicatorDTO, id));
    }

    @PreAuthorize("@MA.isAdmin()")
    @DeleteMapping("/{id}")
    public ResponseResult<IndicatorVO> removeIndicator(@PathVariable("id") Long id) {
        return ok(indicatorService.removeIndicator(id));
    }


    @PreAuthorize("@MA.isAdminOrYoungster()")
    @PostMapping("/elder")
    public ResponseResult<Integer> addElderIndicators(@RequestBody PatchElderIndicatorDTO indicators) {
        return ok(elderIndicatorService.addElderIndicators(indicators));
    }

    @PreAuthorize("@MA.isYoungster()")
    @GetMapping("/elder/{id}")
    public ResponseResult<PatchElderIndicatorVO> getElderIndicators(@PathVariable("id") Long elderId, String startTime, Integer normal) {
        return ok(elderIndicatorService.getElderIndicators(elderId, DateUtils.defaultFormat(startTime), normal));
    }

    @PreAuthorize("@MA.isElder()")
    @GetMapping("/elder")
    public ResponseResult<PatchElderIndicatorVO> getElderIndicators(String startTime, Integer normal) {
        return ok(elderIndicatorService.getElderIndicators(SecurityUtils.getUserId(), DateUtils.defaultFormat(startTime), normal));
    }

    @PreAuthorize("@MA.isYoungster()")
    @GetMapping("/elder/detail/{elderId}")
    public ResponseResult<List<ElderIndicatorDetailVO>> getElderIndicatorDetail(@PathVariable Long elderId, @RequestParam(value = "checkTime") String checkTime) {
        return ok(elderIndicatorService.getElderIndicatorDetail(elderId, DateUtils.defaultFormat(checkTime)));
    }

    @PreAuthorize("@MA.isElder()")
    @GetMapping("/elder/detail")
    public ResponseResult<List<ElderIndicatorDetailVO>> getElderIndicatorDetail(@RequestParam(value = "checkTime") String checkTime) {
        return ok(elderIndicatorService.getElderIndicatorDetail(SecurityUtils.getUserId(), DateUtils.defaultFormat(checkTime)));
    }

    @PreAuthorize("@MA.isYoungster()")
    @DeleteMapping("/elder/{elderId}/{id}")
    public ResponseResult<Void> removeElderIndicator(@PathVariable Long elderId, @PathVariable Long id) {
        elderIndicatorService.removeElderIndicator(elderId, id);
        return ok();
    }
}
