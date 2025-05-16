package com.esprit.ftn.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_title")
    private String eventTitle;

    @Column(name = "competition_title")
    private String competitionTitle;

    @Column(name = "dames_url")
    private String damesUrl;

    @Column(name = "messieurs_url")
    private String messieursUrl;

    @Column(name = "mixte_url")
    private String mixteUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getCompetitionTitle() {
        return competitionTitle;
    }

    public void setCompetitionTitle(String competitionTitle) {
        this.competitionTitle = competitionTitle;
    }

    public String getDamesUrl() {
        return damesUrl;
    }

    public void setDamesUrl(String damesUrl) {
        this.damesUrl = damesUrl;
    }

    public String getMessieursUrl() {
        return messieursUrl;
    }

    public void setMessieursUrl(String messieursUrl) {
        this.messieursUrl = messieursUrl;
    }

    public String getMixteUrl() {
        return mixteUrl;
    }

    public void setMixteUrl(String mixteUrl) {
        this.mixteUrl = mixteUrl;
    }
}