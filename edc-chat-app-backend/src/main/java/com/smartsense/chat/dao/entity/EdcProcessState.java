package com.smartsense.chat.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartsensesolutions.commons.dao.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "edc_process_states")
public class EdcProcessState implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receiver_bpn")
    private String receiverBpn;

    @Column(name = "offer_id")
    private String offerId;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @JsonIgnore
    private Date updatedAt;

    @PrePersist
    void createdAt() {
        createdAt = new Date();
    }

    @PreUpdate
    void updatedAt() {
        updatedAt = new Date();
    }

}

