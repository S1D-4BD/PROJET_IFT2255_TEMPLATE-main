package com.diro.ift2255.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AvisServiceTest {

    private AvisService avisService;

    @BeforeEach
    void setup() {
        avisService = new AvisService();
    }
    // tests pour la fonctionnalité voir les avis étudiants pour un cours donné
    @Test
    void testGetByCourse_existingCourse() {
        String courseId = "IFT1015";
        List<Map<String, Object>> avis = avisService.getByCourse(courseId);

        assertNotNull(avis, "La liste ne devrait pas etre null");

        for (Map<String, Object> a : avis) {
            assertEquals(courseId.toUpperCase(), ((String) a.get("courseId")).toUpperCase(),
                    "Tous les avis devraient etre pour le cours " + courseId);
        }
    }

    @Test
    void testGetByCourse_courseWithNoReviews() {
        String courseId = "ABC_15999";

        List<Map<String, Object>> avis = avisService.getByCourse(courseId);

        assertNotNull(avis, "La liste ne devrait pas etre null");
        assertTrue(avis.isEmpty(), "La liste devrait etre vide pour un cours sans avis");
    }

    @Test
    void testGetByCourse_caseInsensitive() {
        String courseIdLower = "ift1015";
        String courseIdUpper = "IFT1015";

        List<Map<String, Object>> avisLower = avisService.getByCourse(courseIdLower);
        List<Map<String, Object>> avisUpper = avisService.getByCourse(courseIdUpper);

        assertEquals(avisLower.size(), avisUpper.size(),
                "Le nombre d'avis devrait etre le meme peu importe la casse");
    }

    @Test
    void testGetByCourse_courseIdWithSpaces() {
        Map<String, Object> avis = new HashMap<>();
        avis.put("courseId", "IFT7000");
        avis.put("rating", 4);

        avisService.create(avis);

        List<Map<String, Object>> result = avisService.getByCourse(" IFT7000 ");

        assertNotNull(result, "La liste ne devrait pas etre null");
        assertTrue(result.isEmpty(),
                "La liste devrait etre vide car les espaces ne sont pas ignores");
    }

    @Test
    void testGetByCourse_mixedCaseStoredCourseId() {
        Map<String, Object> avis = new HashMap<>();
        avis.put("courseId", "iFt5000");
        avis.put("rating", 5);
        avis.put("difficulty", 2);
        avis.put("charge", 1);

        avisService.create(avis);

        //  on recherche le cours avec une casse différente
        List<Map<String, Object>> result = avisService.getByCourse("IFT5000");

        assertNotNull(result, "La liste ne devrait pas etre null");
        assertFalse(result.isEmpty(), "La liste ne devrait pas etre vide");

        boolean found = false;
        for (Map<String, Object> a : result) {
            if (((String) a.get("courseId")).equalsIgnoreCase("IFT5000")
                    && (int) a.get("rating") == 5) {
                found = true;
            }
        }

        assertTrue(found, "Un avis avec rating 5 devrait etre present");
    }


    @Test
    void testGetStats_courseWithReviews() {
        String courseId = "IFT1015";
        Map<String, Object> stats = avisService.getStats(courseId);

        assertNotNull(stats, "Les stats ne devraient pas etre null");
        assertTrue(stats.containsKey("count"), "Les stats devraient contenir 'count'");
        assertTrue(stats.containsKey("avg_rating"), "Les stats devraient contenir 'avg_rating'");
        assertTrue(stats.containsKey("avg_difficulty"), "Les stats devraient contenir 'avg_difficulty'");
        assertTrue(stats.containsKey("avg_charge"), "Les stats devraient contenir 'avg_charge'");
    }
////////////////////////
    @Test
    void testGetStats_courseWithNoReviews() {
        String courseId = "ABCD__999";
        Map<String, Object> stats = avisService.getStats(courseId);

        assertNotNull(stats, "Les stats ne devraient pas etre null");

        assertEquals(0, stats.get("count"), "Le count devrait etre 0");
        assertEquals(0, stats.get("avg_rating"), "La moyenne rating devrait etre 0");
        assertEquals(0, stats.get("avg_difficulty"), "La moyenne difficulty devrait etre 0");
        assertEquals(0, stats.get("avg_charge"), "La moyenne charge devrait etre 0");
    }

    @Test
    void testGetStats_averagesInValidRange() {
        String courseId = "IFT1015";

        Map<String, Object> stats = avisService.getStats(courseId);
        int count = (int) stats.get("count");

        if (count > 0) {
            double avgRating = ((Number) stats.get("avg_rating")).doubleValue();
            double avgDifficulty = ((Number) stats.get("avg_difficulty")).doubleValue();
            double avgCharge = ((Number) stats.get("avg_charge")).doubleValue();

            assertTrue(avgRating >= 0 && avgRating <= 5,
                    "La moyenne rating devrait etre entre 0 et 5");
            assertTrue(avgDifficulty >= 0 && avgDifficulty <= 5,
                    "La moyenne difficulty devrait etre entre 0 et 5");
            assertTrue(avgCharge >= 0 && avgCharge <= 5,
                    "La moyenne charge devrait etre entre 0 et 5");
        }
    }

    @Test
    void testGetAll_returnsListOfAvis() {
        List<Map<String, Object>> allAvis = avisService.getAll();

        assertNotNull(allAvis, "La liste ne devrait pas etre null");
    }
    
    // tests pour la fonctionnalité soummettre un avis pour un cours
    @Test
    void testCreate_addsNewAvis() {
        int countBefore = avisService.getAll().size();

        Map<String, Object> newAvis = new HashMap<>();
        newAvis.put("courseId", "TEST1234");
        newAvis.put("rating", 4);
        newAvis.put("difficulty", 3);
        newAvis.put("charge", 2);
        newAvis.put("comment", "Test comment");

        Map<String, Object> created = avisService.create(newAvis);
        int countAfter = avisService.getAll().size();

        assertNotNull(created, "L'avis cree ne devrait pas etre null");
        assertEquals(countBefore + 1, countAfter, "Le nombre d'avis devrait augmenter de 1");
        assertEquals("TEST1234", created.get("courseId"), "Le courseId devrait etre correct");
    }

    @Test
    void testCreate_thenGetByCourse() {
        String uniqueCourseId = "UNIQUE_" + System.currentTimeMillis();

        Map<String, Object> newAvis = new HashMap<>();
        newAvis.put("courseId", uniqueCourseId);
        newAvis.put("rating", 5);
        newAvis.put("difficulty", 1);
        newAvis.put("charge", 1);
        newAvis.put("comment", "Super cours!");

        avisService.create(newAvis);
        List<Map<String, Object>> avis = avisService.getByCourse(uniqueCourseId);

        assertEquals(1, avis.size(), "Il devrait y avoir 1 avis pour ce cours");
        assertEquals(5, avis.get(0).get("rating"), "Le rating devrait etre 5");
    }

    @Test
    void testCreate_multipleAvisForSameCourse() {
        String courseId = "IFT2015";

        Map<String, Object> avis1 = new HashMap<>();
        avis1.put("courseId", courseId);
        avis1.put("rating", 4);

        Map<String, Object> avis2 = new HashMap<>();
        avis2.put("courseId", courseId);
        avis2.put("rating", 2);

        avisService.create(avis1);
        avisService.create(avis2);

        List<Map<String, Object>> result = avisService.getByCourse(courseId);

        assertNotNull(result, "La liste ne devrait pas etre null");

        boolean hasRating4 = false;
        boolean hasRating2 = false;

        for (Map<String, Object> a : result) {
            if ((int) a.get("rating") == 4) hasRating4 = true;
            if ((int) a.get("rating") == 2) hasRating2 = true;
        }

        assertTrue(hasRating4, "Un avis avec rating 4 devrait etre present");
        assertTrue(hasRating2, "Un avis avec rating 2 devrait etre present");
    }

    @Test
    void testCreate_withMissingDifficultyAndCharge() {
        int countBefore = avisService.getAll().size();

        Map<String, Object> avis = new HashMap<>();
        avis.put("courseId", "IFT9000");
        avis.put("rating", 4);
        avis.put("comment", "Champs manquants");

        Map<String, Object> created = avisService.create(avis);
        int countAfter = avisService.getAll().size();

        assertNotNull(created, "L'avis cree ne devrait pas etre null");
        assertEquals(countBefore + 1, countAfter,
                "Le nombre d'avis devrait augmenter de 1");

        assertEquals("IFT9000", created.get("courseId"));
        assertEquals(4, created.get("rating"));
        assertEquals("Champs manquants", created.get("comment"));

        assertFalse(created.containsKey("difficulty"),
                "Le champ difficulty ne devrait pas exister");
        assertFalse(created.containsKey("charge"),
                "Le champ charge ne devrait pas exister");
    }

    @Test
    void testCreate_withOnlyRequiredFields() {
        int countBefore = avisService.getAll().size();

        Map<String, Object> avis = new HashMap<>();
        avis.put("courseId", "IFT1234");

        Map<String, Object> created = avisService.create(avis);
        int countAfter = avisService.getAll().size();

        assertNotNull(created, "L'avis cree ne devrait pas etre null");
        assertEquals(countBefore + 1, countAfter,
                "Le nombre d'avis devrait augmenter de 1");

        assertEquals("IFT1234", created.get("courseId"));
        assertFalse(created.containsKey("rating"));
        assertFalse(created.containsKey("difficulty"));
        assertFalse(created.containsKey("charge"));
        assertFalse(created.containsKey("comment"));
    }
}