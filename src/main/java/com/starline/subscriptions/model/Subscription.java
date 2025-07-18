package com.starline.subscriptions.model;

import com.starline.subscriptions.model.enums.SubscriptionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "SUBSCRIPTION")
@Setter
@Getter
@Accessors(chain = true)
public class Subscription extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "EFFECTIVE_DATE")
    @Comment(value = "Effective Date of subscription (set to payment success date)", on = "EFFECTIVE_DATE")
    private LocalDateTime effectiveDate;

    @Column(name = "EXPIRY_DATE")
    @Comment(value = "Expiry Date of subscription (calculated based on plan duration)", on = "EXPIRY_DATE")
    private LocalDateTime expiryDate;

    @Column(name = "STATUS", length = 20, nullable = false)
    @Comment(value = "Subscription Status", on = "STATUS")
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status = SubscriptionStatus.PENDING;


    @Column(name = "USER_ID", nullable = false)
    @Comment(value = "User Id references to users table column ID", on = "USER_ID")
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Plan plan;


}
