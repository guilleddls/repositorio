
package com.clinica.veterinaria.facturas;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class DataSourceFactura extends ManagerConexion{

    public DataSourceFactura() {
    }
    
    

    private DataSourceFactura(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
//                    stmt.executeUpdate("drop table zk_raza if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_factura` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `numero` int(11) NOT NULL, " +
                                       " `cliente` int(11) NOT NULL, " +
                                       " `empleado` int(11) NOT NULL, " +
                                       " `factura` varchar(300) NOT NULL, " +
                                       " `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
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