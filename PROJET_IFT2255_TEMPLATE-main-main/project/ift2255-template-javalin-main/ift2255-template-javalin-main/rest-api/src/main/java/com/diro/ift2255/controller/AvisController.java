package com.diro.ift2255.controller;


import io.javalin.http.Context;
import com.diro.ift2255.service.AvisService;

import java.util.Map;

public class AvisController {
    private AvisService service = new AvisService();

////////////////////////////////////////////////
      public void getAll(Context ctx) {
          ctx.json(service.getAll());
      }


    public void getByCourse(Context ctx) {
      String courseId = ctx.pathParam("courseId");
      ctx.json(service.getByCourse(courseId));
    }

  public void getStats(Context ctx) {
      String courseId = ctx.pathParam("courseId");
      ctx.json(service.getStats(courseId));
  }

    public void create(Context ctx) {
      Map<String, Object> avis = ctx.bodyAsClass(Map.class);
      Map<String, Object> created = service.create(avis);

      ctx.status(201).json(created);
    }
}