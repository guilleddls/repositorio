package com.clinica.veterinaria.poblaciones;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;


public class DataSourcePoblacion extends ManagerConexion{

    public DataSourcePoblacion() {
    }
	
    

    private DataSourcePoblacion(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
                    //stmt.executeUpdate("drop table zk_poblacion if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_poblacion` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `provincia` int(11) NOT NULL, " +
                                       " `poblacion` varchar(200) NOT NULL, " +
                                       " PRIMARY KEY (`id`), " +
                                       " UNIQUE KEY `codigopostal` (`poblacion`) " +
                                       ") ENGINE=InnoDB  DEFAULT CHARSET=utf8;");
                    
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }

}