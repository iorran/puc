package com.puc.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.puc.app.domain.enumeration.RiskType;

/**
 * A Incident.
 */
@Entity
@Table(name = "incident")
public class Incident implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk")
    private RiskType risk;

    @Lob
    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "incident")
    private Set<Action> actions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("incidents")
    private Task task;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Incident title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RiskType getRisk() {
        return risk;
    }

    public Incident risk(RiskType risk) {
        this.risk = risk;
        return this;
    }

    public void setRisk(RiskType risk) {
        this.risk = risk;
    }

    public String getNotes() {
        return notes;
    }

    public Incident notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public Incident actions(Set<Action> actions) {
        this.actions = actions;
        return this;
    }

    public Incident addActions(Action action) {
        this.actions.add(action);
        action.setIncident(this);
        return this;
    }

    public Incident removeActions(Action action) {
        this.actions.remove(action);
        action.setIncident(null);
        return this;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }

    public Task getTask() {
        return task;
    }

    public Incident task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Incident)) {
            return false;
        }
        return id != null && id.equals(((Incident) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Incident{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", risk='" + getRisk() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
