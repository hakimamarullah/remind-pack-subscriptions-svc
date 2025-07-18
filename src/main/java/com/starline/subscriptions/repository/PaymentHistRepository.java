package com.starline.subscriptions.repository;

import com.starline.subscriptions.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentHistRepository extends JpaRepository<PaymentHistory, Long> {


    Optional<PaymentHistory> findByReferenceId(String referenceId);

    @Query("SELECT p.paymentUrl FROM PaymentHistory p WHERE p.referenceId = :referenceId")
    String getPaymentUrlByReferenceId(String referenceId);
}
