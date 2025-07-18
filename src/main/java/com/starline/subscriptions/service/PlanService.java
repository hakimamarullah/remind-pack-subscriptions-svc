package com.starline.subscriptions.service;

import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.plan.PlanInfo;

import java.util.List;

public interface PlanService {

    ApiResponse<List<PlanInfo>> getAvailablePlans();
}
