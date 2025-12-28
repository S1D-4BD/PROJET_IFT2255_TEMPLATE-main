package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.service.TGDEService;
import java.util.Map;

public class TGDEController {
    private TGDEController service = new TGDEController();
/*
    public void getAll(Context ctx) {
        ctx.json(service.getAll());
    }

    public void getByNumero(Context ctx) {
        String m = ctx.pathParam("numero_employe");
        var student = service.getByMatricule(m);
        if (student.isPresent()) {
            ctx.json(student.get());
        } else {
            ctx.status(404).json(Map.of("error", "Not found"));
        }
    }
    */
}