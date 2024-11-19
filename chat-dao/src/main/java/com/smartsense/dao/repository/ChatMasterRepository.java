package com.smartsense.dao.repository;

import com.smartsense.dao.entity.ChatMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMasterRepository extends MongoRepository<ChatMaster, String> {

}
