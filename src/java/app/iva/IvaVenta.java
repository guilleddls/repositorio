package app.iva;

import com.clinica.veterinaria.clientes.Cliente;

/**
 *
 * 
 */
public class IvaVenta extends Iva{
    private Cliente cliente;

    public IvaVenta() {
        super();
    }
    
    
    
    public Cliente getCliente(){
        return cliente;
    }
    
    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }
    
    @Override
    public String getPersona(){
        return (cliente != null)? getCliente().getFullname(): "";
    }
    
    @Override
    public String getCuit(){
        return (cliente != null)? getCliente().getNif(): "";
    }
}
