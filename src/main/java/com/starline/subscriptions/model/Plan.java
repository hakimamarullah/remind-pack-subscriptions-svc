package com.starline.subscriptions.model;

import com.starline.subscriptions.model.enums.PlanValidity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "PLAN")
@Setter
@Getter
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLANS_SEQ")
    @SequenceGenerator(name = "PLANS_SEQ", sequenceName = "PLANS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", length = 100, nullable = false)
    @Comment(value = "Plan name", on = "NAME")
    private String name;

    @Column(name = "DESCRIPTION", length = 1000)
    @Comment(value = "Plan description", on = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    @Comment(value = "Plan price", on = "PRICE")
    private Integer price;

    @Column(name = "VALIDITY", length = 10)
    @Comment(value = "Plan validity in format WEEKLY/MONTHLY/YEARLY", on = "VALIDITY")
    @Enumerated(EnumType.STRING)
    private PlanValidity validity;

    @Column(name = "SUBSCRIPTION_LIMIT")
    @Comment(value = "Plan Subscription limit", on = "SUBSCRIPTION_LIMIT")
    private Integer subsLimit;

    @Column(name = "ENABLED", nullable = false)
    @Comment(value = "Plan enabled flag", on = "ENABLED")
    private Boolean enabled = true;
}
