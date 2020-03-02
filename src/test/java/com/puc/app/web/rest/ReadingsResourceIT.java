package com.puc.app.web.rest;

import com.puc.app.PucApp;
import com.puc.app.domain.Readings;
import com.puc.app.repository.ReadingsRepository;
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
 * Integration tests for the {@link ReadingsResource} REST controller.
 */
@SpringBootTest(classes = PucApp.class)
public class ReadingsResourceIT {

    private static final String DEFAULT_READ_BY = "AAAAAAAAAA";
    private static final String UPDATED_READ_BY = "BBBBBBBBBB";

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;

    private static final Double DEFAULT_VOLUME = 1D;
    private static final Double UPDATED_VOLUME = 2D;

    private static final Double DEFAULT_HUMIDITY = 1D;
    private static final Double UPDATED_HUMIDITY = 2D;

    private static final Double DEFAULT_PRESSURE = 1D;
    private static final Double UPDATED_PRESSURE = 2D;

    @Autowired
    private ReadingsRepository readingsRepository;

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

    private MockMvc restReadingsMockMvc;

    private Readings readings;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReadingsResource readingsResource = new ReadingsResource(readingsRepository);
        this.restReadingsMockMvc = MockMvcBuilders.standaloneSetup(readingsResource)
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
    public static Readings createEntity(EntityManager em) {
        Readings readings = new Readings()
            .readBy(DEFAULT_READ_BY)
            .height(DEFAULT_HEIGHT)
            .volume(DEFAULT_VOLUME)
            .humidity(DEFAULT_HUMIDITY)
            .pressure(DEFAULT_PRESSURE);
        return readings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Readings createUpdatedEntity(EntityManager em) {
        Readings readings = new Readings()
            .readBy(UPDATED_READ_BY)
            .height(UPDATED_HEIGHT)
            .volume(UPDATED_VOLUME)
            .humidity(UPDATED_HUMIDITY)
            .pressure(UPDATED_PRESSURE);
        return readings;
    }

    @BeforeEach
    public void initTest() {
        readings = createEntity(em);
    }

    @Test
    @Transactional
    public void createReadings() throws Exception {
        int databaseSizeBeforeCreate = readingsRepository.findAll().size();

        // Create the Readings
        restReadingsMockMvc.perform(post("/api/readings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readings)))
            .andExpect(status().isCreated());

        // Validate the Readings in the database
        List<Readings> readingsList = readingsRepository.findAll();
        assertThat(readingsList).hasSize(databaseSizeBeforeCreate + 1);
        Readings testReadings = readingsList.get(readingsList.size() - 1);
        assertThat(testReadings.getReadBy()).isEqualTo(DEFAULT_READ_BY);
        assertThat(testReadings.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testReadings.getVolume()).isEqualTo(DEFAULT_VOLUME);
        assertThat(testReadings.getHumidity()).isEqualTo(DEFAULT_HUMIDITY);
        assertThat(testReadings.getPressure()).isEqualTo(DEFAULT_PRESSURE);
    }

    @Test
    @Transactional
    public void createReadingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = readingsRepository.findAll().size();

        // Create the Readings with an existing ID
        readings.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReadingsMockMvc.perform(post("/api/readings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readings)))
            .andExpect(status().isBadRequest());

        // Validate the Readings in the database
        List<Readings> readingsList = readingsRepository.findAll();
        assertThat(readingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllReadings() throws Exception {
        // Initialize the database
        readingsRepository.saveAndFlush(readings);

        // Get all the readingsList
        restReadingsMockMvc.perform(get("/api/readings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(readings.getId().intValue())))
            .andExpect(jsonPath("$.[*].readBy").value(hasItem(DEFAULT_READ_BY)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.doubleValue())))
            .andExpect(jsonPath("$.[*].humidity").value(hasItem(DEFAULT_HUMIDITY.doubleValue())))
            .andExpect(jsonPath("$.[*].pressure").value(hasItem(DEFAULT_PRESSURE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getReadings() throws Exception {
        // Initialize the database
        readingsRepository.saveAndFlush(readings);

        // Get the readings
        restReadingsMockMvc.perform(get("/api/readings/{id}", readings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(readings.getId().intValue()))
            .andExpect(jsonPath("$.readBy").value(DEFAULT_READ_BY))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME.doubleValue()))
            .andExpect(jsonPath("$.humidity").value(DEFAULT_HUMIDITY.doubleValue()))
            .andExpect(jsonPath("$.pressure").value(DEFAULT_PRESSURE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReadings() throws Exception {
        // Get the readings
        restReadingsMockMvc.perform(get("/api/readings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReadings() throws Exception {
        // Initialize the database
        readingsRepository.saveAndFlush(readings);

        int databaseSizeBeforeUpdate = readingsRepository.findAll().size();

        // Update the readings
        Readings updatedReadings = readingsRepository.findById(readings.getId()).get();
        // Disconnect from session so that the updates on updatedReadings are not directly saved in db
        em.detach(updatedReadings);
        updatedReadings
            .readBy(UPDATED_READ_BY)
            .height(UPDATED_HEIGHT)
            .volume(UPDATED_VOLUME)
            .humidity(UPDATED_HUMIDITY)
            .pressure(UPDATED_PRESSURE);

        restReadingsMockMvc.perform(put("/api/readings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReadings)))
            .andExpect(status().isOk());

        // Validate the Readings in the database
        List<Readings> readingsList = readingsRepository.findAll();
        assertThat(readingsList).hasSize(databaseSizeBeforeUpdate);
        Readings testReadings = readingsList.get(readingsList.size() - 1);
        assertThat(testReadings.getReadBy()).isEqualTo(UPDATED_READ_BY);
        assertThat(testReadings.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testReadings.getVolume()).isEqualTo(UPDATED_VOLUME);
        assertThat(testReadings.getHumidity()).isEqualTo(UPDATED_HUMIDITY);
        assertThat(testReadings.getPressure()).isEqualTo(UPDATED_PRESSURE);
    }

    @Test
    @Transactional
    public void updateNonExistingReadings() throws Exception {
        int databaseSizeBeforeUpdate = readingsRepository.findAll().size();

        // Create the Readings

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReadingsMockMvc.perform(put("/api/readings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readings)))
            .andExpect(status().isBadRequest());

        // Validate the Readings in the database
        List<Readings> readingsList = readingsRepository.findAll();
        assertThat(readingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReadings() throws Exception {
        // Initialize the database
        readingsRepository.saveAndFlush(readings);

        int databaseSizeBeforeDelete = readingsRepository.findAll().size();

        // Delete the readings
        restReadingsMockMvc.perform(delete("/api/readings/{id}", readings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Readings> readingsList = readingsRepository.findAll();
        assertThat(readingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
