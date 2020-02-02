package com.puc.app.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Dam.
 */
@Entity
@Table(name = "dam")
public class Dam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "adress")
    private String adress;

    @OneToMany(mappedBy = "dam")
    private Set<Sector> sectors = new HashSet<>();

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

    public Dam name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public Dam adress(String adress) {
        this.adress = adress;
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Set<Sector> getSectors() {
        return sectors;
    }

    public Dam sectors(Set<Sector> sectors) {
        this.sectors = sectors;
        return this;
    }

    public Dam addSectors(Sector sector) {
        this.sectors.add(sector);
        sector.setDam(this);
        return this;
    }

    public Dam removeSectors(Sector sector) {
        this.sectors.remove(sector);
        sector.setDam(null);
        return this;
    }

    public void setSectors(Set<Sector> sectors) {
        this.sectors = sectors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dam)) {
            return false;
        }
        return id != null && id.equals(((Dam) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dam{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", adress='" + getAdress() + "'" +
            "}";
    }
}
