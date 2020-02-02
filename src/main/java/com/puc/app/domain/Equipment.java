package com.puc.app.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Equipment.
 */
@Entity
@Table(name = "equipment")
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "equipment")
    private Set<EquipmentMaintenance> maintenances = new HashSet<>();

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

    public Equipment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EquipmentMaintenance> getMaintenances() {
        return maintenances;
    }

    public Equipment maintenances(Set<EquipmentMaintenance> equipmentMaintenances) {
        this.maintenances = equipmentMaintenances;
        return this;
    }

    public Equipment addMaintenances(EquipmentMaintenance equipmentMaintenance) {
        this.maintenances.add(equipmentMaintenance);
        equipmentMaintenance.setEquipment(this);
        return this;
    }

    public Equipment removeMaintenances(EquipmentMaintenance equipmentMaintenance) {
        this.maintenances.remove(equipmentMaintenance);
        equipmentMaintenance.setEquipment(null);
        return this;
    }

    public void setMaintenances(Set<EquipmentMaintenance> equipmentMaintenances) {
        this.maintenances = equipmentMaintenances;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equipment)) {
            return false;
        }
        return id != null && id.equals(((Equipment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Equipment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
