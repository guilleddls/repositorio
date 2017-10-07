/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clinica.veterinaria.ventas_linea;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class DataSourceVentaLinea extends ManagerConexion{

    public DataSourceVentaLinea() {
    }
    
    

    private DataSourceVentaLinea(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_venta_linea` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `id_venta` int(11) NOT NULL, " +
                                       " `id_producto` int(11) NOT NULL, " +
                                       " `cantidad` int(11) NOT NULL, " +
                                       " `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                       " `tipo` smallint(1) NOT NULL COMMENT '1)Producto 2)Servicio', " +
                                       " `pvp` float DEFAULT NULL, " +
                                       " `iva` int(2) DEFAULT NULL, " +
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