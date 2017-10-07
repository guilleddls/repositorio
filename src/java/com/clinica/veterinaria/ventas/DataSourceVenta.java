
package com.clinica.veterinaria.ventas;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class DataSourceVenta extends ManagerConexion{

    public DataSourceVenta() {
    }
    
    

    private DataSourceVenta(String asd) {
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_raza if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_venta` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                       " `id_cliente` int(11) NOT NULL, " +
                                       " `id_vendedor` int(11) NOT NULL, " +
                                       " `id_veterinario` int(11) DEFAULT NULL, " +
                                       " `albaran` varchar(255) DEFAULT NULL COMMENT 'ruta del albaran', " +
                                       " `factura` int(11) NOT NULL DEFAULT '0' COMMENT 'ruta de la factura', " +
                                       " `facturado` tinyint(1) NOT NULL DEFAULT '0', " +
                                       " PRIMARY KEY (`id`) " +
                                       ") ENGINE=InnoDB  DEFAULT CHARSET=utf8;");
                    
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }


}