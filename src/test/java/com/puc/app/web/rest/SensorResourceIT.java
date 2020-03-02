package com.puc.app.web.rest;

import com.puc.app.PucApp;
import com.puc.app.domain.Sensor;
import com.puc.app.repository.SensorRepository;
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
 * Integration tests for the {@link SensorResource} REST controller.
 */
@SpringBootTest(classes = PucApp.class)
public class SensorResourceIT {

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final Integer DEFAULT_LATITUDE = 1;
    private static final Integer UPDATED_LATITUDE = 2;

    private static final Integer DEFAULT_LONGITUDE = 1;
    private static final Integer UPDATED_LONGITUDE = 2;

    private static final Double DEFAULT_MAX_HEIGHT = 1D;
    private static final Double UPDATED_MAX_HEIGHT = 2D;

    private static final Double DEFAULT_MAX_VOLUME = 1D;
    private static final Double UPDATED_MAX_VOLUME = 2D;

    private static final Double DEFAULT_MAX_HUMIDITY = 1D;
    private static final Double UPDATED_MAX_HUMIDITY = 2D;

    private static final Double DEFAULT_MAX_PRESSURE = 1D;
    private static final Double UPDATED_MAX_PRESSURE = 2D;

    @Autowired
    private SensorRepository sensorRepository;

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

    private MockMvc restSensorMockMvc;

    private Sensor sensor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SensorResource sensorResource = new SensorResource(sensorRepository);
        this.restSensorMockMvc = MockMvcBuilders.standaloneSetup(sensorResource)
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
    public static Sensor createEntity(EntityManager em) {
        Sensor sensor = new Sensor()
            .tag(DEFAULT_TAG)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .maxHeight(DEFAULT_MAX_HEIGHT)
            .maxVolume(DEFAULT_MAX_VOLUME)
            .maxHumidity(DEFAULT_MAX_HUMIDITY)
            .maxPressure(DEFAULT_MAX_PRESSURE);
        return sensor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createUpdatedEntity(EntityManager em) {
        Sensor sensor = new Sensor()
            .tag(UPDATED_TAG)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .maxHeight(UPDATED_MAX_HEIGHT)
            .maxVolume(UPDATED_MAX_VOLUME)
            .maxHumidity(UPDATED_MAX_HUMIDITY)
            .maxPressure(UPDATED_MAX_PRESSURE);
        return sensor;
    }

    @BeforeEach
    public void initTest() {
        sensor = createEntity(em);
    }

    @Test
    @Transactional
    public void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor
        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testSensor.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testSensor.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testSensor.getMaxHeight()).isEqualTo(DEFAULT_MAX_HEIGHT);
        assertThat(testSensor.getMaxVolume()).isEqualTo(DEFAULT_MAX_VOLUME);
        assertThat(testSensor.getMaxHumidity()).isEqualTo(DEFAULT_MAX_HUMIDITY);
        assertThat(testSensor.getMaxPressure()).isEqualTo(DEFAULT_MAX_PRESSURE);
    }

    @Test
    @Transactional
    public void createSensorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor with an existing ID
        sensor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSensors() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList
        restSensorMockMvc.perform(get("/api/sensors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].maxHeight").value(hasItem(DEFAULT_MAX_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].maxVolume").value(hasItem(DEFAULT_MAX_VOLUME.doubleValue())))
            .andExpect(jsonPath("$.[*].maxHumidity").value(hasItem(DEFAULT_MAX_HUMIDITY.doubleValue())))
            .andExpect(jsonPath("$.[*].maxPressure").value(hasItem(DEFAULT_MAX_PRESSURE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", sensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sensor.getId().intValue()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.maxHeight").value(DEFAULT_MAX_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.maxVolume").value(DEFAULT_MAX_VOLUME.doubleValue()))
            .andExpect(jsonPath("$.maxHumidity").value(DEFAULT_MAX_HUMIDITY.doubleValue()))
            .andExpect(jsonPath("$.maxPressure").value(DEFAULT_MAX_PRESSURE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findById(sensor.getId()).get();
        // Disconnect from session so that the updates on updatedSensor are not directly saved in db
        em.detach(updatedSensor);
        updatedSensor
            .tag(UPDATED_TAG)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .maxHeight(UPDATED_MAX_HEIGHT)
            .maxVolume(UPDATED_MAX_VOLUME)
            .maxHumidity(UPDATED_MAX_HUMIDITY)
            .maxPressure(UPDATED_MAX_PRESSURE);

        restSensorMockMvc.perform(put("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSensor)))
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testSensor.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testSensor.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testSensor.getMaxHeight()).isEqualTo(UPDATED_MAX_HEIGHT);
        assertThat(testSensor.getMaxVolume()).isEqualTo(UPDATED_MAX_VOLUME);
        assertThat(testSensor.getMaxHumidity()).isEqualTo(UPDATED_MAX_HUMIDITY);
        assertThat(testSensor.getMaxPressure()).isEqualTo(UPDATED_MAX_PRESSURE);
    }

    @Test
    @Transactional
    public void updateNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Create the Sensor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorMockMvc.perform(put("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        int databaseSizeBeforeDelete = sensorRepository.findAll().size();

        // Delete the sensor
        restSensorMockMvc.perform(delete("/api/sensors/{id}", sensor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
