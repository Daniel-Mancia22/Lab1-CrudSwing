package Modelo;

import java.sql.*;
import java.sql.Connection;

public class Conexion {
    Connection Cx;
    
    public Connection getConnection(){
        String url = "jdbc:mysql://localhost:3306/labds1";
        String usuario = "user-labds1";
        String clave = "labds123";
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Cx = DriverManager.getConnection(url,usuario, clave);
            System.out.println("Conectado.");
        }catch (Exception e){
            System.out.println("Error " + e);   
        }
        return Cx;
        }
    }    