package org.ludwinxocoy.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.ludwinxocoy.bean.*;
import org.ludwinxocoy.db.Conexion;
import org.ludwinxocoy.system.Principal;

public class MenuFacturaController implements Initializable {

    private Principal escenarioPrincipal;


    @FXML
    private TableColumn colNumeroFactura;

    @FXML
    private TableColumn colEstado;

    @FXML
    private TableColumn colTotalFactura;

    @FXML
    private TableColumn colFechaFactura;

    @FXML
    private TableColumn colCodigoCliente;

    @FXML
    private TableColumn colEmpleado;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;
    @FXML
    
    private Button btnRegresar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnReporte;
    
    @FXML
    private TableView tblFactura;

    @FXML
    private TextField txtNumeroFactura;

    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtTotalFactura;

    @FXML
    private TextField txtFechaFactura;

    @FXML
    private ComboBox cmbCodigoCliente;

    @FXML
    private ComboBox cmbEmpleado;

    
    private ObservableList<Factura> listarFacturas;
    private ObservableList<Factura> buscarFacturas;
    private ObservableList<Clientes> listarClientes;
    private ObservableList<Empleados> listarEmpleados;

    
    private enum operaciones {
        ELIMINAR, EDITAR, CANCELAR, ACTUALIZAR, NINGUNO
    }
    
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;

   
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        cargarDatos();
        cmbCodigoCliente.setItems(getClientes());
        cmbEmpleado.setItems(getEmpleados());
    }
    
    public ObservableList<Factura> getFacturas() {
        ArrayList<Factura> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_listarFactura();");
            resultado = p.executeQuery();
            while (resultado.next()) {
                lista.add(new Factura(resultado.getInt("numeroFactura"),
                        resultado.getString("estado"),
                        resultado.getDouble("totalFactura"),
                        resultado.getDate("fechaFactura"),
                        resultado.getInt("clienteID"),
                        resultado.getInt("codigoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarFacturas = FXCollections.observableList(lista);
    }

    
    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarClientes();");
            resultado = procedimiento.executeQuery();
            
            while (resultado.next()) {
                lista.add(new Clientes(resultado.getInt("clienteID"),
                        resultado.getString("nombreClientes"),
                        resultado.getString("apellidosClientes"),
                        resultado.getString("direccionClientes"),
                        resultado.getString("NIT"),
                        resultado.getString("telefonoClientes"),
                        resultado.getString("correoClientes")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarClientes = FXCollections.observableList(lista);
    }

    
    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_listarEmpleados();");
            resultado = p.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleados(resultado.getInt("codigoEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getDouble("sueldo"),
                        resultado.getString("direccion"),
                        resultado.getString("turno"),
                        resultado.getInt("codigoCargoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarEmpleados = FXCollections.observableList(lista);
    }

    public void cargarDatos() {
        tblFactura.setItems(getFacturas());
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotalFactura.setCellValueFactory(new PropertyValueFactory<>("totalFactura"));
        colFechaFactura.setCellValueFactory(new PropertyValueFactory<>("fechaFactura"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<>("clienteID"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
    }

    public void desactivarControles() {
        txtNumeroFactura.setEditable(false);
        txtEstado.setEditable(false);
        txtTotalFactura.setEditable(false);
        txtFechaFactura.setEditable(false);
        cmbCodigoCliente.setDisable(true);
        cmbEmpleado.setDisable(true);
    }

    public void activarControles() {
        txtNumeroFactura.setEditable(true);
        txtEstado.setEditable(true);
        txtTotalFactura.setEditable(true);
        txtFechaFactura.setEditable(true);
        cmbCodigoCliente.setDisable(false);
        cmbEmpleado.setDisable(false);
    }

    public void limpiarControles() {
        txtNumeroFactura.clear();
        txtEstado.clear();
        txtTotalFactura.clear();
        txtFechaFactura.clear();
        tblFactura.getSelectionModel().getSelectedItem();
        cmbCodigoCliente.getSelectionModel().getSelectedItem();
        cmbEmpleado.getSelectionModel().getSelectedItem();
    }

    
    public void guardar() {
        
        
        Factura registro = new Factura();
        registro.setNumeroFactura(Integer.parseInt(txtNumeroFactura.getText()));
        registro.setEstado(txtEstado.getText());
        registro.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
        registro.setFechaFactura(Date.valueOf(txtFechaFactura.getText()));
        registro.setClienteID(((Clientes) cmbCodigoCliente.getSelectionModel().getSelectedItem()).getclienteID());
        registro.setCodigoEmpleado(((Empleados) cmbEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_agregarFactura(?,?,?,?,?,?);");
           
            p.setInt(1, registro.getNumeroFactura());
            p.setString(2, registro.getEstado());
            p.setDouble(3, registro.getTotalFactura());
            p.setDate(4, registro.getFechaFactura());
            p.setInt(5, registro.getClienteID());
            p.setInt(6, registro.getCodigoEmpleado());
            p.execute();
            listarFacturas.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
           
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }


    public Clientes buscarCliente(int codigoCliente) {
        Clientes resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall(" call sp_buscarClientes(?);");
            p.setInt(1, codigoCliente);
            ResultSet registro = p.executeQuery();
            while (registro.next()) {
                resultado = new Clientes(registro.getInt("ClienteID"),
                        registro.getString("nombreClientes"),
                        registro.getString("apellidosClientes"),
                        registro.getString("direccionClientes"),
                        registro.getString("NIT"),
                        registro.getString("telefonoClientes"),
                        registro.getString("correoClientes")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    public Empleados buscarEmpleados(int codigoEmpleado) {
        Empleados resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_buscarEmpleados(?);");
            p.setInt(1, codigoEmpleado);
            ResultSet registro = p.executeQuery();
            while (registro.next()) {
                resultado = new Empleados(registro.getInt("codigoEmpleado"),
                        registro.getString("nombresEmpleado"),
                        registro.getString("apellidosEmpleado"),
                        registro.getDouble("sueldo"),
                        registro.getString("direccion"),
                        registro.getString("turno"),
                        registro.getInt("codigoCargoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void seleccionarElementos() {
        Factura compraSeleccionada = (Factura) tblFactura.getSelectionModel().getSelectedItem();
        if (compraSeleccionada != null) {
            txtNumeroFactura.setText(String.valueOf(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
            txtEstado.setText(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getEstado());
            txtTotalFactura.setText((String.valueOf(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getTotalFactura())));
            txtFechaFactura.setText(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getFechaFactura().toString());
            cmbCodigoCliente.getSelectionModel().select(buscarCliente(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getClienteID()));
            cmbEmpleado.getSelectionModel().select(buscarEmpleados(((Factura) tblFactura.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        }
    }

   
    public void eliminar() {
        switch (tipoDeOperaciones) {
           
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
         
            default:
                if (tblFactura.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminación del registro", "Eliminar Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarFactura(?);");
                            procedimiento.setInt(1, ((Factura) tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
                            procedimiento.execute();
                            listarFacturas.remove(tblFactura.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Registro para Eliminar");
                }
                break;
        }
    }

    
    public void actualizar() {
        try {
            
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarFactura(?,?,?,?,?,?);");
            Factura registro = (Factura) tblFactura.getSelectionModel().getSelectedItem();

       
            
            
            registro.setNumeroFactura(Integer.parseInt(txtNumeroFactura.getText()));
            registro.setEstado(txtEstado.getText());
            registro.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
            registro.setFechaFactura(Date.valueOf(txtFechaFactura.getText()));
            registro.setClienteID(((Clientes) cmbCodigoCliente.getSelectionModel().getSelectedItem()).getclienteID());
            registro.setCodigoEmpleado(((Empleados) cmbEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());

            
            p.setInt(1, registro.getNumeroFactura());
            p.setString(2, registro.getEstado());
            p.setDouble(3, registro.getTotalFactura());
            p.setDate(4, registro.getFechaFactura());
            p.setInt(5, registro.getClienteID());
            p.setInt(6, registro.getCodigoEmpleado());
            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                
                if (tblFactura.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtNumeroFactura.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar una Factura para Actualizar");
                }
                break;
            case ACTUALIZAR:
               
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                desactivarControles();
                limpiarControles();
                cargarDatos();
                break;
        }
    }

  
    public void reportes() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);

                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }


    @FXML
    public void handleButtonAction(ActionEvent event) {
        
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();

        }
    }

 

}
