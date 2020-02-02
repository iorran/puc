package com.puc.app.web.rest;

import com.puc.app.PucApp;
import com.puc.app.domain.Dam;
import com.puc.app.repository.DamRepository;
import com.puc.app.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.puc.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DamResource} REST controller.
 */
@SpringBootTest(classes = PucApp.class)
public class DamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    @Autowired
    private DamRepository damRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDamMockMvc;

    private Dam dam;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DamResource damResource = new DamResource(damRepository);
        this.restDamMockMvc = MockMvcBuilders.standaloneSetup(damResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dam createEntity(EntityManager em) {
        Dam dam = new Dam()
            .name(DEFAULT_NAME)
            .adress(DEFAULT_ADRESS);
        return dam;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dam createUpdatedEntity(EntityManager em) {
        Dam dam = new Dam()
            .name(UPDATED_NAME)
            .adress(UPDATED_ADRESS);
        return dam;
    }

    @BeforeEach
    public void initTest() {
        dam = createEntity(em);
    }

    @Test
    @Transactional
    public void createDam() throws Exception {
        int databaseSizeBeforeCreate = damRepository.findAll().size();

        // Create the Dam
        restDamMockMvc.perform(post("/api/dams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dam)))
            .andExpect(status().isCreated());

        // Validate the Dam in the database
        List<Dam> damList = damRepository.findAll();
        assertThat(damList).hasSize(databaseSizeBeforeCreate + 1);
        Dam testDam = damList.get(damList.size() - 1);
        assertThat(testDam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDam.getAdress()).isEqualTo(DEFAULT_ADRESS);
    }

    @Test
    @Transactional
    public void createDamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = damRepository.findAll().size();

        // Create the Dam with an existing ID
        dam.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDamMockMvc.perform(post("/api/dams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dam)))
            .andExpect(status().isBadRequest());

        // Validate the Dam in the database
        List<Dam> damList = damRepository.findAll();
        assertThat(damList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDams() throws Exception {
        // Initialize the database
        damRepository.saveAndFlush(dam);

        // Get all the damList
        restDamMockMvc.perform(get("/api/dams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dam.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)));
    }
    
    @Test
    @Transactional
    public void getDam() throws Exception {
        // Initialize the database
        damRepository.saveAndFlush(dam);

        // Get the dam
        restDamMockMvc.perform(get("/api/dams/{id}", dam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dam.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS));
    }

    @Test
    @Transactional
    public void getNonExistingDam() throws Exception {
        // Get the dam
        restDamMockMvc.perform(get("/api/dams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDam() throws Exception {
        // Initialize the database
        damRepository.saveAndFlush(dam);

        int databaseSizeBeforeUpdate = damRepository.findAll().size();

        // Update the dam
        Dam updatedDam = damRepository.findById(dam.getId()).get();
        // Disconnect from session so that the updates on updatedDam are not directly saved in db
        em.detach(updatedDam);
        updatedDam
            .name(UPDATED_NAME)
            .adress(UPDATED_ADRESS);

        restDamMockMvc.perform(put("/api/dams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDam)))
            .andExpect(status().isOk());

        // Validate the Dam in the database
        List<Dam> damList = damRepository.findAll();
        assertThat(damList).hasSize(databaseSizeBeforeUpdate);
        Dam testDam = damList.get(damList.size() - 1);
        assertThat(testDam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDam.getAdress()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingDam() throws Exception {
        int databaseSizeBeforeUpdate = damRepository.findAll().size();

        // Create the Dam

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamMockMvc.perform(put("/api/dams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dam)))
            .andExpect(status().isBadRequest());

        // Validate the Dam in the database
        List<Dam> damList = damRepository.findAll();
        assertThat(damList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDam() throws Exception {
        // Initialize the database
        damRepository.saveAndFlush(dam);

        int databaseSizeBeforeDelete = damRepository.findAll().size();

        // Delete the dam
        restDamMockMvc.perform(delete("/api/dams/{id}", dam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dam> damList = damRepository.findAll();
        assertThat(damList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
