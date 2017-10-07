/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clinica.veterinaria.citas;


/**
 *
 * 
 */
import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.servicio_familia.ServicioFamilia;
import com.clinica.veterinaria.user.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cita {
    int _id;
    Date _fecha;
    String _hora;
    String _informe;
    Mascota _mascota;
    Cliente _cliente;
    ServicioFamilia _servicio;
    int _estado; // 0)Cancelado 1)Acudido 2)Pendiente(Predeterminado)
    User _empleado;
    Date fechatiempo;
    
    
    public Cita() {}
    
    public Cita(int _id, Date _fecha, String _hora, String _informe, ServicioFamilia _servicio, 
            Mascota _mascota, Cliente _cliente, int _estado, User _empleado) {
        this._id = _id;
        this._fecha = _fecha;
        this._hora = _hora;
        this._informe = _informe;
        this._mascota = _mascota;
        this._cliente = _cliente;
        this._servicio = _servicio;
        this._estado = _estado;
        this._empleado = _empleado;
    }

    public String getHora() {
        return _hora;
    }

    public void setHora(String _hora) {
        this._hora = _hora;
    }

    public ServicioFamilia getServicio() {
        return _servicio;
    }

    public void setServicio(ServicioFamilia _tipo) {
        this._servicio = _tipo;
    }
        
    public Date getFecha() {
        return _fecha;
    }

    public void setFecha(Date _fecha) {
        this._fecha = _fecha;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public Mascota getMascota() {
        return _mascota;
    }

    public void setMascota(Mascota _mascota) {
        this._mascota = _mascota;
    }
    
    public Cliente getCliente() {
        return _cliente;
    }

    public void setCliente(Cliente _cliente) {
        this._cliente = _cliente;
    }
    
    public String getInforme() {
        return _informe;
    }

    public void setInforme(String _informe) {
        this._informe = _informe;
    }

    public int getEstado() {
        return _estado;
    }

    public void setEstado(int _estado) {
        this._estado = _estado;
    }

    public User getEmpleado() {
        return _empleado;
    }

    public void setEmpleado(User _empleado) {
        this._empleado = _empleado;
    }

    public Date getFechatiempo() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(_fecha +" "+_hora);
        } catch (ParseException ex) {
            Logger.getLogger(Cita.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

   
    
}
