package com.puc.app.web.rest;

import com.puc.app.PucApp;
import com.puc.app.domain.Sector;
import com.puc.app.repository.SectorRepository;
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
 * Integration tests for the {@link SectorResource} REST controller.
 */
@SpringBootTest(classes = PucApp.class)
public class SectorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SectorRepository sectorRepository;

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

    private MockMvc restSectorMockMvc;

    private Sector sector;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SectorResource sectorResource = new SectorResource(sectorRepository);
        this.restSectorMockMvc = MockMvcBuilders.standaloneSetup(sectorResource)
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
    public static Sector createEntity(EntityManager em) {
        Sector sector = new Sector()
            .name(DEFAULT_NAME);
        return sector;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sector createUpdatedEntity(EntityManager em) {
        Sector sector = new Sector()
            .name(UPDATED_NAME);
        return sector;
    }

    @BeforeEach
    public void initTest() {
        sector = createEntity(em);
    }

    @Test
    @Transactional
    public void createSector() throws Exception {
        int databaseSizeBeforeCreate = sectorRepository.findAll().size();

        // Create the Sector
        restSectorMockMvc.perform(post("/api/sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sector)))
            .andExpect(status().isCreated());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeCreate + 1);
        Sector testSector = sectorList.get(sectorList.size() - 1);
        assertThat(testSector.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSectorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sectorRepository.findAll().size();

        // Create the Sector with an existing ID
        sector.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectorMockMvc.perform(post("/api/sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sector)))
            .andExpect(status().isBadRequest());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSectors() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList
        restSectorMockMvc.perform(get("/api/sectors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sector.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSector() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get the sector
        restSectorMockMvc.perform(get("/api/sectors/{id}", sector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sector.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingSector() throws Exception {
        // Get the sector
        restSectorMockMvc.perform(get("/api/sectors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSector() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();

        // Update the sector
        Sector updatedSector = sectorRepository.findById(sector.getId()).get();
        // Disconnect from session so that the updates on updatedSector are not directly saved in db
        em.detach(updatedSector);
        updatedSector
            .name(UPDATED_NAME);

        restSectorMockMvc.perform(put("/api/sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSector)))
            .andExpect(status().isOk());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
        Sector testSector = sectorList.get(sectorList.size() - 1);
        assertThat(testSector.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSector() throws Exception {
        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();

        // Create the Sector

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectorMockMvc.perform(put("/api/sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sector)))
            .andExpect(status().isBadRequest());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSector() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        int databaseSizeBeforeDelete = sectorRepository.findAll().size();

        // Delete the sector
        restSectorMockMvc.perform(delete("/api/sectors/{id}", sector.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
