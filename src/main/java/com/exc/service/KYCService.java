package com.exc.service;

import com.exc.domain.KYC;
import com.exc.repository.KYCRepository;
import com.exc.service.dto.KYCDTO;
import com.exc.service.mapper.KYCMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing KYC.
 */
@Service
@Transactional
public class KYCService {

    private final Logger log = LoggerFactory.getLogger(KYCService.class);

    private KYCRepository kYCRepository;

    private KYCMapper kYCMapper;

    public KYCService(KYCRepository kYCRepository, KYCMapper kYCMapper) {
        this.kYCRepository = kYCRepository;
        this.kYCMapper = kYCMapper;
    }

    /**
     * Save a kYC.
     *
     * @param kYCDTO the entity to save
     * @return the persisted entity
     */
    public KYCDTO save(KYCDTO kYCDTO) {
        log.debug("Request to save KYC : {}", kYCDTO);

        KYC kYC = kYCMapper.toEntity(kYCDTO);
        kYC = kYCRepository.save(kYC);
        return kYCMapper.toDto(kYC);
    }

    /**
     * Get all the kYCS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<KYCDTO> findAll(Pageable pageable) {
        log.debug("Request to get all KYCS");
        return kYCRepository.findAll(pageable)
            .map(kYCMapper::toDto);
    }


    /**
     * Get one kYC by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<KYCDTO> findOne(Long id) {
        log.debug("Request to get KYC : {}", id);
        return kYCRepository.findById(id)
            .map(kYCMapper::toDto);
    }

    /**
     * Delete the kYC by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete KYC : {}", id);
        kYCRepository.deleteById(id);
    }
}
