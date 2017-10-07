package com.clinica.veterinaria.provincias;

import java.util.Date;

/**
 * Provincia.
 * 
 * 
 */

//import java.util.Date;

public class Provincia {
    private int id;
    private String provincia;

    public Provincia () {}
    
    public Provincia(int _id, String _provincia) {
        this.id = _id;
        this.provincia = _provincia;
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String _provincia) {
        this.provincia = _provincia;
    }

    @Override
    public String toString() {
        return provincia;
    }
    
   
}
