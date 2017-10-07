/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clinica.veterinaria.pedido_linea;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class DataSourcePedidoLinea extends ManagerConexion {

    public DataSourcePedidoLinea() {
    }

    
    
    private DataSourcePedidoLinea(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_raza if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_pedido_linea` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `cantidad` int(11) NOT NULL, " +
                                       " `coste` double NOT NULL, " +
                                       " `id_pedido` int(11) NOT NULL, " +
                                       " `id_producto` int(11) NOT NULL, " +
                                       " `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
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