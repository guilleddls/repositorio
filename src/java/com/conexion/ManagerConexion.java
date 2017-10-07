package com.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 
 */
public class ManagerConexion {
 
    
    private static final String url = "jdbc:mysql://127.6.211.2:3306/clinica";
    private static final String user = "admintLmN9KS";
    private static final String pwd = "q2v-Vd-GbnkG";

    
    
//    private static final String url = "jdbc:mysql://localhost:3306/clinica"; 
//    private static final String user = "root";
//    private static final String pwd = "";
//    
    private Connection conn = null;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
        }
    }

    public Statement getStatement() throws SQLException {       
        
        getConnection();
        
        return conn.createStatement();
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
                e.printStackTrace(System.err);
        }
    }
    
    public Connection getConnection() throws SQLException {
        if(conn == null) conn = DriverManager.getConnection(url, user, pwd);
        return conn;
    }
     
    public void closeStatement(Statement stmt){
        if(stmt != null){
            try {
                stmt.close();
               
            } catch (SQLException ex) {
                System.err.println("Error al cerrar statement: " + ex.getMessage());
            }
        }
    }
    
    public void closeResultSet(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
               
            } catch (SQLException ex) {
                System.err.println("Error al cerrar resultset: " + ex.getMessage());
            }
        }
    }
    
    
    public boolean execute(String sql) {
        
        try {
            Statement stmt = getStatement();
            stmt.execute(sql);
            closeStatement(stmt);
            return true;
        } catch (SQLException e) {
            System.err.println("Error en execute: " + e.getMessage());
            e.printStackTrace(System.err);
            return false;
        } finally {
            close();
        }
    }
}
