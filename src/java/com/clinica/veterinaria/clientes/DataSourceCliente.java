package com.clinica.veterinaria.clientes;


import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataSourceCliente extends ManagerConexion {

    public DataSourceCliente() {
    }
    
    
    private DataSourceCliente(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
                    //stmt.executeUpdate("drop table zk_cliente if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_cliente` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `nombre` varchar(150) NOT NULL, " +
                                       " `apellidos` varchar(150) NOT NULL, " +
                                       " `nif` varchar(10) NOT NULL, " +
                                       " `direccion` varchar(250) NOT NULL, " +
                                       " `ciudad` INT(11) NOT NULL, " +
                                       " `provincia` INT(11) NOT NULL, " +
                                       " `telefono` int(9) NOT NULL, " +
                                       " `telefono2` int(9) DEFAULT NULL, " +
                                       " `email` varchar(200) DEFAULT NULL, " +
                                       " `fecha_alta` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                       " `codigopostal` int(5) DEFAULT NULL, " +
                                       " PRIMARY KEY (`id`), " +
                                       " UNIQUE KEY `nif` (`nif`) " +
                                     " ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;");
                    
                    ResultSet rs = stmt.executeQuery("SELECT * FROM zk_cliente");
                    rs.last();
                    
                    if(rs.getRow() == 0){
                    
                        stmt.executeUpdate("INSERT INTO `zk_cliente` (`id`, `nombre`, `apellidos`, "
                                                              + "`nif`, `direccion`, `ciudad`, "
                                                              + "`provincia`, `telefono`, `telefono2`, "
                                                              + "`email`) VALUES "
                                            + "(1, 'Consumidor', 'Final', '00000000A', 'Anónimo', '1785', '11', 900000000, "
                                            + "NULL, 'ventarapida@ventarapida.com');");
                    }
                    
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }

   
}