package com.clinica.veterinaria.citas;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;
//import org.hsqldb.jdbcDriver;

public class DataSourceCita extends ManagerConexion {

    public DataSourceCita() {
    }
	
   

   

    

    private DataSourceCita(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
                    //stmt.executeUpdate("drop table zk_cita if exists");
                    stmt.executeUpdate(" CREATE TABLE IF NOT EXISTS `zk_cita` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `fecha` date NOT NULL, " +
                                       " `hora` time DEFAULT NULL, " +
                                       " `informe` varchar(500) DEFAULT NULL, " +
                                       " `tipo` int(1) DEFAULT NULL, " +
                                       " `id_mascota` int(11) NOT NULL, " +
                                       " `id_cliente` int(11) NOT NULL, " +
                                       " `estado` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0)Cancelado 1)Acudido 2)Pendiente 3)Avisado', " +
                                       " `id_empleado` int(11) NOT NULL, " +
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