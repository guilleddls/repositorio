package com.clinica.veterinaria.servicio_familia;


import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 */
public class DataSourceServicioFamilia extends ManagerConexion {

    public DataSourceServicioFamilia() {
    }
     
    

    private DataSourceServicioFamilia(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_raza if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_servicio_familia` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `nombre` varchar(200) NOT NULL, " +
                                       " `descripcion` varchar(500) DEFAULT ' ', " +
                                       " PRIMARY KEY (`id`) ," +
                                       " UNIQUE KEY `nombre` (`nombre`)" +
                                       " ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;");
                    
                    ResultSet rs = stmt.executeQuery("SELECT * FROM zk_servicio_familia");
                    rs.last();
                    
                    if(rs.getRow() == 0){
                    
                        stmt.executeUpdate("INSERT INTO `zk_servicio_familia` (`id`, `nombre`, `descripcion`) VALUES " +
                                           " (1, 'Vacuna', 'Vacuna');");
                    }
                    
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }

 
}