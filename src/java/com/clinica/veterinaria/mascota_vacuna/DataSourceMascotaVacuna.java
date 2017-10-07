package com.clinica.veterinaria.mascota_vacuna;

import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 */
public class DataSourceMascotaVacuna extends ManagerConexion {

    public DataSourceMascotaVacuna() {
    }
       	
    

    private DataSourceMascotaVacuna(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_raza if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_mascota_vacuna` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `mascota` int(11) NOT NULL, " +
                                       " `vacuna` int(11) NOT NULL, " +
                                       " `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                       " `veterinario` int(11) NOT NULL, " +
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