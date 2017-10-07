package com.clinica.veterinaria.provincias;

/**
 * Event DAO.
 * 
 * 
 */
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProvinciaDAO extends ManagerConexion{	
	
	
	
    public List<Provincia> findAll() {
        List<Provincia> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("select * from zk_provincia ORDER BY 2");

            // fetch all events from database
            Provincia prod;

            while (rs.next()) {
                    prod = new Provincia();
                    prod.setId (rs.getInt(1));
                    prod.setProvincia (rs.getString(2));
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

    public List<Provincia> findAll(String consulta) {
        List<Provincia> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);

            // fetch all events from database
            Provincia prod;

            while (rs.next()) {
                    prod = new Provincia();
                    prod.setId (rs.getInt(1));
                    prod.setProvincia (rs.getString(2));
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
            ResultSet rs = stmt.executeQuery("select * from zk_provincia ORDER BY 2");

            // fetch all events from database
            Provincia prod;

            while (rs.next()) {
                    prod = new Provincia();
                    prod.setId (rs.getInt(1));
                    prod.setProvincia (rs.getString(2));
                    allEvents.add(prod.getProvincia());
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

    public boolean delete(Provincia prod) {
            return execute("delete from zk_provincia where id = '" + prod.getId() + "'");
    }

    public boolean insert(Provincia prod) {
            return execute("insert into zk_provincia(provincia)" +
                            "values ('" + prod.getProvincia()    +   "')");
    }

    public boolean update(Provincia prod) {
        return execute("UPDATE zk_provincia SET provincia = '" + prod.getProvincia() + "' WHERE id = "  + prod.getId() );
    }
	
	
}