package com.clinica.veterinaria.especies;

import com.clinica.veterinaria.razas.Raza;
import java.util.HashSet;

/**
 *
 * 
 */
public class Especie {
    private int _id;
    private String _especie;
    private HashSet<Raza> razas = new HashSet<>();

    public Especie() {}
    
    public Especie(int _id, String _especie) {
        this._id = _id;
        this._especie = _especie;
    }

    public String getEspecie() {
        return _especie;
    }

    public void setEspecie(String _especie) {
        this._especie = _especie;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }
    
    public void asignarRaza(Raza raza){
        if(!razas.contains(raza))
            razas.add(raza);
    }
    
    public void eliminarRaza(Raza raza){
        razas.remove(raza);
    }
    
    public HashSet<Raza> getRazas() {
        return razas;
    }

    public void setRazas(HashSet<Raza> razas) {
        this.razas = razas;
    }

    @Override
    public String toString() {
        return _especie;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Especie other = (Especie) obj;
        return this._id == other._id;
    }
    
    
    
}
