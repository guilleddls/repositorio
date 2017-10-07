package com.clinica.veterinaria.poblaciones;

import com.clinica.veterinaria.provincias.Provincia;

/**
 * Poblacion.
 * 
 * 
 */

//import java.util.Date;

public class Poblacion {
    private int id;
    private int provincia;
    private String poblacion;
    private Provincia prov;

    public Poblacion () {}
    
    public Poblacion(int _id, int _provincia, String _poblacion) {
        this.id = _id;
        this.provincia = _provincia;
        this.poblacion = _poblacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }
    
    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String _poblacion) {
        this.poblacion = _poblacion;
    }

    public int getProvincia() {
        return provincia;
    }

    public void setProvincia(int _provincia) {
        this.provincia = _provincia;
    }

    public Provincia getProv() {
        return prov;
    }

    public void setProv(Provincia prov) {
        this.prov = prov;
    }

    @Override
    public String toString() {
        return poblacion;
    }

   
}
