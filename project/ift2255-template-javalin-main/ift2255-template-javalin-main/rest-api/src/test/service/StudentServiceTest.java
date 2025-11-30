package com.diro.ift2255.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.diro.ift2255.model.Student;

class StudentServiceTest {

    // Test pour vérifier qu'on retrouve bien un étudiant existant
    @Test
    void testGetByMatricule_existingStudent() {
        StudentService service = new StudentService();

        Optional<Student> opt = service.getByMatricule("20280089");

        // On s’attend à trouver Aya
        assertTrue(opt.isPresent(), "L'étudiant devrait exister");
        assertEquals("Aya Dair", opt.get().getName());
    }

    // Test pour un matricule qui n’existe pas
    @Test
    void testGetByMatricule_nonExistingStudent() {
        StudentService service = new StudentService();

        Optional<Student> opt = service.getByMatricule("00000000");

        // Ici, aucun étudiant ne doit être trouvé
        assertFalse(opt.isPresent(), "L'étudiant ne devrait pas exister");
    }

    // Test pour vérifier qu’on récupère bien tous les étudiants
    @Test
    void testGetAll_returnsAllStudents() {
        StudentService service = new StudentService();

        List<Student> students = service.getAll();

        // On sait qu’il y a 3 étudiants dans le constructeur
        assertEquals(3, students.size(), "Il devrait y avoir 3 étudiants");
    }

    // Test pour vérifier l’éligibilité quand toutes les conditions sont remplies
    @Test
    void testIsEligible_withAllPrerequisites() {
        StudentService service = new StudentService();
        Student aya = service.getByMatricule("20280089").get();

        List<String> prereq = Arrays.asList("IFT1015", "IFT1005");
        boolean eligible = service.isEligible(aya, prereq);

        assertTrue(eligible, "Aya a bien complété les prérequis");
    }

    // Test pour vérifier quand il manque un prérequis
    @Test
    void testIsEligible_missingPrerequisites() {
        StudentService service = new StudentService();
        Student gabriel = service.getByMatricule("20283304").get();

        List<String> prereq = Arrays.asList("IFT3355"); // Gabriel ne l’a pas
        boolean eligible = service.isEligible(gabriel, prereq);

        assertFalse(eligible, "Gabriel ne devrait pas être éligible");
    }

    // Test pour le cas trivial : pas de prérequis
    @Test
    void testIsEligible_emptyPrerequisites() {
        StudentService service = new StudentService();
        Student celina = service.getByMatricule("20279666").get();

        List<String> prereq = Collections.emptyList();
        boolean eligible = service.isEligible(celina, prereq);

        assertTrue(eligible, "Sans prérequis, tout le monde est éligible");
    }
}




