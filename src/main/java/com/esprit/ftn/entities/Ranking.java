package com.esprit.ftn.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rankings")
@Data
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Competition info
    @Column(name = "competition_title")
    private String competitionTitle;

    @Column(name = "competition_url")
    private String competitionUrl;

    // Event info
    @Column(name = "event_name")
    private String eventName;

    // Category info
    @Column(name = "category")
    private String category; // "dames" or "messieurs"

    @Column(name = "category_url")
    private String categoryUrl;

    @Column(name = "event_title")
    private String eventTitle; // "Séries"

    // Ranking details
    @Column(name = "place")
    private String place;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "nation")
    private String nation;

    @Column(name = "birth_year")
    private String birthYear;

    @Column(name = "club")
    private String club;

    @Column(name = "time")
    private String time;

    @Column(name = "points")
    private String points;

    @Column(name = "passage_time", length = 512)
    private String passageTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompetitionTitle() {
        return competitionTitle;
    }

    public void setCompetitionTitle(String competitionTitle) {
        this.competitionTitle = competitionTitle;
    }

    public String getCompetitionUrl() {
        return competitionUrl;
    }

    public void setCompetitionUrl(String competitionUrl) {
        this.competitionUrl = competitionUrl;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPassageTime() {
        return passageTime;
    }

    public void setPassageTime(String passageTime) {
        this.passageTime = passageTime;
    }
}