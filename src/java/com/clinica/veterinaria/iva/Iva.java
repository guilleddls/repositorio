package com.clinica.veterinaria.iva;

/**
 * 
 */
public class Iva {
    
    private int _id;
    private float _valor;
    private String _nombre;
    private String _descripcion;

    public Iva() {}
    
    public Iva(int _id, int _valor, String _nombre, String _descripcion) {
        this._id = _id;
        this._valor = _valor;
        this._nombre = _nombre;
        this._descripcion = _descripcion;
    }
    
    public String getDescripcion() {
        return _descripcion;
    }

    public void setDescripcion(String _descripcion) {
        this._descripcion = _descripcion;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public float getValor() {
        return _valor;
    }

    public void setValor(float _valor) {
        this._valor = _valor;
    }

    public String getNombre() {
        return _nombre;
    }

    public void setNombre(String _nombre) {
        this._nombre = _nombre;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Iva other = (Iva) obj;
        if (this._valor != other._valor) {
            return false;
        }
        if ((this._nombre == null) ? (other._nombre != null) : !this._nombre.equals(other._nombre)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
