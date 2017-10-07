package com.clinica.veterinaria.provincias;


import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataSourceProvincia extends ManagerConexion{

    public DataSourceProvincia() {
    }
	
    
    
    private DataSourceProvincia(String asd) {
            // drop the table if it exists
            try {
                    Statement stmt = this.getStatement();
                    //stmt.executeUpdate("drop table zk_provincia if exists");
                    //stmt.executeUpdate("create table event (evtid VARCHAR(37) NOT NULL PRIMARY KEY, name VARCHAR(100), priority INTEGER, evtdate TIMESTAMP)");
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `zk_provincia` ( " +
                                       " `id` int(11) NOT NULL AUTO_INCREMENT, " +
                                       " `provincia` varchar(200) NOT NULL, " +
                                       " PRIMARY KEY (`id`) " +
                                       ") ENGINE=InnoDB  DEFAULT CHARSET=utf8;");
                    
                    ResultSet rs = stmt.executeQuery("SELECT * FROM zk_provincia");
                    
                    rs.last();
                    
                    if(rs.getRow() == 0){
                        
                        stmt.executeUpdate("INSERT INTO `zk_provincia` (`id`, `provincia`) VALUES" +
                                       "  (1, 'Buenos Aires')," +
                                       "  (2, 'Santa Fe')," +
                                       "  (3, 'Entre Rios')," +
                                       "  (4, 'Corrientes')," +
                                       "  (5, 'La Pampa')," +
                                       "  (6, 'Tucuman')," +
                                       "  (7, 'Santa Cruz')," +
                                       "  (8, 'Misiones')," +
                                       "  (9, 'Tierra del Fuego')," +
                                       "  (10, 'Neuquén')," +
                                       "  (11, 'Mendoza')," +
                                       "  (12, 'Formosa')," +
                                       "  (13, 'Chaco')," +
                                       "  (14, 'Córdoba')," +
                                       "  (15, 'Jujuy')," +
                                       "  (16, 'San Juan')," +
                                       "  (17, 'La Rioja')," +
                                       "  (18, 'Catamarca')," +
                                       "  (19, 'Santiago del Estero')," +
                                       "  (20, 'Salta')," +
                                       "  (21, 'Rio Negro')," +
                                       "  (22, 'Chubut')," +
                                       "  (23, 'San Luis');");
                    }
                    stmt.close();
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                    this.close();
            }
    }


}