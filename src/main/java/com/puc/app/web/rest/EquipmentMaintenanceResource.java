package com.puc.app.web.rest;

import com.puc.app.domain.EquipmentMaintenance;
import com.puc.app.repository.EquipmentMaintenanceRepository;
import com.puc.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.puc.app.domain.EquipmentMaintenance}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EquipmentMaintenanceResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentMaintenanceResource.class);

    private static final String ENTITY_NAME = "equipmentMaintenance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipmentMaintenanceRepository equipmentMaintenanceRepository;

    public EquipmentMaintenanceResource(EquipmentMaintenanceRepository equipmentMaintenanceRepository) {
        this.equipmentMaintenanceRepository = equipmentMaintenanceRepository;
    }

    /**
     * {@code POST  /equipment-maintenances} : Create a new equipmentMaintenance.
     *
     * @param equipmentMaintenance the equipmentMaintenance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipmentMaintenance, or with status {@code 400 (Bad Request)} if the equipmentMaintenance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipment-maintenances")
    public ResponseEntity<EquipmentMaintenance> createEquipmentMaintenance(@RequestBody EquipmentMaintenance equipmentMaintenance) throws URISyntaxException {
        log.debug("REST request to save EquipmentMaintenance : {}", equipmentMaintenance);
        if (equipmentMaintenance.getId() != null) {
            throw new BadRequestAlertException("A new equipmentMaintenance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipmentMaintenance result = equipmentMaintenanceRepository.save(equipmentMaintenance);
        return ResponseEntity.created(new URI("/api/equipment-maintenances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipment-maintenances} : Updates an existing equipmentMaintenance.
     *
     * @param equipmentMaintenance the equipmentMaintenance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentMaintenance,
     * or with status {@code 400 (Bad Request)} if the equipmentMaintenance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipmentMaintenance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipment-maintenances")
    public ResponseEntity<EquipmentMaintenance> updateEquipmentMaintenance(@RequestBody EquipmentMaintenance equipmentMaintenance) throws URISyntaxException {
        log.debug("REST request to update EquipmentMaintenance : {}", equipmentMaintenance);
        if (equipmentMaintenance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EquipmentMaintenance result = equipmentMaintenanceRepository.save(equipmentMaintenance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, equipmentMaintenance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /equipment-maintenances} : get all the equipmentMaintenances.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipmentMaintenances in body.
     */
    @GetMapping("/equipment-maintenances")
    public ResponseEntity<List<EquipmentMaintenance>> getAllEquipmentMaintenances(Pageable pageable) {
        log.debug("REST request to get a page of EquipmentMaintenances");
        Page<EquipmentMaintenance> page = equipmentMaintenanceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipment-maintenances/:id} : get the "id" equipmentMaintenance.
     *
     * @param id the id of the equipmentMaintenance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipmentMaintenance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment-maintenances/{id}")
    public ResponseEntity<EquipmentMaintenance> getEquipmentMaintenance(@PathVariable Long id) {
        log.debug("REST request to get EquipmentMaintenance : {}", id);
        Optional<EquipmentMaintenance> equipmentMaintenance = equipmentMaintenanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(equipmentMaintenance);
    }

    /**
     * {@code DELETE  /equipment-maintenances/:id} : delete the "id" equipmentMaintenance.
     *
     * @param id the id of the equipmentMaintenance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipment-maintenances/{id}")
    public ResponseEntity<Void> deleteEquipmentMaintenance(@PathVariable Long id) {
        log.debug("REST request to delete EquipmentMaintenance : {}", id);
        equipmentMaintenanceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
