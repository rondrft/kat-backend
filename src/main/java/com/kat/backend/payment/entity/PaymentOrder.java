package com.kat.backend.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_orders", indexes = {
        @Index(name = "idx_payment_orders_user", columnList = "userId"),
        @Index(name = "idx_payment_orders_guild", columnList = "guildId"),
        @Index(name = "idx_payment_orders_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOrder {

    @Id
    private String id;

    @Column(nullable = false)
    private String guildId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String plan;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String mercadoPagoPreferenceId;
    private String mercadoPagoPaymentId;

    @Column(columnDefinition = "TEXT")
    private String checkoutUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
