package com.diro.ift2255.model;

import java.util.List;

public class TGDE {
    private String numero_employe;
    private String name;
    private String program;

    public TGDE() {}

    public TGDE(String numero_employe, String name, String program) {
        this.numero_employe = numero_employe;
        this.name = name;
        this.program = program;
    }

    public String getNumero() { return numero_employe; }
    public void setNumero(String numero_employe) { this.numero_employe = numero_employe; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

}