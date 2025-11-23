package com.diro.ift2255;

import io.javalin.Javalin;
import com.diro.ift2255.config.Routes;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> { //omg ces erreurs la man ouf
                cors.addRule(it -> {
                    it.anyHost();
                });
            });
        }).start(3000);

        Routes.register(app);
        System.out.println("BACKEND RUNNING ON http://localhost:3000"); //jai du OCD, jai toujours teste sur ce port en backend lol
    }
}