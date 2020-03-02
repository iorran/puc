package com.puc.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Readings.
 */
@Entity
@Table(name = "readings")
public class Readings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "read_by")
    private String readBy;

    @Column(name = "height")
    private Double height;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "pressure")
    private Double pressure;

    @ManyToOne
    @JsonIgnoreProperties("readings")
    private Sensor sensor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReadBy() {
        return readBy;
    }

    public Readings readBy(String readBy) {
        this.readBy = readBy;
        return this;
    }

    public void setReadBy(String readBy) {
        this.readBy = readBy;
    }

    public Double getHeight() {
        return height;
    }

    public Readings height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getVolume() {
        return volume;
    }

    public Readings volume(Double volume) {
        this.volume = volume;
        return this;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Readings humidity(Double humidity) {
        this.humidity = humidity;
        return this;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public Readings pressure(Double pressure) {
        this.pressure = pressure;
        return this;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Readings sensor(Sensor sensor) {
        this.sensor = sensor;
        return this;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Readings)) {
            return false;
        }
        return id != null && id.equals(((Readings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Readings{" +
            "id=" + getId() +
            ", readBy='" + getReadBy() + "'" +
            ", height=" + getHeight() +
            ", volume=" + getVolume() +
            ", humidity=" + getHumidity() +
            ", pressure=" + getPressure() +
            "}";
    }
}
