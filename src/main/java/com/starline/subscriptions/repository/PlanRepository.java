package com.starline.subscriptions.repository;

import com.starline.subscriptions.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByIdAndEnabledTrue(Long id);

    List<Plan> findAllByEnabledTrue();

    @Query(value = "SELECT p.limit FROM Plan p WHERE p.id = :id")
    Optional<Integer>  getLimitById(Long id);
}
