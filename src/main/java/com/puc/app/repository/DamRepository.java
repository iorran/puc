package com.puc.app.repository;

import com.puc.app.domain.Dam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Dam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DamRepository extends JpaRepository<Dam, Long> {

}
