package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.service.StudentService;
import java.util.Map;

public class StudentController {
    private StudentService service = new StudentService();

    public void getAll(Context ctx) {
        ctx.json(service.getAll());
    }

    public void getByMatricule(Context ctx) {
        String m = ctx.pathParam("matricule");
        var student = service.getByMatricule(m);
        if (student.isPresent()) {
            ctx.json(student.get());
        } else {
            ctx.status(404).json(Map.of("error", "Not found"));
        }
    }
}