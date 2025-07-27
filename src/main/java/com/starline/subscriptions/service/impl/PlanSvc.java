package com.starline.subscriptions.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starline.subscriptions.dto.ApiResponse;
import com.starline.subscriptions.dto.plan.PlanInfo;
import com.starline.subscriptions.model.Plan;
import com.starline.subscriptions.repository.PlanRepository;
import com.starline.subscriptions.service.PlanService;
import com.starline.subscriptions.utils.CurrencyUtil;
import com.starline.subscriptions.utils.SubscriptionsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"plans"})
@RegisterReflectionForBinding({
        PlanInfo.class,
        Plan.class
})
public class PlanSvc implements PlanService {

    private final PlanRepository planRepository;

    private final ObjectMapper mapper;

    @Cacheable
    @Override
    public ApiResponse<List<PlanInfo>> getAvailablePlans() {
        List<Plan> plans = planRepository.findAllByEnabledTrue();
        return ApiResponse.setSuccess(toPlanInfoList(plans));
    }

    private List<PlanInfo> toPlanInfoList(List<Plan> plans) {
        List<PlanInfo> results = new ArrayList<>();
        for (Plan plan : plans) {
            var planInfo = new PlanInfo();
            planInfo.setId(plan.getId())
                    .setName(plan.getName())
                    .setPriceDisplay(CurrencyUtil.formatIDR(plan.getPrice()))
                    .setPrice(plan.getPrice())
                    .setDescription(plan.getDescription())
                    .setValidity(plan.getValidity())
                    .setEnabled(plan.getEnabled())
                    .setValidityDisplay(SubscriptionsUtil.getValidityDisplay(plan.getValidity()));
            results.add(planInfo);
        }
        return results;
    }
}
