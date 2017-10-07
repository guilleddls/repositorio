package com.clinica.veterinaria.servicios;

import com.clinica.veterinaria.items.Item;
import com.clinica.veterinaria.iva.Iva;
import com.clinica.veterinaria.servicio_familia.ServicioFamilia;
import java.util.Date;

/**
 * 
 */

public class Servicio extends Item{
    private ServicioFamilia _familia;
    
    public Servicio() {}
    
    public Servicio(int _id, String _codigo, String _nombre, String _descripcion, float _precio, Iva _iva, String _observaciones , Date _fecha_alta, ServicioFamilia familia) 
    {
        super(_id, _codigo, _nombre, _descripcion,  _precio, _iva, _observaciones, _fecha_alta);
        this._familia = familia;
    }

    public ServicioFamilia getFamilia() {
        return _familia;
    }

    public void setFamilia(ServicioFamilia _familia) {
        this._familia = _familia;
    }
}
