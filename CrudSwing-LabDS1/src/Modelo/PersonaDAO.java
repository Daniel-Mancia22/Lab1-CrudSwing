package Modelo;

import Vista.vista;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.sql.Connection;
import javax.swing.table.DefaultTableModel;

public class PersonaDAO {
    Conexion Conectar = new Conexion(); // instancia de la clase Conexion
    Connection Cx; 
    PreparedStatement ps; 
    ResultSet rs;
    vista v = new vista();  // Instancia del formulario Vista
    // Se inicializa el dafaulttablemodel basado en el modelo de la tabla del formulario vista
    DefaultTableModel modelo = (DefaultTableModel) v.tabla.getModel();

    public List listar(){
        List<persona>datos = new ArrayList<>();
        
        String sql = "select id, nombre, correo, telefono from Personas";
        try{
            Cx = Conectar.getConnection();
            ps = Cx.prepareStatement(sql);
            rs = ps.executeQuery();

            //Ejecucion del bucle para iterar cada dato e la tabla
            while(rs.next()){
                persona p = new persona(); //Intancia de la clase persona
                //utilizamos los set y get para asignar los valores obtenidos de la consulta datos
                p.setId(rs.getInt(1));
                p.setNombre(rs.getString(2));
                p.setCorreo(rs.getString(3));
                p.setTelefono(rs.getString(4));
                datos.add(p); //Agregamos el ojeto persona a la lista de datos
            }
        }catch(Exception e){
            System.out.println("Error: " + e); 
        }
    return datos;
    } 
    
    public int Agregar(persona p) {
    String sql = "insert into Personas (nombre, correo, telefono) values (?, ?, ?)";
    try {
        Cx = Conectar.getConnection();
        ps = Cx.prepareStatement(sql);
        //Establecemos el orden de los valores
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getCorreo());
        ps.setString(3, p.getTelefono());
        ps.executeUpdate();
    } catch (Exception e) {
        System.out.println("Error: " + e);
        }
    return 1;
    }
    
    public int Actualizar(persona p) {
    String sql = "update Personas set nombre=?, correo=?, telefono=? where id=?";
    try {
        Cx = Conectar.getConnection();
        ps = Cx.prepareStatement(sql); //Iniciamos la consulata
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getCorreo());
        ps.setString(3, p.getTelefono());
        ps.setInt(4, p.getId());
        ps.executeUpdate();
    } catch (Exception e) {
        System.out.println("Error: " + e);
        }
    return 1;
    }
    
    public void Eliminar(int id) {
    String sql = "delete from Personas where id=?";
    try {
        Cx = Conectar.getConnection();
        ps = Cx.prepareStatement(sql);  //Iniciamos la consulata
        ps.setInt(1, id);
        ps.executeUpdate();
    } catch (Exception e) {
        System.out.println("Error: " + e);
        }
    }
    
    public DefaultTableModel buscarEmpleado(String buscar) {
    String[] nombreColumna = {"id", "nombre", "correo", "telefono"};
    DefaultTableModel modelo = new DefaultTableModel(null, nombreColumna);

    String sql = "select *from Personas where nombre like ? or correo like ? or telefono like ?";
    try {
        Cx = Conectar.getConnection();
        ps = Cx.prepareStatement(sql);

        // Asignar los parámetros de búsqueda con comodines
        ps.setString(1, "%" + buscar + "%");
        ps.setString(2, "%" + buscar + "%");
        ps.setString(3, "%" + buscar + "%");
        rs = ps.executeQuery();

        // Iterar sobre los resultados del ResultSet
        while (rs.next()) {
            String[] registros = new String[4];
            registros[0] = rs.getString("id");
            registros[1] = rs.getString("nombre");
            registros[2] = rs.getString("correo");
            registros[3] = rs.getString("telefono");
            modelo.addRow(registros); // Agregar fila al modelo
        }
    } catch (Exception e) {
        System.out.println("Error: " + e);
        }
    return modelo;
    }
}