package com.puc.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Sensor.
 */
@Entity
@Table(name = "sensor")
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag")
    private String tag;

    @Column(name = "latitude")
    private Integer latitude;

    @Column(name = "longitude")
    private Integer longitude;

    @Column(name = "max_height")
    private Double maxHeight;

    @Column(name = "max_volume")
    private Double maxVolume;

    @Column(name = "max_humidity")
    private Double maxHumidity;

    @Column(name = "max_pressure")
    private Double maxPressure;

    @OneToMany(mappedBy = "sensor")
    private Set<Readings> readings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("sensors")
    private Sector sector;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public Sensor tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public Sensor latitude(Integer latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public Sensor longitude(Integer longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Double getMaxHeight() {
        return maxHeight;
    }

    public Sensor maxHeight(Double maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public void setMaxHeight(Double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Double getMaxVolume() {
        return maxVolume;
    }

    public Sensor maxVolume(Double maxVolume) {
        this.maxVolume = maxVolume;
        return this;
    }

    public void setMaxVolume(Double maxVolume) {
        this.maxVolume = maxVolume;
    }

    public Double getMaxHumidity() {
        return maxHumidity;
    }

    public Sensor maxHumidity(Double maxHumidity) {
        this.maxHumidity = maxHumidity;
        return this;
    }

    public void setMaxHumidity(Double maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public Double getMaxPressure() {
        return maxPressure;
    }

    public Sensor maxPressure(Double maxPressure) {
        this.maxPressure = maxPressure;
        return this;
    }

    public void setMaxPressure(Double maxPressure) {
        this.maxPressure = maxPressure;
    }

    public Set<Readings> getReadings() {
        return readings;
    }

    public Sensor readings(Set<Readings> readings) {
        this.readings = readings;
        return this;
    }

    public Sensor addReadings(Readings readings) {
        this.readings.add(readings);
        readings.setSensor(this);
        return this;
    }

    public Sensor removeReadings(Readings readings) {
        this.readings.remove(readings);
        readings.setSensor(null);
        return this;
    }

    public void setReadings(Set<Readings> readings) {
        this.readings = readings;
    }

    public Sector getSector() {
        return sector;
    }

    public Sensor sector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sensor)) {
            return false;
        }
        return id != null && id.equals(((Sensor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Sensor{" +
            "id=" + getId() +
            ", tag='" + getTag() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", maxHeight=" + getMaxHeight() +
            ", maxVolume=" + getMaxVolume() +
            ", maxHumidity=" + getMaxHumidity() +
            ", maxPressure=" + getMaxPressure() +
            "}";
    }
}
