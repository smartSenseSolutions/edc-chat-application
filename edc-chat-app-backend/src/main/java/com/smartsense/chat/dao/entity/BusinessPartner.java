package com.smartsense.chat.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "raw_data_master")
public class BusinessPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private final UUID id = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    @Column(name = "bpn")
    private String bpn;

    @Column(name = "edc_url")
    private String edcUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @JsonIgnore
    private Date updatedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = new Date();
    }
}
