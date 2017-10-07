package app.calendario;

import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




public class EventoDAO {
    
    private ManagerConexion mc;
     
    private final static String TABLA ="zk_evento";
    private final static String selectEvento = "SELECT * FROM " + TABLA;
    private final static String insertEvento = "INSERT INTO " + TABLA + "(news_item, date_begin, date_end, title, content, header_color, content_color, isLocked) values (%1$d, %2$d, %3$d, '%4$s', '%5$s', '%6$s', '%7$s', %8$b)";
    private final static String updateEvento = "UPDATE " + TABLA + " SET date_begin = %1$d, date_end = %2$d, title = '%3$s', content = '%4$s', header_color = '%5$s', content_color = '%6$s', isLocked = %7$b WHERE news_item = %8$d";
    private final static String deleteEvento = "DELETE FROM " + TABLA + " WHERE news_item = %1$d";

    private static String stringSelectAll() {
        return selectEvento;
    }

    private static String stringInsert(Evento evt) {		
        Long begin = evt.getBeginDate().getTime();
        Long end = evt.getEndDate().getTime();
        Boolean locked = evt.isLocked();

        Object[] args = {evt.getNews_item(), begin, end, evt.getTitle(), evt.getContent(), evt.getHeaderColor(), evt.getContentColor(), locked};
        return String.format(insertEvento, args);
    }

    private static String stringUpdate(Evento evt) {
        Long begin = evt.getBeginDate().getTime();
        Long end = evt.getEndDate().getTime();
        Boolean locked = evt.isLocked();
        Integer newsItem = evt.getNews_item();

        Object[] args = {begin, end, evt.getTitle(), evt.getContent(), evt.getHeaderColor(), evt.getContentColor(), locked, newsItem};
        return String.format(updateEvento, args);
    }

    private static String stringDelete(int id) {
        Integer boxedID = id;
        Object[] args = {boxedID};
        return String.format(deleteEvento, args);
    }
    

    public EventoDAO() {
        

    }

    public List selectAll() {
        Statement stmt = null;

        List allObjects = new ArrayList();


        try {
            mc = new ManagerConexion();

            stmt = mc.getStatement();
            ResultSet rs = stmt.executeQuery(stringSelectAll());

            Evento evento;

            while (rs.next()) {

                //System.out.println(rs.toString());

                evento = new Evento();

                Date fecha = new Date();

                evento.setNews_item(rs.getInt(1));
                fecha.setTime(rs.getLong(2));
                
                evento.setBeginDate((Date)fecha.clone());
                fecha.setTime(rs.getLong(3));
                evento.setEndDate((Date)fecha.clone());

                evento.setTitle(rs.getString(4));
                evento.setContent(rs.getString(5));
                evento.setHeaderColor(rs.getString(6));
                evento.setContentColor(rs.getString(7));
                evento.setLocked(rs.getBoolean(8));

                allObjects.add(evento);
            }

        } catch (SQLException e) {
                e.printStackTrace(System.err);
        }finally{
            mc.closeStatement(stmt);
            mc.close();
        }

        return allObjects;
    }

    public boolean update(Evento ni) {
        boolean result = false;

        Statement stmt = null;
        try {
            this.mc = new ManagerConexion();
            stmt = mc.getStatement();//conn.createStatement();

            //execute the query
            if (stmt.executeUpdate(stringUpdate(ni)) > 0) result = true;
                
        } catch (SQLException ex) {

                ex.printStackTrace(System.err);
                System.out.println(stringInsert(ni));
        }finally {
            mc.closeStatement(stmt);
            mc.close();
        }

        return result;		
    }

     public int lastID(){
            int last_id = 0;
            Statement stmt = null;
            try{
                mc = new ManagerConexion();
                stmt = mc.getStatement();//conn.createStatement();
                
        
                ResultSet rs = stmt.executeQuery("SELECT IFNULL(MAX(id),0) FROM zk_evento");
                rs.next();
                last_id = rs.getInt(1);
               
            } catch (SQLException e) {
			e.printStackTrace(System.err);
		} finally {
                
            mc.closeStatement(stmt);
            mc.close();
        
		
		}
            return last_id+1;

        }
    
    public boolean insert(Evento ni) {
        boolean result = false;

        Statement stmt = null;

        //List allObjects = new ArrayList();

        try {
            mc = new ManagerConexion();
            stmt = mc.getStatement();//conn.createStatement();
            ni.setNews_item(lastID());
            //execute the query
            System.out.println(stringInsert(ni));
            if (stmt.executeUpdate(stringInsert(ni)) > 0)
                result = true;
        } catch (SQLException ex) {

                ex.printStackTrace(System.err);
                System.out.println(stringInsert(ni));
        }finally {
            mc.closeStatement(stmt);
            mc.close();
        }

        return result;
    }

    public boolean delete(Evento ni) {
        boolean result = false;
        Statement stmt = null;
        try {
            mc = new ManagerConexion();
            stmt = mc.getStatement();//conn.createStatement();

            //execute the query
            if (stmt.executeUpdate(stringDelete(ni.getNews_item())) > 0)
                    result = true;

        } catch (SQLException ex) {
                ex.printStackTrace(System.err);
        }finally {
            mc.closeStatement(stmt);
            mc.close();
        }

        return result;

    }
}