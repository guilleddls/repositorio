
package com.clinica.veterinaria.servicios;


import com.conexion.ManagerConexion;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class DataSourceServicio extends ManagerConexion{

    public DataSourceServicio() {
    }
    	
    
    
    private DataSourceServicio(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_servicio` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `codigo` varchar(9) NOT NULL, " +
                                       " `servicio` varchar(200) NOT NULL, " +
                                       " `descripcion` varchar(200) DEFAULT NULL, " +
                                       " `precio` float NOT NULL, " +
                                       " `id_iva` int(11) NOT NULL, " +
                                       " `id_familia` int(11) DEFAULT NULL, " +
                                       " PRIMARY KEY (`id`) " +
                                       ") ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;");
                    
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }


}