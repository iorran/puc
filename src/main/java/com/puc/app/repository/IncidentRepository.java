package com.puc.app.repository;

import com.puc.app.domain.Incident;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Incident entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

}
