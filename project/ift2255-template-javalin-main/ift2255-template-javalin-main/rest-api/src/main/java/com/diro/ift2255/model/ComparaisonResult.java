package com.diro.ift2255.model;

import java.util.List;

public class ComparaisonResult {
    private int creditDifference;
    private double reviewScoreDifference;
    private List<String> commonSessions;
    private List<String> missingPrerequisitesForA;
    private List<String> missingPrerequisitesForB;

    // Getter Setters
    public int getCreditDifference() { return creditDifference; }
    public void setCreditDifference(int creditDifference) { this.creditDifference = creditDifference; }

    public double getReviewScoreDifference() { return reviewScoreDifference; }
    public void setReviewScoreDifference(double reviewScoreDifference) { this.reviewScoreDifference = reviewScoreDifference; }

    public List<String> getCommonSessions() { return commonSessions; }
    public void setCommonSessions(List<String> commonSessions) { this.commonSessions = commonSessions; }

    public List<String> getMissingPrerequisitesForA() { return missingPrerequisitesForA; }
    public void setMissingPrerequisitesForA(List<String> missingPrerequisitesForA) { this.missingPrerequisitesForA = missingPrerequisitesForA; }

    public List<String> getMissingPrerequisitesForB() { return missingPrerequisitesForB; }
    public void setMissingPrerequisitesForB(List<String> missingPrerequisitesForB) { this.missingPrerequisitesForB = missingPrerequisitesForB; }
}
