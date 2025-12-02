package com.diro.ift2255.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;


public class ComparaisonTest {

    @Test
    void testCompareCredits_withNullAndNumericCredits() {
        Course courseA = new Course();
        courseA.setId("IFT2035");

        Course courseB = new Course() {
            @Override
            public String getCredits() {
                return "3";
            }
        };
        courseB.setId("IFT2000");

        Comparaison comparaison = new Comparaison(courseA, courseB);

        int diff = comparaison.compareCredits();

        assertEquals(3, diff,
                "La difference de credits entre 0 et 3 devrait etre 3");
    }

    @Test
    void testGetCommonSessions_simpleIntersection() {
        Course courseA = new Course();
        courseA.setId("IFT2125");
        courseA.setSessions(Arrays.asList("H25"));

        Course courseB = new Course();
        courseB.setId("IFT2015");
        courseB.setSessions(Arrays.asList("H25", "A26"));
        Comparaison comparaison = new Comparaison(courseA, courseB);

        List<String> common = comparaison.getCommonSessions();

        assertEquals(1, common.size(), "Il devrait y avoir exactement 1 session commune");
        assertTrue(common.contains("H25"), "La session commune devrait etre H25");
    }

    @Test
    void testGetMissingPrerequisites_symetricDifference() {
        Course courseA = new Course();
        courseA.setId("IFT2125");
        courseA.setPrerequisites(Arrays.asList("IFT2015", "MAT1978"));

        Course courseB = new Course();
        courseB.setId("IFT2255");
        courseB.setPrerequisites(Arrays.asList("IFT1025"));

        Comparaison comparaison = new Comparaison(courseA, courseB);

        Map<String, List<String>> missing = comparaison.getMissingPrerequisites();

        List<String> missingForA = missing.get("IFT2125");
        List<String> missingForB = missing.get("IFT2255");

        assertEquals(1, missingForA.size(), "Il devrait manquer 1 prerequis au cours A");
        assertTrue(missingForA.contains("IFT1025"),
                "Pour A, il devrait manquer le prerequis de B : IFT1025");

        assertEquals(2, missingForB.size(), "Il devrait manquer 2 prerequis au cours B");
        assertTrue(missingForB.contains("IFT2015") || missingForB.contains("MAT1978"),
                "Pour B, il devrait manquer les prerequis de A : IFT2015 et MAT1978");
    }
}