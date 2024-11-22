package com.smartsense.chat.dao.entity;

import com.smartsensesolutions.commons.dao.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "edc_process_state_id")
    private EdcProcessState edcProcessState;

    @Column(name = "partner_bpn")
    private String partnerBpn;

    @Column(name = "message")
    private String message;

    @Column(name = "self_owner")
    private Boolean selfOwner;

    @Column(name = "is_chat_success")
    private boolean chatSuccess;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @PrePersist
    void createdAt() {
        createdAt = new Date();
    }
}