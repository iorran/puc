package com.puc.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Sector.
 */
@Entity
@Table(name = "sector")
public class Sector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "sector")
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "sector")
    private Set<Sensor> sensors = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("sectors")
    private Dam dam;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Sector name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Sector tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Sector addTasks(Task task) {
        this.tasks.add(task);
        task.setSector(this);
        return this;
    }

    public Sector removeTasks(Task task) {
        this.tasks.remove(task);
        task.setSector(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public Sector sensors(Set<Sensor> sensors) {
        this.sensors = sensors;
        return this;
    }

    public Sector addSensors(Sensor sensor) {
        this.sensors.add(sensor);
        sensor.setSector(this);
        return this;
    }

    public Sector removeSensors(Sensor sensor) {
        this.sensors.remove(sensor);
        sensor.setSector(null);
        return this;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Dam getDam() {
        return dam;
    }

    public Sector dam(Dam dam) {
        this.dam = dam;
        return this;
    }

    public void setDam(Dam dam) {
        this.dam = dam;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sector)) {
            return false;
        }
        return id != null && id.equals(((Sector) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Sector{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
