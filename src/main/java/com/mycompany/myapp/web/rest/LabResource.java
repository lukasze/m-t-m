package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Lab;
import com.mycompany.myapp.repository.LabRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Lab}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LabResource {

    private final Logger log = LoggerFactory.getLogger(LabResource.class);

    private static final String ENTITY_NAME = "lab";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LabRepository labRepository;

    public LabResource(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    /**
     * {@code POST  /labs} : Create a new lab.
     *
     * @param lab the lab to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lab, or with status {@code 400 (Bad Request)} if the lab has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/labs")
    public ResponseEntity<Lab> createLab(@RequestBody Lab lab) throws URISyntaxException {
        log.debug("REST request to save Lab : {}", lab);
        if (lab.getId() != null) {
            throw new BadRequestAlertException("A new lab cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lab result = labRepository.save(lab);
        return ResponseEntity
            .created(new URI("/api/labs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /labs/:id} : Updates an existing lab.
     *
     * @param id the id of the lab to save.
     * @param lab the lab to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lab,
     * or with status {@code 400 (Bad Request)} if the lab is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lab couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/labs/{id}")
    public ResponseEntity<Lab> updateLab(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lab lab)
        throws URISyntaxException {
        log.debug("REST request to update Lab : {}, {}", id, lab);
        if (lab.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lab.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!labRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Lab result = labRepository.save(lab);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lab.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /labs/:id} : Partial updates given fields of an existing lab, field will ignore if it is null
     *
     * @param id the id of the lab to save.
     * @param lab the lab to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lab,
     * or with status {@code 400 (Bad Request)} if the lab is not valid,
     * or with status {@code 404 (Not Found)} if the lab is not found,
     * or with status {@code 500 (Internal Server Error)} if the lab couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/labs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Lab> partialUpdateLab(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lab lab)
        throws URISyntaxException {
        log.debug("REST request to partial update Lab partially : {}, {}", id, lab);
        if (lab.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lab.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!labRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lab> result = labRepository
            .findById(lab.getId())
            .map(existingLab -> {
                if (lab.getJobTitle() != null) {
                    existingLab.setJobTitle(lab.getJobTitle());
                }
                if (lab.getMinSalary() != null) {
                    existingLab.setMinSalary(lab.getMinSalary());
                }
                if (lab.getMaxSalary() != null) {
                    existingLab.setMaxSalary(lab.getMaxSalary());
                }

                return existingLab;
            })
            .map(labRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lab.getId().toString())
        );
    }

    /**
     * {@code GET  /labs} : get all the labs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of labs in body.
     */
    @GetMapping("/labs")
    public List<Lab> getAllLabs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Labs");
        return labRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /labs/:id} : get the "id" lab.
     *
     * @param id the id of the lab to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lab, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/labs/{id}")
    public ResponseEntity<Lab> getLab(@PathVariable Long id) {
        log.debug("REST request to get Lab : {}", id);
        Optional<Lab> lab = labRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(lab);
    }

    /**
     * {@code DELETE  /labs/:id} : delete the "id" lab.
     *
     * @param id the id of the lab to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/labs/{id}")
    public ResponseEntity<Void> deleteLab(@PathVariable Long id) {
        log.debug("REST request to delete Lab : {}", id);
        labRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
