package com.smartsense.dao.repository;


import com.smartsense.dao.entity.EDCRegisterMaster;
import com.smartsensesolutions.commons.dao.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EDCRegisterMasterRepository extends BaseRepository<EDCRegisterMaster, UUID> {

    boolean existsByEdcUrl(String edcUrl);

    @Query("select e from EDCRegisterMaster e where e.userId.id = ?1")
    List<EDCRegisterMaster> findByUserId(UUID id);

}
