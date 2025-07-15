package com.esprit.ftn.services;

import com.esprit.ftn.entities.*;
import com.esprit.ftn.Repositories.*;
import com.fasterxml.jackson.databind.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.*;
import java.util.*;
import java.util.regex.*;

@Service
public class DataImportService {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private ClubRepository ClubRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void importCompetitionsFromJson(String jsonContent) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonContent);
        Iterator<String> seasons = rootNode.fieldNames();

        while (seasons.hasNext()) {
            String seasonLabel = seasons.next();
            JsonNode competitionsNode = rootNode.get(seasonLabel);

            // Extract season year range from the season label
            Pattern seasonYearPattern = Pattern.compile("(20\\d{2})\\s*[\\-–—]\\s*(20\\d{2})");
            Matcher seasonYearMatcher = seasonYearPattern.matcher(seasonLabel);
            String seasonYearRange = null;

            if (seasonYearMatcher.find()) {
                seasonYearRange = seasonYearMatcher.group(1) + "-" + seasonYearMatcher.group(2);
            }

            for (JsonNode compNode : competitionsNode) {
                Competition competition = new Competition();
                competition.setTitle(compNode.get("title").asText());
                competition.setUrl(compNode.get("url").asText());

                // Set season from seasonLabel if available
                if (seasonYearRange != null) {
                    competition.setSeason("Saison " + seasonYearRange);
                }

                // Extract dates from title and season info
                extractDatesFromTitle(competition);

                competitionRepository.save(competition);
            }
        }
    }

    @Transactional
    public void importEventsFromJson(String jsonContent) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonContent);
        List<Event> eventsToSave = new ArrayList<>();

        // Process each competition in the array
        for (JsonNode competitionNode : rootNode) {
            String competitionTitle = competitionNode.get("competition_title").asText();

            JsonNode eventsNode = competitionNode.get("events");

            // Process each event
            for (JsonNode eventNode : eventsNode) {
                Event event = new Event();
                event.setEventTitle(eventNode.get("event").asText());
                event.setCompetitionTitle(competitionTitle);

                // Set category URLs
                event.setDamesUrl(eventNode.get("dames").asText());
                event.setMessieursUrl(eventNode.get("messieurs").asText());
                event.setMixteUrl(eventNode.get("mixte").asText());

                eventsToSave.add(event);
            }
        }

        // Batch save all events
        eventRepository.saveAll(eventsToSave);
    }

    @Transactional
    public void importRankingsFromJson(String jsonContent) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonContent);
        List<Ranking> rankingsToSave = new ArrayList<>();

        // Process each competition in the array
        for (JsonNode competitionNode : rootNode) {
            String competitionTitle = competitionNode.get("competition_title").asText();
            String competitionUrl = competitionNode.get("competition_url").asText();

            JsonNode eventsRankingsNode = competitionNode.get("events_rankings");

            // Process each event
            for (JsonNode eventNode : eventsRankingsNode) {
                String eventName = eventNode.get("event_name").asText();

                JsonNode categoriesNode = eventNode.get("categories");

                // Process each category
                for (JsonNode categoryNode : categoriesNode) {
                    String category = categoryNode.get("category").asText();
                    String categoryUrl = categoryNode.get("url").asText();
                    String eventTitle = categoryNode.get("event_title").asText();

                    JsonNode rankingNode = categoryNode.get("ranking");

                    // Process each ranking entry
                    for (JsonNode rankEntry : rankingNode) {
                        Ranking ranking = new Ranking();

                        // Set competition info
                        ranking.setCompetitionTitle(competitionTitle);
                        ranking.setCompetitionUrl(competitionUrl);

                        // Set event info
                        ranking.setEventName(eventName);

                        // Set category info
                        ranking.setCategory(category);
                        ranking.setCategoryUrl(categoryUrl);
                        ranking.setEventTitle(eventTitle);

                        // Set ranking details
                        ranking.setPlace(rankEntry.get("Place").asText());
                        ranking.setFullName(rankEntry.get("Nom et prénom").asText());
                        ranking.setNation(rankEntry.get("Nation").asText());
                        ranking.setBirthYear(rankEntry.get("Naissance").asText());
                        ranking.setClub(rankEntry.get("Club").asText());
                        ranking.setTime(rankEntry.get("Temps").asText());
                        ranking.setPoints(rankEntry.get("Points").asText());

                        // Handle optional passage time (might be empty string)
                        JsonNode passageTimeNode = rankEntry.get("Temps de passage");
                        if (passageTimeNode != null && !passageTimeNode.asText().isEmpty()) {
                            ranking.setPassageTime(passageTimeNode.asText());
                        } else {
                            ranking.setPassageTime("");
                        }

                        rankingsToSave.add(ranking);
                    }
                }
            }
        }

        // Batch save all rankings
        rankingRepository.saveAll(rankingsToSave);
    }

    @Transactional
    public void importClubsFromJson(String jsonContent) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonContent);
        JsonNode clubsNode = rootNode.get("clubs");
        String sourceUrl = rootNode.has("url") ? rootNode.get("url").asText() : null;
        // Default discipline if not provided in JSON
        String discipline = rootNode.has("discipline") ? rootNode.get("discipline").asText() : "unknown";

        if (clubsNode != null && clubsNode.isArray()) {
            for (JsonNode clubNode : clubsNode) {
                String originalName = clubNode.get("club_name").asText();
                String link = clubNode.has("club_link") ? clubNode.get("club_link").asText() : null;

                // Extract the club name without the number prefix (e.g., "1/ACADEMIE DE NATATION" -> "ACADEMIE DE NATATION")
                String name = originalName;
                if (name.matches("\\d+/.*")) {
                    name = name.substring(name.indexOf('/') + 1).trim();
                }

                // Check if club already exists
                Optional<Club> existingClub = ClubRepository.findByName(name);

                if (existingClub.isPresent()) {
                    Club club = existingClub.get();
                    // Update existing club if necessary
                    if (link != null && club.getLink() == null) {
                        club.setLink(link);
                    }
                    if (sourceUrl != null && club.getSourceUrl() == null) {
                        club.setSourceUrl(sourceUrl);
                    }
                    ClubRepository.save(club);
                } else {
                    // Create new club
                    Club club = new Club();
                    club.setName(name);
                    club.setOriginalName(originalName);
                    club.setLink(link);
                    club.setSourceUrl(sourceUrl);
                    club.setDiscipline(discipline);
                    ClubRepository.save(club);
                }
            }
        }
    }

    private void extractDatesFromTitle(Competition competition) {
        String title = competition.getTitle();

        // First try to extract dates from the title itself
        Pattern datePattern = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4})\\s*[¤\\-–—]?\s*(\\d{1,2}/\\d{1,2}/\\d{4})?");
        Matcher dateMatcher = datePattern.matcher(title);

        if (dateMatcher.find()) {
            String startDateStr = dateMatcher.group(1);
            String endDateStr = dateMatcher.group(2);

            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("d/M/yyyy")
                    .toFormatter(Locale.FRENCH);

            try {
                competition.setDateStart(LocalDate.parse(startDateStr, formatter));

                if (endDateStr != null && !endDateStr.isEmpty()) {
                    competition.setDateEnd(LocalDate.parse(endDateStr, formatter));
                } else {
                    competition.setDateEnd(competition.getDateStart());
                }
                return; // If we found dates in title, we're done
            } catch (DateTimeParseException e) {
                // Try alternative date formats if standard format fails
                tryAlternativeDateFormats(competition, startDateStr, endDateStr);
                return;
            }
        }

        // If no explicit dates in title, extract from season info
        extractSeasonInfo(competition);
    }

    private void tryAlternativeDateFormats(Competition competition, String startDateStr, String endDateStr) {
        // Try different date formats (dd-MM-yyyy, yyyy-MM-dd, etc.)
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("d-M-yyyy", Locale.FRENCH),
                DateTimeFormatter.ofPattern("yyyy-M-d", Locale.FRENCH),
                DateTimeFormatter.ofPattern("d.M.yyyy", Locale.FRENCH)
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                competition.setDateStart(LocalDate.parse(startDateStr, formatter));

                if (endDateStr != null && !endDateStr.isEmpty()) {
                    competition.setDateEnd(LocalDate.parse(endDateStr, formatter));
                } else {
                    competition.setDateEnd(competition.getDateStart());
                }

                // If we successfully parsed the date, break out of the loop
                break;
            } catch (DateTimeParseException e) {
                // Continue to the next formatter
            }
        }
    }

    private void extractSeasonInfo(Competition competition) {
        // First check if season is already set (from parent key during import)
        if (competition.getSeason() != null && competition.getSeason().startsWith("Saison ")) {
            // Extract years from season string like "Saison 2023-2024"
            Pattern seasonPattern = Pattern.compile("Saison (20\\d{2})-(20\\d{2})");
            Matcher seasonMatcher = seasonPattern.matcher(competition.getSeason());

            if (seasonMatcher.find()) {
                int startYear = Integer.parseInt(seasonMatcher.group(1));
                int endYear = Integer.parseInt(seasonMatcher.group(2));

                // Set date_start to first year and date_end to second year
                competition.setDateStart(LocalDate.of(startYear, 1, 1));
                competition.setDateEnd(LocalDate.of(endYear, 12, 31));
                return;
            }
        }

        // Fallback to extracting from title and URL
        String title = competition.getTitle().toLowerCase();
        String url = competition.getUrl().toLowerCase();
        String season = null;
        int seasonYear = extractSeasonYear(competition);

        // Extract season type (hiver/été)
        if (title.contains("hiver") || title.contains("d'hiver")) {
            season = "hiver";
        } else if (title.contains("été") || title.contains("d'été")) {
            season = "été";
        } else if (title.contains("ouverture")) {
            season = "ouverture";
        } else if (title.contains("25m")) {
            season = "hiver";
        } else if (title.contains("50m")) {
            season = "été";
        } else if (title.contains("tc")) {
            if (competition.getDateStart() != null) {
                int month = competition.getDateStart().getMonthValue();
                season = (month >= 4 && month <= 9) ? "été" : "hiver";
            }
        }

        // If we have a season type and year, set approximate dates
        if (season != null && seasonYear > 0) {
            competition.setSeason(season + " " + seasonYear);
            if ("hiver".equals(season)) {
                competition.setDateStart(LocalDate.of(seasonYear, 10, 1));
                competition.setDateEnd(LocalDate.of(seasonYear + 1, 3, 31));
            } else {
                competition.setDateStart(LocalDate.of(seasonYear, 4, 1));
                competition.setDateEnd(LocalDate.of(seasonYear, 9, 30));
            }
        } else if (seasonYear > 0) {
            // If we only have year, set full year
            competition.setDateStart(LocalDate.of(seasonYear, 1, 1));
            competition.setDateEnd(LocalDate.of(seasonYear, 12, 31));
        }

        // If we still don't have dates, try to extract from URL
        if (competition.getDateStart() == null) {
            extractDatesFromUrl(competition);
        }
    }

    private void extractDatesFromUrl(Competition competition) {
        // Extract year from URL path (e.g., /2023/12/res_24001-3.html)
        Pattern urlPattern = Pattern.compile("/(20\\d{2})/\\d{2}/");
        Matcher urlMatcher = urlPattern.matcher(competition.getUrl());

        if (urlMatcher.find()) {
            int year = Integer.parseInt(urlMatcher.group(1));
            competition.setDateStart(LocalDate.of(year, 1, 1));
            competition.setDateEnd(LocalDate.of(year, 12, 31));
        }
    }

    private int extractSeasonYear(Competition competition) {
        // First try to extract from season string if available
        if (competition.getSeason() != null) {
            Pattern seasonPattern = Pattern.compile("(20\\d{2})");
            Matcher seasonMatcher = seasonPattern.matcher(competition.getSeason());
            if (seasonMatcher.find()) {
                return Integer.parseInt(seasonMatcher.group(1));
            }
        }

        // Then try to extract from URL
        Pattern urlPattern = Pattern.compile("/(20\\d{2})/");
        Matcher urlMatcher = urlPattern.matcher(competition.getUrl());
        if (urlMatcher.find()) {
            return Integer.parseInt(urlMatcher.group(1));
        }

        // Finally try to extract from title
        Pattern titlePattern = Pattern.compile("(20\\d{2})");
        Matcher titleMatcher = titlePattern.matcher(competition.getTitle());
        if (titleMatcher.find()) {
            return Integer.parseInt(titleMatcher.group(1));
        }

        return -1;
    }
}