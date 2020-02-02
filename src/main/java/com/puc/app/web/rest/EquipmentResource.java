package com.puc.app.web.rest;

import com.puc.app.domain.Equipment;
import com.puc.app.repository.EquipmentRepository;
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
 * REST controller for managing {@link com.puc.app.domain.Equipment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EquipmentResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentResource.class);

    private static final String ENTITY_NAME = "equipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipmentRepository equipmentRepository;

    public EquipmentResource(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * {@code POST  /equipment} : Create a new equipment.
     *
     * @param equipment the equipment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipment, or with status {@code 400 (Bad Request)} if the equipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipment")
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) throws URISyntaxException {
        log.debug("REST request to save Equipment : {}", equipment);
        if (equipment.getId() != null) {
            throw new BadRequestAlertException("A new equipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Equipment result = equipmentRepository.save(equipment);
        return ResponseEntity.created(new URI("/api/equipment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipment} : Updates an existing equipment.
     *
     * @param equipment the equipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipment,
     * or with status {@code 400 (Bad Request)} if the equipment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipment")
    public ResponseEntity<Equipment> updateEquipment(@RequestBody Equipment equipment) throws URISyntaxException {
        log.debug("REST request to update Equipment : {}", equipment);
        if (equipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Equipment result = equipmentRepository.save(equipment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, equipment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /equipment} : get all the equipment.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipment in body.
     */
    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> getAllEquipment(Pageable pageable) {
        log.debug("REST request to get a page of Equipment");
        Page<Equipment> page = equipmentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipment/:id} : get the "id" equipment.
     *
     * @param id the id of the equipment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment/{id}")
    public ResponseEntity<Equipment> getEquipment(@PathVariable Long id) {
        log.debug("REST request to get Equipment : {}", id);
        Optional<Equipment> equipment = equipmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(equipment);
    }

    /**
     * {@code DELETE  /equipment/:id} : delete the "id" equipment.
     *
     * @param id the id of the equipment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        log.debug("REST request to delete Equipment : {}", id);
        equipmentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
