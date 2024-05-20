package org.ludwinxocoy.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.ludwinxocoy.bean.Proveedores;
import org.ludwinxocoy.bean.TelefonoProveedor;
import org.ludwinxocoy.system.Principal;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.ludwinxocoy.db.Conexion;

public class MenuTelefonoProveedorController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    private TableView tblTelefonoProveedor;

    @FXML
    private TableColumn colTelefono;

    @FXML
    private TableColumn colNumeroPrincipal;

    @FXML
    private TableColumn colNumeroSecundario;

    @FXML
    private TableColumn colObservaciones;

    @FXML
    private TableColumn colCodigoProveedor;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnRegresar;

    @FXML
    private ComboBox cmbCodigoProveedor;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtNumPrincipal;

    @FXML
    private TextField txtNumSecundario;

    @FXML
    private TextField txtObservaciones;

    private ObservableList<TelefonoProveedor> listarTelefonoProveedor;
    private ObservableList<Proveedores> listarProveedores;

    private enum operador {
        ELIMINAR, EDITAR, CANCELAR, ACTUALIZAR, NINGUNO
    }

    private operador tipoDeOperaciones = operador.NINGUNO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbCodigoProveedor.setItems(getProveedores());
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public ObservableList<TelefonoProveedor> getTelefonoProveedor() {
        ArrayList<TelefonoProveedor> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTelefonoProveedor;");
            resultado = p.executeQuery();
            while (resultado.next()) {
                lista.add(new TelefonoProveedor(resultado.getInt("codigoTelefonoProveedor"),
                        resultado.getString("numeroPrincipal"),
                        resultado.getString("numeroSecundario"),
                        resultado.getString("observaciones"),
                        resultado.getInt("codigoProveedor")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarTelefonoProveedor = FXCollections.observableList(lista);
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProveedor();");
            resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Proveedores(resultado.getInt("codigoProveedor"),
                        resultado.getString("NitProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("ContactoPrincipal"),
                        resultado.getString("paginaWeb")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarProveedores = FXCollections.observableList(lista);
    }

    public void guardar() {

        TelefonoProveedor registro = new TelefonoProveedor();
        registro.setCodigoTelefonoProveedor(Integer.parseInt(txtTelefono.getText()));
        registro.setNumeroPrincipal(txtNumPrincipal.getText());
        registro.setNumeroSecundario(txtNumSecundario.getText());
        registro.setObservaciones(txtObservaciones.getText());
        registro.setCodigoProveedor(((Proveedores) cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_agregarTelefonoProveedor(?,?,?,?,?);");

            p.setInt(1, registro.getCodigoTelefonoProveedor());
            p.setString(2, registro.getNumeroPrincipal());
            p.setString(3, registro.getNumeroSecundario());
            p.setString(4, registro.getObservaciones());
            p.setInt(5, registro.getCodigoProveedor());
            p.execute();
            listarTelefonoProveedor.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        try {

            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarTelefonoProveedor(?,?,?,?,?);");
            TelefonoProveedor registro = (TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem();

            registro.setCodigoTelefonoProveedor(Integer.parseInt(txtTelefono.getText()));
            registro.setNumeroPrincipal(txtNumPrincipal.getText());
            registro.setNumeroSecundario(txtNumSecundario.getText());
            registro.setObservaciones(txtObservaciones.getText());
            registro.setCodigoProveedor(((Proveedores) cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());

            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
            procedimiento.setString(2, registro.getNumeroPrincipal());
            procedimiento.setString(3, registro.getNumeroSecundario());
            procedimiento.setString(4, registro.getObservaciones());
            procedimiento.setInt(5, registro.getCodigoProveedor());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:

                if (tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtTelefono.setEditable(false);
                    tipoDeOperaciones = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Telefono para Actualizar");
                }
                break;
            case ACTUALIZAR:

                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operador.NINGUNO;
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
                tipoDeOperaciones = operador.NINGUNO;
                break;
        }
    }

    public void cargarDatos() {
        tblTelefonoProveedor.setItems(getTelefonoProveedor());
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("codigoTelefonoProveedor"));
        colNumeroPrincipal.setCellValueFactory(new PropertyValueFactory<>("numeroPrincipal"));
        colNumeroSecundario.setCellValueFactory(new PropertyValueFactory<>("numeroSecundario"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    public void activarControles() {
        txtTelefono.setEditable(true);
        txtNumPrincipal.setEditable(true);
        txtNumSecundario.setEditable(true);
        txtObservaciones.setEditable(true);
        cmbCodigoProveedor.setDisable(false);
    }

    public void desactivarControles() {
        txtTelefono.setEditable(false);
        txtNumPrincipal.setEditable(false);
        txtNumSecundario.setEditable(false);
        txtObservaciones.setEditable(false);
        cmbCodigoProveedor.setDisable(true);
    }

    public void limpiarControles() {
        txtTelefono.clear();
        txtNumPrincipal.clear();
        txtNumSecundario.clear();
        txtObservaciones.clear();
        tblTelefonoProveedor.getSelectionModel().getSelectedItem();
        cmbCodigoProveedor.getSelectionModel().getSelectedItem();
    }

    public void seleccionarElementos() {
        txtTelefono.setText(String.valueOf(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor()));
        txtNumPrincipal.setText(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroPrincipal());
        txtNumSecundario.setText(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroSecundario());
        txtObservaciones.setText(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getObservaciones());
        cmbCodigoProveedor.getSelectionModel().select(buscarProveedores(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
    }

    public Proveedores buscarProveedores(int codigoProveedor) {
        Proveedores resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_buscarProveedores(?);");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Proveedores(registro.getInt("codigoProveedor"),
                        registro.getString("NITProvedor"),
                        registro.getString("nombresProveedor"),
                        registro.getString("apellidosProveedor"),
                        registro.getString("direccionProveedor"),
                        registro.getString("razonSocial"),
                        registro.getString("contactoPrincipal"),
                        registro.getString("paginaWeb"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operador.ACTUALIZAR;
                break;

            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operador.NINGUNO;
                break;
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
                tipoDeOperaciones = operador.NINGUNO;
                break;

            default:
                if (tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminación del registro", "Eliminar Telefono Proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarTelefonoProveedor(?);");
                            procedimiento.setInt(1, ((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
                            procedimiento.execute();
                            listarTelefonoProveedor.remove(tblTelefonoProveedor.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Telefono para Eliminar");
                }
                break;
        }
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuProveedoresView();
        }
    }

}
