package com.starline.subscriptions.controllers;


import com.starline.subscriptions.annotations.LogRequestResponse;
import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.plan.PlanInfo;
import com.starline.subscriptions.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plans")
@LogRequestResponse
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<PlanInfo>>> getAvailablePlans() {
        return planService.getAvailablePlans().toResponseEntity();
    }
}
