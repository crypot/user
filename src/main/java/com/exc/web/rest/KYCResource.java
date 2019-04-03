package com.exc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.exc.service.KYCService;
import com.exc.web.rest.errors.BadRequestAlertException;
import com.exc.web.rest.util.HeaderUtil;
import com.exc.web.rest.util.PaginationUtil;
import com.exc.service.dto.KYCDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing KYC.
 */
@RestController
@RequestMapping("/api")
public class KYCResource {

    private final Logger log = LoggerFactory.getLogger(KYCResource.class);

    private static final String ENTITY_NAME = "userKyc";

    private KYCService kYCService;

    public KYCResource(KYCService kYCService) {
        this.kYCService = kYCService;
    }

    /**
     * POST  /kycs : Create a new kYC.
     *
     * @param kYCDTO the kYCDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new kYCDTO, or with status 400 (Bad Request) if the kYC has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/kycs")
    @Timed
    public ResponseEntity<KYCDTO> createKYC(@RequestBody KYCDTO kYCDTO) throws URISyntaxException {
        log.debug("REST request to save KYC : {}", kYCDTO);
        if (kYCDTO.getId() != null) {
            throw new BadRequestAlertException("A new kYC cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KYCDTO result = kYCService.save(kYCDTO);
        return ResponseEntity.created(new URI("/api/kycs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /kycs : Updates an existing kYC.
     *
     * @param kYCDTO the kYCDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated kYCDTO,
     * or with status 400 (Bad Request) if the kYCDTO is not valid,
     * or with status 500 (Internal Server Error) if the kYCDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/kycs")
    @Timed
    public ResponseEntity<KYCDTO> updateKYC(@RequestBody KYCDTO kYCDTO) throws URISyntaxException {
        log.debug("REST request to update KYC : {}", kYCDTO);
        if (kYCDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        KYCDTO result = kYCService.save(kYCDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, kYCDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /kycs : get all the kYCS.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of kYCS in body
     */
    @GetMapping("/kycs")
    @Timed
    public ResponseEntity<List<KYCDTO>> getAllKYCS(Pageable pageable) {
        log.debug("REST request to get a page of KYCS");
        Page<KYCDTO> page = kYCService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/kycs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /kycs/:id : get the "id" kYC.
     *
     * @param id the id of the kYCDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the kYCDTO, or with status 404 (Not Found)
     */
    @GetMapping("/kycs/{id}")
    @Timed
    public ResponseEntity<KYCDTO> getKYC(@PathVariable Long id) {
        log.debug("REST request to get KYC : {}", id);
        Optional<KYCDTO> kYCDTO = kYCService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kYCDTO);
    }

    /**
     * DELETE  /kycs/:id : delete the "id" kYC.
     *
     * @param id the id of the kYCDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/kycs/{id}")
    @Timed
    public ResponseEntity<Void> deleteKYC(@PathVariable Long id) {
        log.debug("REST request to delete KYC : {}", id);
        kYCService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
