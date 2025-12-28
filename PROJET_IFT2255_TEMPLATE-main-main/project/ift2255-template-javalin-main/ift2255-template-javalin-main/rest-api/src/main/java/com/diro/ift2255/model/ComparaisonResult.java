package com.diro.ift2255.model;

import java.util.List;

public class ComparaisonResult {
    private int creditDifference;
    private double reviewScoreDifference;
    private List<String> commonSessions;
    private List<String> missingPrerequisitesForA;
    private List<String> missingPrerequisitesForB;

    private double avgRatingA; //qui vient du jsob
    private double avgRatingB;

    private double scoreAcademiqueA;
    private double scoreAcademiqueB; //qui vienent du csv

    private double avgChargeA;
    private double avgChargeB;
    private String moyenneA;  // moy lettrees
    private String moyenneB;

    // getters et les seters
    public int getCreditDifference() {
        return creditDifference;
    }
    public void setCreditDifference(int creditDifference) {
        this.creditDifference = creditDifference;
    }

    public double getReviewScoreDifference() {
        return reviewScoreDifference;
    }
    public void setReviewScoreDifference(double reviewScoreDifference) {
        this.reviewScoreDifference = reviewScoreDifference;
    }

    public List<String> getCommonSessions() {
        return commonSessions;
    }
    public void setCommonSessions(List<String> commonSessions) {
        this.commonSessions = commonSessions;
    }

    public List<String> getMissingPrerequisitesForA() {
        return missingPrerequisitesForA;
    }
    public void setMissingPrerequisitesForA(List<String> missingPrerequisitesForA) {
        this.missingPrerequisitesForA = missingPrerequisitesForA;
    }

    public List<String> getMissingPrerequisitesForB() {
        return missingPrerequisitesForB;
    }
    public void setMissingPrerequisitesForB(List<String> missingPrerequisitesForB) {
        this.missingPrerequisitesForB = missingPrerequisitesForB;
    }

    public double getAvgRatingA() {
        return avgRatingA;
    }
    public void setAvgRatingA(double avgRatingA) {
        this.avgRatingA = avgRatingA;
    }

    public double getAvgRatingB() {
        return avgRatingB;
    }
    public void setAvgRatingB(double avgRatingB) {
        this.avgRatingB = avgRatingB;
    }

    public double getScoreAcademiqueA() {
        return scoreAcademiqueA;
    }
    public void setScoreAcademiqueA(double s) {
        this.scoreAcademiqueA = s;
    }

    public double getScoreAcademiqueB() {
        return scoreAcademiqueB;
    }
    public void setScoreAcademiqueB(double s) {
        this.scoreAcademiqueB = s;
    }

    public double getAvgChargeA() {
        return avgChargeA;
    }
    public void setAvgChargeA(double c) {
        this.avgChargeA = c;
    }

    public double getAvgChargeB() {
        return avgChargeB;
    }
    public void setAvgChargeB(double c) {
        this.avgChargeB = c;
    }

    public String getMoyenneA() {
        return moyenneA;
    }
    public void setMoyenneA(String m) {
        this.moyenneA = m;
    }

    public String getMoyenneB() {
        return moyenneB;
    }
    public void setMoyenneB(String m) {
        this.moyenneB = m;
    }
}
