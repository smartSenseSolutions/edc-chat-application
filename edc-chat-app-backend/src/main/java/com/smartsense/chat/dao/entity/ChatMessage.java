package com.smartsense.chat.dao.entity;

import com.smartsensesolutions.commons.dao.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_messages")
public class ChatMessage implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edc_offer_details_id")
    private EdcOfferDetails edcOfferDetails;

    @Column(name = "partner_bpn")
    private String partnerBpn;

    @Column(name = "message")
    private String message;

    @Column(name = "self_owner")
    private Boolean selfOwner;

    @Column(name = "is_chat_success")
    private boolean chatSuccess;

    @Column(name = "negotiation_id")
    private String negotiationId;

    @Column(name = "agreement_id")
    private String agreementId;

    @Column(name = "transfer_id")
    private String transferId;

    @Column(name = "error_detail")
    private String errorDetail;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @PrePersist
    void createdAt() {
        createdAt = new Date();
    }
}
