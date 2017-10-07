package com.clinica.veterinaria.especies;

import com.clinica.veterinaria.razas.Raza;
import com.clinica.veterinaria.razas.RazaDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class EspecieDAO extends ManagerConexion {
    
    public List<Especie> findAll() {
        List<Especie> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("select * from zk_especie");

            // fetch all events from database
            Especie especie;
            List<Raza> razalist = new RazaDAO().findAll();
            while (rs.next()) {
                especie = new Especie();
                especie.setId (rs.getInt(1));
                especie.setEspecie (rs.getString(2));



                for (Raza x : razalist) {
                    if (x.getEspecie().getId() == especie.getId()) {
                        especie.asignarRaza(x);
                    }
                }

                allEvents.add(especie);
            }

            closeResultSet(rs);
            closeStatement(stmt);
        } catch (SQLException e) {
                e.printStackTrace(System.err);
        } finally {
            close();
        }

        return allEvents;
    }

    public List<Especie> findAll(String consulta) {
        List<Especie> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);

            // fetch all events from database
            Especie prod;

            while (rs.next()) {
                prod = new Especie();
                prod.setId (rs.getInt(1));
                prod.setEspecie (rs.getString(2));
                allEvents.add(prod);
            }
            closeResultSet(rs);
            closeStatement(stmt);   

        } catch (SQLException e) {
                e.printStackTrace(System.err);
        } finally {
            close();
        }

        return allEvents;
    }

    public List<String> findAllString() {
        List<String> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("select * from zk_especie");

                // fetch all events from database
                Especie prod;

                while (rs.next()) {
                        prod = new Especie();
                        prod.setId (rs.getInt(1));
                        prod.setEspecie (rs.getString(2));
                        allEvents.add(prod.getEspecie());
                }
                closeResultSet(rs);
                closeStatement(stmt);
        } catch (SQLException e) {
                e.printStackTrace(System.err);
        } finally {
            close();
        }

        return allEvents;
    }

    public boolean delete(Especie prod) {
            return execute("delete from zk_especie where id = '" + prod.getId() + "'");
    }

    public boolean insert(Especie prod) {
            return execute("insert into zk_especie(especie)" +
                            "values ('" + prod.getEspecie()    +   "')");
    }

    public boolean update(Especie prod) {
        return execute("UPDATE zk_especie SET especie = '" + prod.getEspecie() + "' WHERE id = '"  + prod.getId() + "'");
    }
	
	
}