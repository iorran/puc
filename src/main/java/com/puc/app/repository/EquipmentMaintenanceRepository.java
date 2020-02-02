package com.puc.app.repository;

import com.puc.app.domain.EquipmentMaintenance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EquipmentMaintenance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipmentMaintenanceRepository extends JpaRepository<EquipmentMaintenance, Long> {

}
