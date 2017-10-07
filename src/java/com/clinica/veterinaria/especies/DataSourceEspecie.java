package com.clinica.veterinaria.especies;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;


public class DataSourceEspecie extends ManagerConexion{

    public DataSourceEspecie() {
    }
	
    

    private DataSourceEspecie(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_especie if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_especie` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `especie` varchar(200) NOT NULL, " +
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