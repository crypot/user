package com.exc.web.rest;

import com.exc.UserApp;

import com.exc.config.SecurityBeanOverrideConfiguration;

import com.exc.domain.KYC;
import com.exc.repository.KYCRepository;
import com.exc.service.KYCService;
import com.exc.service.dto.KYCDTO;
import com.exc.service.mapper.KYCMapper;
import com.exc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.exc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.exc.domain.enumeration.DocumentType;
/**
 * Test class for the KYCResource REST controller.
 *
 * @see KYCResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, UserApp.class})
public class KYCResourceIntTest {

    private static final String DEFAULT_FILE = "AAAAAAAAAA";
    private static final String UPDATED_FILE = "BBBBBBBBBB";

    private static final DocumentType DEFAULT_TYPE = DocumentType.PASSPORT;
    private static final DocumentType UPDATED_TYPE = DocumentType.DRIVERLICENSE;

    private static final Boolean DEFAULT_IS_VALID = false;
    private static final Boolean UPDATED_IS_VALID = true;

    private static final LocalDate DEFAULT_VALIDATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALIDATION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private KYCRepository kYCRepository;

    @Autowired
    private KYCMapper kYCMapper;
    
    @Autowired
    private KYCService kYCService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restKYCMockMvc;

    private KYC kYC;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final KYCResource kYCResource = new KYCResource(kYCService);
        this.restKYCMockMvc = MockMvcBuilders.standaloneSetup(kYCResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KYC createEntity(EntityManager em) {
        KYC kYC = new KYC()
            .file(DEFAULT_FILE)
            .type(DEFAULT_TYPE)
            .isValid(DEFAULT_IS_VALID)
            .validationDate(DEFAULT_VALIDATION_DATE);
        return kYC;
    }

    @Before
    public void initTest() {
        kYC = createEntity(em);
    }

    @Test
    @Transactional
    public void createKYC() throws Exception {
        int databaseSizeBeforeCreate = kYCRepository.findAll().size();

        // Create the KYC
        KYCDTO kYCDTO = kYCMapper.toDto(kYC);
        restKYCMockMvc.perform(post("/api/kycs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kYCDTO)))
            .andExpect(status().isCreated());

        // Validate the KYC in the database
        List<KYC> kYCList = kYCRepository.findAll();
        assertThat(kYCList).hasSize(databaseSizeBeforeCreate + 1);
        KYC testKYC = kYCList.get(kYCList.size() - 1);
        assertThat(testKYC.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testKYC.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testKYC.isIsValid()).isEqualTo(DEFAULT_IS_VALID);
        assertThat(testKYC.getValidationDate()).isEqualTo(DEFAULT_VALIDATION_DATE);
    }

    @Test
    @Transactional
    public void createKYCWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kYCRepository.findAll().size();

        // Create the KYC with an existing ID
        kYC.setId(1L);
        KYCDTO kYCDTO = kYCMapper.toDto(kYC);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKYCMockMvc.perform(post("/api/kycs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kYCDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KYC in the database
        List<KYC> kYCList = kYCRepository.findAll();
        assertThat(kYCList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllKYCS() throws Exception {
        // Initialize the database
        kYCRepository.saveAndFlush(kYC);

        // Get all the kYCList
        restKYCMockMvc.perform(get("/api/kycs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kYC.getId().intValue())))
            .andExpect(jsonPath("$.[*].file").value(hasItem(DEFAULT_FILE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isValid").value(hasItem(DEFAULT_IS_VALID.booleanValue())))
            .andExpect(jsonPath("$.[*].validationDate").value(hasItem(DEFAULT_VALIDATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getKYC() throws Exception {
        // Initialize the database
        kYCRepository.saveAndFlush(kYC);

        // Get the kYC
        restKYCMockMvc.perform(get("/api/kycs/{id}", kYC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(kYC.getId().intValue()))
            .andExpect(jsonPath("$.file").value(DEFAULT_FILE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.isValid").value(DEFAULT_IS_VALID.booleanValue()))
            .andExpect(jsonPath("$.validationDate").value(DEFAULT_VALIDATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKYC() throws Exception {
        // Get the kYC
        restKYCMockMvc.perform(get("/api/kycs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKYC() throws Exception {
        // Initialize the database
        kYCRepository.saveAndFlush(kYC);

        int databaseSizeBeforeUpdate = kYCRepository.findAll().size();

        // Update the kYC
        KYC updatedKYC = kYCRepository.findById(kYC.getId()).get();
        // Disconnect from session so that the updates on updatedKYC are not directly saved in db
        em.detach(updatedKYC);
        updatedKYC
            .file(UPDATED_FILE)
            .type(UPDATED_TYPE)
            .isValid(UPDATED_IS_VALID)
            .validationDate(UPDATED_VALIDATION_DATE);
        KYCDTO kYCDTO = kYCMapper.toDto(updatedKYC);

        restKYCMockMvc.perform(put("/api/kycs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kYCDTO)))
            .andExpect(status().isOk());

        // Validate the KYC in the database
        List<KYC> kYCList = kYCRepository.findAll();
        assertThat(kYCList).hasSize(databaseSizeBeforeUpdate);
        KYC testKYC = kYCList.get(kYCList.size() - 1);
        assertThat(testKYC.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testKYC.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testKYC.isIsValid()).isEqualTo(UPDATED_IS_VALID);
        assertThat(testKYC.getValidationDate()).isEqualTo(UPDATED_VALIDATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingKYC() throws Exception {
        int databaseSizeBeforeUpdate = kYCRepository.findAll().size();

        // Create the KYC
        KYCDTO kYCDTO = kYCMapper.toDto(kYC);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKYCMockMvc.perform(put("/api/kycs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kYCDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KYC in the database
        List<KYC> kYCList = kYCRepository.findAll();
        assertThat(kYCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKYC() throws Exception {
        // Initialize the database
        kYCRepository.saveAndFlush(kYC);

        int databaseSizeBeforeDelete = kYCRepository.findAll().size();

        // Get the kYC
        restKYCMockMvc.perform(delete("/api/kycs/{id}", kYC.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<KYC> kYCList = kYCRepository.findAll();
        assertThat(kYCList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KYC.class);
        KYC kYC1 = new KYC();
        kYC1.setId(1L);
        KYC kYC2 = new KYC();
        kYC2.setId(kYC1.getId());
        assertThat(kYC1).isEqualTo(kYC2);
        kYC2.setId(2L);
        assertThat(kYC1).isNotEqualTo(kYC2);
        kYC1.setId(null);
        assertThat(kYC1).isNotEqualTo(kYC2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KYCDTO.class);
        KYCDTO kYCDTO1 = new KYCDTO();
        kYCDTO1.setId(1L);
        KYCDTO kYCDTO2 = new KYCDTO();
        assertThat(kYCDTO1).isNotEqualTo(kYCDTO2);
        kYCDTO2.setId(kYCDTO1.getId());
        assertThat(kYCDTO1).isEqualTo(kYCDTO2);
        kYCDTO2.setId(2L);
        assertThat(kYCDTO1).isNotEqualTo(kYCDTO2);
        kYCDTO1.setId(null);
        assertThat(kYCDTO1).isNotEqualTo(kYCDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(kYCMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(kYCMapper.fromId(null)).isNull();
    }
}
