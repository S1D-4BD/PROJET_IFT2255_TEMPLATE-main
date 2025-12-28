package com.diro.ift2255.service;

import com.diro.ift2255.model.TGDE;
import java.util.*;

public class TGDEService {
    private Map<String, TGDE> TGDE = new HashMap<>();

    public TGDEService() {
      TGDE.put("12345", new TGDE("12345", "Romy Castillo", "Informatique"));
      TGDE.put("54321", new TGDE("54321", "Susana Hernandez", "Mathématiques"));


    }

    public Optional<TGDE> getByNumero(String m) {
        return Optional.ofNullable(TGDE.get(m));
    }

    public List<TGDE> getAll() {
        return new ArrayList<>(TGDE.values());
    }

}