package com.esprit.ftn.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
    public class FileDetail {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String sourceUrl;
        private String filename;
        private String location;
        private LocalDate date;
        private LocalDate creationdate;
        private Double temperatureAir;
        private Double temperatureWater;

        @Lob
        private String events;  // raw JSON array of events

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Double getTemperatureAir() {
            return temperatureAir;
        }

        public void setTemperatureAir(Double temperatureAir) {
            this.temperatureAir = temperatureAir;
        }

        public Double getTemperatureWater() {
            return temperatureWater;
        }

        public void setTemperatureWater(Double temperatureWater) {
            this.temperatureWater = temperatureWater;
        }

        public String getEvents() {
            return events;
        }

        public void setEvents(String events) {
            this.events = events;
        }

        public LocalDate getCreationdate() {
            return creationdate;
        }

        public void setCreationdate(LocalDate creationdate) {
            this.creationdate = creationdate;
        }
    }
