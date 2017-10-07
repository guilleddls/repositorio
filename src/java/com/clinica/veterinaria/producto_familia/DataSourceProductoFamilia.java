package com.clinica.veterinaria.producto_familia;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class DataSourceProductoFamilia extends ManagerConexion{

    public DataSourceProductoFamilia() {
    }
       	
    

    private DataSourceProductoFamilia(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_raza if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_producto_familia` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `nombre` varchar(200) NOT NULL, " +
                                       " `descripcion` varchar(500) DEFAULT ' ', " +
                                       " `tratamiento` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1)Tratamiento 0)Otro', " +
                                       " PRIMARY KEY (`id`), " +
                                       " UNIQUE KEY `nombre` (`nombre`)" +
                                       ") ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;");
                    
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }

  
}