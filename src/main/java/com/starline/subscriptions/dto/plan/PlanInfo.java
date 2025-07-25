package com.starline.subscriptions.dto.plan;

import com.starline.subscriptions.model.enums.PlanValidity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PlanInfo {


    private Long id;


    private String name;


    private String description;


    private Integer price;

    private String priceDisplay;


    private PlanValidity validity;

    private String validityDisplay;


    private Boolean enabled;
}
