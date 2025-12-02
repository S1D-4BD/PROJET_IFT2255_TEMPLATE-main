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
    

    

}
