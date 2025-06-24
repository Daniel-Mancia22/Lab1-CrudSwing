package Controlador;

import Modelo.PersonaDAO;
import Modelo.persona;
import Vista.vista;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class controlador implements ActionListener{
    PersonaDAO dao = new PersonaDAO();
    persona p = new persona();
    
    vista v = new vista(); //Incluiremos el Jframe
    DefaultTableModel modelo = new DefaultTableModel();
            
    public controlador(vista v){
        this.v = v; 
        this.v.btnListar.addActionListener(this); 
        this.v.btnguardar.addActionListener(this);
        this.v.btneditar.addActionListener(this);
        this.v.btnlisto.addActionListener(this);
        this.v.btneliminar.addActionListener(this);
        this.v.btnbuscar.addActionListener(this);
        this.v.btncerrar.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == v.btnListar){
            Listar(v.tabla);
        }

        if(e.getSource() == v.btnguardar){
            agregar();
        }
        
        if(e.getSource() == v.btneditar){
           SeleccionarFilaEditar();
        }
        
        if(e.getSource() == v.btnlisto){
            actualizar();
        }
        
        if(e.getSource() == v.btneliminar){
            eliminar(v.tabla);
        }
        
        if(e.getSource() == v.btnbuscar){
            BuscarFiltro(v.txtfiltrobuscar.getText());
        }
        
        if (e.getSource() == v.btncerrar){ 
        System.exit(0);
        }
    }
    
    public void Listar(JTable tabla){
        modelo = (DefaultTableModel)tabla.getModel();
        List<persona>lista = dao.listar();
        Object[] object = new Object[4];//Se crea un arreglo para almacenar los datos
        
        //for para recorrer la lista persona
        for(int i = 0; i< lista.size(); i++){
        object[0] = lista.get(i).getId(); //Obtenemos el id y se asigna en la posicion del areglo
        object[1] = lista.get(i).getNombre();
        object[2] = lista.get(i).getCorreo();
        object[3] = lista.get(i).getTelefono();
        modelo.addRow(object); //Agregamos al table los datos 
        }
    }
    
    public void agregar() {
        if (!validarCamposLlenos()) { //Validar que los campos estén llenos antes de agregar
            return;
        }

        //Obtener los datos de los campos de texto
        String nombre = v.txtnombre.getText();
        String correo = v.txtcorreo.getText();
        String telefono = v.txttelefono.getText();

        //Establecer los valores en el objeto persona
        p.setNombre(nombre);
        p.setCorreo(correo);
        p.setTelefono(telefono);

        int res = dao.Agregar(p);  //Llamar al método agregar del DAO

        if (res == 1) {
            LimpiarCajas();
            modelo.setRowCount(0); //Limpia la lista anterior 
            Listar(v.tabla); //LLena la tabla con las actualizaciones
            JOptionPane.showMessageDialog(v, "Se Agregó con éxito...");
        } else {
            JOptionPane.showMessageDialog(v, "Error al agregar la persona.");
        }
    }

    public boolean validarCamposLlenos() {
        if (v.txtnombre.getText().trim().isEmpty() || 
            v.txtcorreo.getText().trim().isEmpty() || 
            v.txttelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(v, "Todos los campos son obligatorios.");
            return false;  // Indica que los campos no están llenos
        }
        return true;  // Indica que todos los campos están llenos
    }
    
    public boolean valirCampoBusqueda(){
        if (v.txtfiltrobuscar.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(v, "Ingrese el Nombre, Correo o Telefono de la persona que desea buscar...");
            return false;  // Indica que el campo no está lleno
        }
        return true;  // Indica que el campo este lleno
    }
    
    public void LimpiarCajas(){
        v.txtid.setText("");
        v.txtnombre.setText("");
        v.txtcorreo.setText("");
        v.txttelefono.setText("");
    }
 
    public void actualizar() {
        int filaS = v.tabla.getSelectedRow(); //fila seleccionada

        if (filaS == -1) { //Ver si eligio una fila
            JOptionPane.showMessageDialog(v, "Debe seleccionar una persona para Actualizarla.");
            return;
        }
        // Obtener los datos de la fila seleccionada
        String id = v.tabla.getValueAt(filaS, 0).toString();  //ID de la persona seleccionada
        String nombre = v.txtnombre.getText();
        String correo = v.txtcorreo.getText();
        String telefono = v.txttelefono.getText();

        // Establecer los valores en el objeto persona
        p.setId(Integer.parseInt(id));
        p.setNombre(nombre);
        p.setCorreo(correo);
        p.setTelefono(telefono);
    
        // Confirmar la actualización
        int res = JOptionPane.showConfirmDialog(v, "¿Está seguro de actualizar esta persona?", "Confirmar", JOptionPane.YES_NO_OPTION);
        int resultado = dao.Actualizar(p);
        if (res == JOptionPane.YES_OPTION) { //Si la respuesta es "Sí" (valor 0)
                JOptionPane.showMessageDialog(v, "Persona actualizada exitosamente.");
                LimpiarCajas();
                modelo.setRowCount(0); 
                Listar(v.tabla);
        } else {
            JOptionPane.showMessageDialog(v, "Actualización cancelada.");
            LimpiarCajas();
        }
    }
    
    public void SeleccionarFilaEditar(){
        
        int filaS = v.tabla.getSelectedRow();
        if (filaS == -1) {
        JOptionPane.showMessageDialog(v, "Debe seleccionar una persona para Editarla.");
        return;
        }
        v.MandarDatosParaCajas();
        v.txtnombre.setEnabled(true);
        v.txtcorreo.setEnabled(true);
        v.txttelefono.setEnabled(true);
    }
    
    public void eliminar(JTable tabla) {
        int filaS = tabla.getSelectedRow();

        if (filaS == -1) {//Verificamos si hay una fila seleccionada
            JOptionPane.showMessageDialog(v, "Debe seleccionar una persona para eliminar.");
            return;
        }   

        int res = JOptionPane.showConfirmDialog(v, "¿Está seguro de eliminar esta persona?", "Confirmar", JOptionPane.YES_NO_OPTION);
        int id = Integer.parseInt(tabla.getValueAt(filaS, 0).toString());
        if (res == JOptionPane.YES_OPTION) {
            dao.Eliminar(id);
            JOptionPane.showMessageDialog(v, "Persona eliminada exitosamente.");
            LimpiarCajas();
            modelo.setRowCount(0); 
            Listar(v.tabla);
            v.txtid.setEnabled(true);
            v.txtnombre.setEnabled(true);
            v.txtcorreo.setEnabled(true);
            v.txttelefono.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(v, "Eliminacion cancelada.");
            LimpiarCajas();
        }
    }
    
    public void BuscarFiltro(String buscar) {
        
        if (!valirCampoBusqueda()) { //Validar que los campos estén llenos antes de agregar
            return;
        }
    // Llamar al método buscarEmpleado y obtener el modelo actualizado
    DefaultTableModel modelo = dao.buscarEmpleado(buscar);
    v.tabla.setModel(modelo);

    // Verificar si hay algún resultado
    if (modelo.getRowCount() > 0) {
        // Obtener los datos del empleado encontrado
        String id = (String) modelo.getValueAt(0, 0);  
        String nombre = (String) modelo.getValueAt(0, 1);  
        String correo = (String) modelo.getValueAt(0, 2);  
        String telefono = (String) modelo.getValueAt(0, 3);  
      
        // Mostrar los datos en las cajas de texto
        v.txtid.setText(id);
        v.txtnombre.setText(nombre);
        v.txtcorreo.setText(correo);
        v.txttelefono.setText(telefono);

        // Deshabilitar las cajas de texto para que no se puedan editar
        v.txtid.setEnabled(false);
        v.txtnombre.setEnabled(false);
        v.txtcorreo.setEnabled(false);
        v.txttelefono.setEnabled(false);

        v.txtid.setDisabledTextColor(Color.BLACK);  // Cambiar color negro
        v.txtnombre.setDisabledTextColor(Color.BLACK);
        v.txtcorreo.setDisabledTextColor(Color.BLACK);
        v.txttelefono.setDisabledTextColor(Color.BLACK);
    } else {
        // Limpiar los campos si no se encuentra ningún empleado
        v.txtid.setText("");
        v.txtnombre.setText("");
        v.txtcorreo.setText("");
        v.txttelefono.setText("");
        // Limpia los demás campos

        // Habilitar las cajas de texto en caso de que quieras hacer otra búsqueda
        v.txtid.setEnabled(true);
        v.txtnombre.setEnabled(true);
        v.txtcorreo.setEnabled(true);
        v.txttelefono.setEnabled(true);
        }
    }
}