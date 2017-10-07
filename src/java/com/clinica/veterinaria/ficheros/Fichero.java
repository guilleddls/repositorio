package com.clinica.veterinaria.ficheros;

import java.util.Date;

/**
 *
 * 
 */
public class Fichero {
    private int _id;
    private int _id_externo;
    private int _tipo;
    private String _ruta;
    private Date _fecha;

    public Fichero() {
       
    }
    
    public Fichero(int _id, int _id_externo, int _tipo, String _ruta, Date _fecha) {
        this._id = _id;
        this._id_externo = _id_externo;
        this._tipo = _tipo;
        this._ruta = _ruta;
        this._fecha = _fecha;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getId_externo() {
        return _id_externo;
    }

    public void setId_externo(int _id_externo) {
        this._id_externo = _id_externo;
    }

    public int getTipo() {
        return _tipo;
    }

    public void setTipo(int _tipo) {
        this._tipo = _tipo;
    }

    public String getRuta() {
        return (_ruta!=null)? "http://json-veterinaria.16mb.com/ficheros/uploads/"+_ruta : null;
    }

    public void setRuta(String _ruta) {
        this._ruta = _ruta;
    }
    
    public String getImagen(){
        return _ruta;
    }
    
    public String getNombre() {
//        StringBuilder sb = new StringBuilder(this.getRuta());
//        //String recortar = "/uploads/historiales/";
//        sb.delete(0, 23);
        return getImagen();//sb.toString();
    }

    public Date getFecha() {
        return _fecha;
    }

    public void setFecha(Date _fecha) {
        this._fecha = _fecha;
    }
    
    public String getExtension(){
        String extension[] = {".jpg", ".jpeg" , ".gif" , ".png", ".pdf", ".doc", ".docx", ".xls", "xlsx"};
        
        String aux = _ruta.toLowerCase();
        
        if( aux.endsWith(extension[0]) || 
            aux.endsWith(extension[1]) || 
            aux.endsWith(extension[2]) ||
            aux.endsWith(extension[3])
           )
        {
            return getRuta();
        }
        
        
        else if(aux.endsWith(extension[4])){//PDF
            return "/images/PDF.png";
        }
        else if(aux.endsWith(extension[5])){ //DOC
            return "/images/DOC.png";
        }
        else if(aux.endsWith(extension[6])){ //DOCX
            return "/images/DOC.png";
        }
        else if(aux.endsWith(extension[7])){ //XLS
            return "/images/XLS.png";
        }
        else if(aux.endsWith(extension[8])){ //XLS
            return "/images/XLS.png";
        }
        else {  //DEFAULT
            return "/images/DEFAULT.png";
        }
    }
}