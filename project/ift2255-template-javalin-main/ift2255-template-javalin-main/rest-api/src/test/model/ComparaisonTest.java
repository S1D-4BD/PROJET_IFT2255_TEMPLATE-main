package com.diro.ift2255.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;


public class ComparaisonTest {
   
    //TEST: compareCredits(), tester la comparaison du nbr de credits entre deux cours (un dont les crédits ne sont pas définis)
    @Test
    void testCompareCredits_withNullAndNumericCredits() {
        //Arrange
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

        //Act
        int diff = comparaison.compareCredits();
        //Assert
        assertEquals(3, diff,
                "La différence de crédits entre 0 et 3 devrait être 3");
    }

    
    // TEST: getCommonSessions(), trouver l'intersection, donc la session commune dans laquelle deux cours se donne
    @Test
    void testGetCommonSessions_simpleIntersection() {
        //Arrange
        Course courseA = new Course();
        courseA.setId("IFT2125");
        courseA.setSessions(Arrays.asList("H25"));

        Course courseB = new Course();
        courseB.setId("IFT2015");
        courseB.setSessions(Arrays.asList("H25", "A26"));
        Comparaison comparaison = new Comparaison(courseA, courseB);

        //Act
        List<String> common = comparaison.getCommonSessions();
        //Assert
        assertEquals(1, common.size(), "Il devrait y avoir exactement 1 session commune");
        assertTrue(common.contains("H25"), "La session commune devrait être H25");
    }

    // TEST: getMissingPrerequisites(), entre deux cours, trouver les prérequis qu'un cours A n'a pas et qu'un cours B a, et vice-versa
    @Test
    void testGetMissingPrerequisites_symetricDifference() {
        //Arrange
        Course courseA = new Course();
        courseA.setId("IFT2125");
        courseA.setPrerequisites(Arrays.asList("IFT2015", "MAT1978"));

        Course courseB = new Course();
        courseB.setId("IFT2255");
        courseB.setPrerequisites(Arrays.asList("IFT1025"));

        Comparaison comparaison = new Comparaison(courseA, courseB);

        //Act
        Map<String, List<String>> missing = comparaison.getMissingPrerequisites();

        List<String> missingForA = missing.get("IFT2125");
        List<String> missingForB = missing.get("IFT2255");

        //Assert
        assertEquals(1, missingForA.size(), "Il devrait manquer 1 prérequis au cours A");
        assertTrue(missingForA.contains("IFT1025"),
                "Pour A, il devrait manquer le prérequis de B : IFT1025");

        assertEquals(1, missingForB.size(), "Il devrait manquer 1 prérequis au cours B");
        assertTrue(missingForB.contains("IFT2015","MAT1978"),
                "Pour B, il devrait manquer les prérequis de A : IFT2015 et MAT1978");
    }

    

}
