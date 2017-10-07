package com.clinica.veterinaria.mascotas;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;


public class DataSourceMascota extends ManagerConexion{

    public DataSourceMascota() {
    }
    

    private DataSourceMascota(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
                    //stmt.executeUpdate("drop table zk_mascota if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_mascota` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `chip` varchar(20) DEFAULT NULL, " +
                                       " `nombre` varchar(50) NOT NULL, " +
                                       " `sexo` varchar(6) DEFAULT NULL COMMENT '0)Macho 1)Hembra', " +
                                       " `fecha_nac` date NOT NULL, " +
                                       " `fecha_def` date DEFAULT NULL, " +
                                       " `peso` float DEFAULT NULL, " +
                                       " `altura` float DEFAULT NULL, " +
                                       " `observaciones` varchar(250) DEFAULT NULL, " +
                                       " `especie` varchar(100) DEFAULT NULL, " +
                                       " `pelo` varchar(100) DEFAULT NULL COMMENT '1)Corto 2)Duro 3)Largo 4)Medio 5)Medio-Largo 6)Propio 7)Rizado', " +
                                       " `raza` varchar(100) DEFAULT NULL, " +
                                       " `fecha_alta` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                       " `fecha_baja` datetime DEFAULT NULL, " +
                                       " `id_cliente` int(11) NOT NULL, " +
                                       " PRIMARY KEY (`id`), " +
                                       " UNIQUE KEY `chip` (`chip`) " +
                                       ") ENGINE=InnoDB  DEFAULT CHARSET=utf8;");
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }


}