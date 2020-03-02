package com.puc.app.repository;

import com.puc.app.domain.Readings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Readings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReadingsRepository extends JpaRepository<Readings, Long> {

}
