package com.diro.ift2255.service;
/////suuuper important --->>> https://www.baeldung.com/jackson-object-mapper-tutorial
import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.core.type.TypeReference; // i need it to read json
import com.fasterxml.jackson.databind.ObjectMapper; //aussi, on recupere du json pour creer un objet (par serialisation /deserialisation)

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.*;


///
//map is nice here car on l utilise comme dictionnaire string to object

public class AvisService {
    private static final String AVIS_PATH = "data/avis.json";
    private ObjectMapper mapper = new ObjectMapper();


    public List<Map<String, Object>> getAll() {
        return readFile();
    }

  public List<Map<String, Object>> getByCourse(String courseId) {
    List<Map<String, Object>> tousLesAvis = readFile();
    List<Map<String, Object>> avisFiltred = new ArrayList<>();


      for (Map<String, Object> avis : tousLesAvis) {//on check pour tt les avis
          String idDuCours = (String) avis.get("courseId");
          if (courseId.equalsIgnoreCase(idDuCours)) { //SI C EST LE BON COURS
              avisFiltred.add(avis);
          }
        }

        return avisFiltred;
  }


    public Map<String, Object> getStats(String courseId) {
      List<Map<String, Object>> Listeavis = getByCourse(courseId);

      //CAS DE BASE SI YA AUCUN AVIS BAH NO STATS ON THAT
      if (Listeavis.isEmpty()) {
          Map<String, Object> stats_NULL = new HashMap<>();
          stats_NULL.put("count", 0);
          stats_NULL.put("avg_rating", 0);
          stats_NULL.put("avg_difficulty", 0);
          stats_NULL.put("avg_charge", 0);
          stats_NULL.put("score_academique", CalculerCSV(courseId));
          return stats_NULL;
      }


        int TOTALRating = 0;          //SI NN ON A AT LEAST 1 AVIS
        for (Map<String, Object> avis : Listeavis) {
          int rating = (int) avis.get("rating");
          TOTALRating = TOTALRating + rating;
        }

      int TOTALDifficulty = 0;
      for (Map<String, Object> avis : Listeavis) {
        int difficulty = (int) avis.get("difficulty");
        TOTALDifficulty = TOTALDifficulty + difficulty;
      }

        int TOTALCharge = 0;
        for (Map<String, Object> avis : Listeavis) { //pr chaqu avis a traiter
            int charge = (int) avis.get("charge");
            TOTALCharge += charge;
        }

        int nbravis = Listeavis.size();
        double moyenneRating = (double) TOTALRating / nbravis;
        double moyenneDifficulty = (double) TOTALDifficulty / nbravis;
        double moyenneWorkload = (double) TOTALCharge / nbravis;

        double moyenneRatingDEC = arrondir(moyenneRating);
        double moyenneDifficultyDEC = arrondir(moyenneDifficulty);
        double moyenneChargeDEC = arrondir(moyenneWorkload);


        Map<String, Object> stats = new HashMap<>();
        stats.put("count", nbravis);
        stats.put("avg_rating", moyenneRatingDEC);
        stats.put("avg_difficulty", moyenneDifficultyDEC);
        stats.put("avg_charge", moyenneChargeDEC);
        stats.put("score_academique", CalculerCSV(courseId));

        return stats;
    }


    private double arrondir(double valeur) { //on veux pas call ca everywhere
      double multiple = valeur * 10;
      ///
      long arrondi = Math.round(multiple);
      double resultat = arrondi / 10.0;


        return resultat;
    }


    public Map<String, Object> create(Map<String, Object> avis) {
        List<Map<String, Object>> allavis = readFile();
        allavis.add(avis);

///serialise
        writeFile(allavis);
        return avis;
    }

    private List<Map<String, Object>> readFile() {
        try {
          File fichier = new File(AVIS_PATH);

          if (!fichier.exists()) {
              return new ArrayList<>();
          }

          TypeReference<List<Map<String, Object>>> typeReference = new TypeReference<>() {};
          List<Map<String, Object>> avis = mapper.readValue(fichier, typeReference);
          return avis;

        } catch (IOException e) {
            return new ArrayList<>();
        }
    }


    private void writeFile(List<Map<String, Object>> avis) {
      try {
          File fichier = new File(AVIS_PATH);
          mapper.writerWithDefaultPrettyPrinter().writeValue(fichier, avis);
      } catch (IOException e) {
          e.printStackTrace();
      }
    }

    private double CalculerCSV(String courseId) { //c'est la mm fct que pour les comparaisons de cours
        try (BufferedReader br = new BufferedReader(new FileReader("data/historique_cours_prog_117510.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equalsIgnoreCase(courseId)) {
                    return Double.parseDouble(parts[3]);
                }
            }
        } catch (Exception e) {}
        return 0.0;
    }
}