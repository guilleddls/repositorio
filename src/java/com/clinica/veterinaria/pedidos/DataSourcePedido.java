package com.clinica.veterinaria.pedidos;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class DataSourcePedido extends ManagerConexion{

    public DataSourcePedido() {
    }
        	
    

    private DataSourcePedido(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_raza if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_pedido` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                       " `fecha_entrega` timestamp NULL DEFAULT NULL, " +
                                       " `fecha_pago` timestamp NULL DEFAULT NULL, " +
                                       " `pagado` tinyint(1) NOT NULL, " +
                                       " `id_proveedor` int(11) NOT NULL, " +
                                       " `id_empleado` int(11) NOT NULL, " +
                                       " PRIMARY KEY (`id`) " +
                                       ") ENGINE=InnoDB  DEFAULT CHARSET=utf8;"
                                       );
                    
                    
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }

  
}