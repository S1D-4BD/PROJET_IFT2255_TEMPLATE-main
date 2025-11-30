package com.diro.ift2255.model;


import com.diro.ift2255.service.AvisService;////
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;




public class Comparaison {
    private final Course courseA;
    private final Course courseB;
    private final AvisService avisService; //since on doit retourner avis

    public Comparaison(Course courseA, Course courseB) {
        this.courseA = courseA;
        this.courseB = courseB;
        this.avisService = new AvisService();
    }

    private double safeCredits(Course c) {
        String s = c.getCredits();
        if (s == null) return 0.0;
        try {
            return Double.parseDouble(s.trim());////(Double) s
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private double safeAverageReviewScore(Course c) {
        Map<String, Object> stats = avisService.getStats(c.getId());
        Object avgRating = stats.get("avg_rating");
        if (avgRating instanceof Number) {
            return ((Number) avgRating).doubleValue();
        }
        return 0.0;
    }

    private List<String> safeOfferedSessions(Course c) {
        List<String> s = c.getSessions();
        return (s == null) ? Collections.emptyList() : s;
    }

    private List<String> safePrerequisites(Course c) {
        List<String> p = c.getPrerequisites();
        return (p == null) ? Collections.emptyList() : p;
    }

    public int compareCredits() {
        return (int) Math.abs(safeCredits(courseA) - safeCredits(courseB));
    }

    public double compareReviews() {
        return Math.abs(safeAverageReviewScore(courseA) - safeAverageReviewScore(courseB));
    }

    public List<String> getCommonSessions() {
        Set<String> sessionsA = new HashSet<>(safeOfferedSessions(courseA));
        Set<String> sessionsB = new HashSet<>(safeOfferedSessions(courseB));
        sessionsA.retainAll(sessionsB);
        return new ArrayList<>(sessionsA);
    }

    public Map<String, List<String>> getMissingPrerequisites() {
        Set<String> prereqA = new HashSet<>(safePrerequisites(courseA));
        Set<String> prereqB = new HashSet<>(safePrerequisites(courseB));

        Set<String> missingForA = new HashSet<>(prereqB);
        missingForA.removeAll(prereqA);

        Set<String> missingForB = new HashSet<>(prereqA);
        missingForB.removeAll(prereqB);

        Map<String, List<String>> result = new HashMap<>();
        result.put(courseA.getId(), new ArrayList<>(missingForA));
        result.put(courseB.getId(), new ArrayList<>(missingForB));
        return result;
    }

    public ComparaisonResult buildResult() {
        ComparaisonResult result = new ComparaisonResult();
        result.setCreditDifference(compareCredits());
        result.setReviewScoreDifference(compareReviews());
        result.setCommonSessions(getCommonSessions());

        Map<String, List<String>> prereqs = getMissingPrerequisites();
        result.setMissingPrerequisitesForA(prereqs.get(courseA.getId()));
        result.setMissingPrerequisitesForB(prereqs.get(courseB.getId()));
        return result;
    }
}
