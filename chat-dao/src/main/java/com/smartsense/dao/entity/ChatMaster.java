package com.smartsense.dao.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_master")
@Builder
public class ChatMaster extends BaseAuditEntity {

    @Id
    private String id;
    private UUID senderId;
    private UUID receiverId;
    private String message;
    private String messageType;
}
