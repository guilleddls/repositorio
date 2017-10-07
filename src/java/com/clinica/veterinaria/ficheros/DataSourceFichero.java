package com.clinica.veterinaria.ficheros;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;


public class DataSourceFichero extends ManagerConexion {

    public DataSourceFichero() {
    }
	
    

    private DataSourceFichero(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_especie if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_ficheros` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `id_externo` int(11) NOT NULL, " +
                                       " `tipo` smallint(2) NOT NULL COMMENT '1)Historial', " +
                                       " `ruta` varchar(250) NOT NULL, " +
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