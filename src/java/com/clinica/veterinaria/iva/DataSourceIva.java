/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clinica.veterinaria.iva;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class DataSourceIva extends ManagerConexion{

    public DataSourceIva() {
    }
       	
    

    private DataSourceIva(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_raza if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_iva` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `valor` int(11) NOT NULL, " +
                                       " `nombre` varchar(200) DEFAULT NULL, " +
                                       " `descripcion` varchar(250) DEFAULT NULL, " +
                                       " PRIMARY KEY (`id`) " +
                                       ") ENGINE=InnoDB  DEFAULT CHARSET=utf8;" );
                    
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }

    
}