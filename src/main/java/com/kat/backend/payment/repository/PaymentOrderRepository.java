package com.kat.backend.payment.repository;

import com.kat.backend.payment.entity.PaymentOrder;
import com.kat.backend.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, String> {

    Optional<PaymentOrder> findTopByGuildIdAndUserIdAndPlanAndStatusAndCreatedAtAfter(
            String guildId, String userId, String plan, PaymentStatus status, LocalDateTime since);

    Optional<PaymentOrder> findByMercadoPagoPaymentId(String mercadoPagoPaymentId);
}
