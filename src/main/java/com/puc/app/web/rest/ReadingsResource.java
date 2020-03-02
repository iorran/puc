package com.puc.app.web.rest;

import com.puc.app.domain.Readings;
import com.puc.app.repository.ReadingsRepository;
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
 * REST controller for managing {@link com.puc.app.domain.Readings}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReadingsResource {

    private final Logger log = LoggerFactory.getLogger(ReadingsResource.class);

    private static final String ENTITY_NAME = "readings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReadingsRepository readingsRepository;

    public ReadingsResource(ReadingsRepository readingsRepository) {
        this.readingsRepository = readingsRepository;
    }

    /**
     * {@code POST  /readings} : Create a new readings.
     *
     * @param readings the readings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new readings, or with status {@code 400 (Bad Request)} if the readings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/readings")
    public ResponseEntity<Readings> createReadings(@RequestBody Readings readings) throws URISyntaxException {
        log.debug("REST request to save Readings : {}", readings);
        if (readings.getId() != null) {
            throw new BadRequestAlertException("A new readings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Readings result = readingsRepository.save(readings);
        return ResponseEntity.created(new URI("/api/readings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /readings} : Updates an existing readings.
     *
     * @param readings the readings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated readings,
     * or with status {@code 400 (Bad Request)} if the readings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the readings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/readings")
    public ResponseEntity<Readings> updateReadings(@RequestBody Readings readings) throws URISyntaxException {
        log.debug("REST request to update Readings : {}", readings);
        if (readings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Readings result = readingsRepository.save(readings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, readings.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /readings} : get all the readings.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of readings in body.
     */
    @GetMapping("/readings")
    public ResponseEntity<List<Readings>> getAllReadings(Pageable pageable) {
        log.debug("REST request to get a page of Readings");
        Page<Readings> page = readingsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /readings/:id} : get the "id" readings.
     *
     * @param id the id of the readings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the readings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/readings/{id}")
    public ResponseEntity<Readings> getReadings(@PathVariable Long id) {
        log.debug("REST request to get Readings : {}", id);
        Optional<Readings> readings = readingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(readings);
    }

    /**
     * {@code DELETE  /readings/:id} : delete the "id" readings.
     *
     * @param id the id of the readings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/readings/{id}")
    public ResponseEntity<Void> deleteReadings(@PathVariable Long id) {
        log.debug("REST request to delete Readings : {}", id);
        readingsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
