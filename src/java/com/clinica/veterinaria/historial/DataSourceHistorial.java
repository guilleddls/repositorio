package com.clinica.veterinaria.historial;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;


public class DataSourceHistorial extends ManagerConexion {

    public DataSourceHistorial() {
    }
	
    

    private DataSourceHistorial(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
                    //stmt.executeUpdate("drop table zk_cliente if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_historial` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `id_mascota` int(11) NOT NULL, " +
                                       " `id_veterinario` int(11) NOT NULL, " +
                                       " `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                       " `tipo_visita` int(1) NOT NULL, " +
                                       " `id_peso` int(11) NOT NULL, " +
                                       " `anamnesis` varchar(500) DEFAULT NULL, " +
                                       " `diagnostico` varchar(500) DEFAULT NULL, " +
                                       " `tratamiento` varchar(500) DEFAULT NULL, " +
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