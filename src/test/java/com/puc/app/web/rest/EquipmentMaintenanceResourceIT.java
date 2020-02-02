package com.puc.app.web.rest;

import com.puc.app.PucApp;
import com.puc.app.domain.EquipmentMaintenance;
import com.puc.app.repository.EquipmentMaintenanceRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.puc.app.web.rest.TestUtil.sameInstant;
import static com.puc.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EquipmentMaintenanceResource} REST controller.
 */
@SpringBootTest(classes = PucApp.class)
public class EquipmentMaintenanceResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private EquipmentMaintenanceRepository equipmentMaintenanceRepository;

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

    private MockMvc restEquipmentMaintenanceMockMvc;

    private EquipmentMaintenance equipmentMaintenance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EquipmentMaintenanceResource equipmentMaintenanceResource = new EquipmentMaintenanceResource(equipmentMaintenanceRepository);
        this.restEquipmentMaintenanceMockMvc = MockMvcBuilders.standaloneSetup(equipmentMaintenanceResource)
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
    public static EquipmentMaintenance createEntity(EntityManager em) {
        EquipmentMaintenance equipmentMaintenance = new EquipmentMaintenance()
            .date(DEFAULT_DATE)
            .notes(DEFAULT_NOTES);
        return equipmentMaintenance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentMaintenance createUpdatedEntity(EntityManager em) {
        EquipmentMaintenance equipmentMaintenance = new EquipmentMaintenance()
            .date(UPDATED_DATE)
            .notes(UPDATED_NOTES);
        return equipmentMaintenance;
    }

    @BeforeEach
    public void initTest() {
        equipmentMaintenance = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentMaintenance() throws Exception {
        int databaseSizeBeforeCreate = equipmentMaintenanceRepository.findAll().size();

        // Create the EquipmentMaintenance
        restEquipmentMaintenanceMockMvc.perform(post("/api/equipment-maintenances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentMaintenance)))
            .andExpect(status().isCreated());

        // Validate the EquipmentMaintenance in the database
        List<EquipmentMaintenance> equipmentMaintenanceList = equipmentMaintenanceRepository.findAll();
        assertThat(equipmentMaintenanceList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentMaintenance testEquipmentMaintenance = equipmentMaintenanceList.get(equipmentMaintenanceList.size() - 1);
        assertThat(testEquipmentMaintenance.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEquipmentMaintenance.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createEquipmentMaintenanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentMaintenanceRepository.findAll().size();

        // Create the EquipmentMaintenance with an existing ID
        equipmentMaintenance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentMaintenanceMockMvc.perform(post("/api/equipment-maintenances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentMaintenance)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentMaintenance in the database
        List<EquipmentMaintenance> equipmentMaintenanceList = equipmentMaintenanceRepository.findAll();
        assertThat(equipmentMaintenanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEquipmentMaintenances() throws Exception {
        // Initialize the database
        equipmentMaintenanceRepository.saveAndFlush(equipmentMaintenance);

        // Get all the equipmentMaintenanceList
        restEquipmentMaintenanceMockMvc.perform(get("/api/equipment-maintenances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentMaintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }
    
    @Test
    @Transactional
    public void getEquipmentMaintenance() throws Exception {
        // Initialize the database
        equipmentMaintenanceRepository.saveAndFlush(equipmentMaintenance);

        // Get the equipmentMaintenance
        restEquipmentMaintenanceMockMvc.perform(get("/api/equipment-maintenances/{id}", equipmentMaintenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentMaintenance.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentMaintenance() throws Exception {
        // Get the equipmentMaintenance
        restEquipmentMaintenanceMockMvc.perform(get("/api/equipment-maintenances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentMaintenance() throws Exception {
        // Initialize the database
        equipmentMaintenanceRepository.saveAndFlush(equipmentMaintenance);

        int databaseSizeBeforeUpdate = equipmentMaintenanceRepository.findAll().size();

        // Update the equipmentMaintenance
        EquipmentMaintenance updatedEquipmentMaintenance = equipmentMaintenanceRepository.findById(equipmentMaintenance.getId()).get();
        // Disconnect from session so that the updates on updatedEquipmentMaintenance are not directly saved in db
        em.detach(updatedEquipmentMaintenance);
        updatedEquipmentMaintenance
            .date(UPDATED_DATE)
            .notes(UPDATED_NOTES);

        restEquipmentMaintenanceMockMvc.perform(put("/api/equipment-maintenances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEquipmentMaintenance)))
            .andExpect(status().isOk());

        // Validate the EquipmentMaintenance in the database
        List<EquipmentMaintenance> equipmentMaintenanceList = equipmentMaintenanceRepository.findAll();
        assertThat(equipmentMaintenanceList).hasSize(databaseSizeBeforeUpdate);
        EquipmentMaintenance testEquipmentMaintenance = equipmentMaintenanceList.get(equipmentMaintenanceList.size() - 1);
        assertThat(testEquipmentMaintenance.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEquipmentMaintenance.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = equipmentMaintenanceRepository.findAll().size();

        // Create the EquipmentMaintenance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentMaintenanceMockMvc.perform(put("/api/equipment-maintenances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentMaintenance)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentMaintenance in the database
        List<EquipmentMaintenance> equipmentMaintenanceList = equipmentMaintenanceRepository.findAll();
        assertThat(equipmentMaintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEquipmentMaintenance() throws Exception {
        // Initialize the database
        equipmentMaintenanceRepository.saveAndFlush(equipmentMaintenance);

        int databaseSizeBeforeDelete = equipmentMaintenanceRepository.findAll().size();

        // Delete the equipmentMaintenance
        restEquipmentMaintenanceMockMvc.perform(delete("/api/equipment-maintenances/{id}", equipmentMaintenance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipmentMaintenance> equipmentMaintenanceList = equipmentMaintenanceRepository.findAll();
        assertThat(equipmentMaintenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
