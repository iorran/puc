package com.puc.app.web.rest;

import com.puc.app.domain.Dam;
import com.puc.app.repository.DamRepository;
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
 * REST controller for managing {@link com.puc.app.domain.Dam}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DamResource {

    private final Logger log = LoggerFactory.getLogger(DamResource.class);

    private static final String ENTITY_NAME = "dam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DamRepository damRepository;

    public DamResource(DamRepository damRepository) {
        this.damRepository = damRepository;
    }

    /**
     * {@code POST  /dams} : Create a new dam.
     *
     * @param dam the dam to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dam, or with status {@code 400 (Bad Request)} if the dam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dams")
    public ResponseEntity<Dam> createDam(@RequestBody Dam dam) throws URISyntaxException {
        log.debug("REST request to save Dam : {}", dam);
        if (dam.getId() != null) {
            throw new BadRequestAlertException("A new dam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dam result = damRepository.save(dam);
        return ResponseEntity.created(new URI("/api/dams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dams} : Updates an existing dam.
     *
     * @param dam the dam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dam,
     * or with status {@code 400 (Bad Request)} if the dam is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dams")
    public ResponseEntity<Dam> updateDam(@RequestBody Dam dam) throws URISyntaxException {
        log.debug("REST request to update Dam : {}", dam);
        if (dam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Dam result = damRepository.save(dam);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dam.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dams} : get all the dams.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dams in body.
     */
    @GetMapping("/dams")
    public ResponseEntity<List<Dam>> getAllDams(Pageable pageable) {
        log.debug("REST request to get a page of Dams");
        Page<Dam> page = damRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dams/:id} : get the "id" dam.
     *
     * @param id the id of the dam to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dam, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dams/{id}")
    public ResponseEntity<Dam> getDam(@PathVariable Long id) {
        log.debug("REST request to get Dam : {}", id);
        Optional<Dam> dam = damRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dam);
    }

    /**
     * {@code DELETE  /dams/:id} : delete the "id" dam.
     *
     * @param id the id of the dam to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dams/{id}")
    public ResponseEntity<Void> deleteDam(@PathVariable Long id) {
        log.debug("REST request to delete Dam : {}", id);
        damRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
