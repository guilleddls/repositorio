
package com.clinica.veterinaria.vacunas;


import com.conexion.ManagerConexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class DataSourceVacuna extends ManagerConexion{

    public DataSourceVacuna() {
    }
    	
    
    private DataSourceVacuna(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_raza if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_vacuna` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `nombre` varchar(255) NOT NULL, " +
                                       " `descripcion` varchar(1000) DEFAULT NULL, " +
                                       " `especie` int(11) NOT NULL, " +
                                       " `dias` int(4) NOT NULL, " +
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