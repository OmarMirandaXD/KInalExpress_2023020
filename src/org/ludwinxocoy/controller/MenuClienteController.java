package org.ludwinxocoy.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.ludwinxocoy.bean.Clientes;
import org.ludwinxocoy.db.Conexion;
import org.ludwinxocoy.system.Principal;

public class MenuClienteController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Clientes> listaClientes;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    @FXML
    private Button btnRegresar;
    @FXML
    private ImageView imgAgregar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private Button btnAgregarCliente;
    @FXML
    private Button btnEliminarCliente;
    @FXML
    private Button btnReportesClientes;
    @FXML
    private Button btnEditarCliente;
    @FXML
    private TextField txtIdCliente;
    @FXML
    private TextField txtNit;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TableView tblClientes;
    @FXML
    private TableColumn colClienteC;
    @FXML
    private TableColumn colNitC;
    @FXML
    private TableColumn colNombreC;
    @FXML
    private TableColumn colApellidoC;
    @FXML
    private TableColumn colDireccionC;
    @FXML
    private TableColumn colTelefonoC;
    @FXML
    private TableColumn colCorreoC;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnAgregarCliente.setText("Guardar");
                btnEliminarCliente.setText("Cancelar");
                btnEditarCliente.setDisable(true);
                btnReportesClientes.setDisable(true);

                tipoDeOperaciones = operaciones.AGREGAR;
                break; // Agregar break aquí

            case AGREGAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarCliente.setText("Agregar");
                btnEliminarCliente.setText("Eliminar");
                btnEditarCliente.setDisable(false);
                btnReportesClientes.setDisable(false);
                tipoDeOperaciones = operaciones.AGREGAR;
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarCliente.setText("Agregar");
                btnEliminarCliente.setText("Eliminar");
                btnEditarCliente.setDisable(false);
                btnReportesClientes.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del registro",
                            "Eliminar Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarClientes(?)}");
                            procedimiento.setInt(1, ((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getclienteID());
                            procedimiento.execute();
                            listaClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Necesitas seleccionar un Cliente antes..");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    btnEditarCliente.setText("Actualizar");
                    btnReportesClientes.setText("Cancelar");
                    btnAgregarCliente.setDisable(true);
                    btnEliminarCliente.setDisable(true);
                    tipoDeOperaciones = operaciones.NINGUNO;
                    activarControles();
                    txtIdCliente.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un cliente para editar");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditarCliente.setText("Editar");
                btnReportesClientes.setText("Cancelar");
                btnAgregarCliente.setDisable(false);
                btnEliminarCliente.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_actualizar(?, ?, ?, ?, ?, ?, ?)}");
            Clientes registro = (Clientes) tblClientes.getSelectionModel().getSelectedItem();
            registro.setNIT(txtNit.getText());
            registro.setNombreClientes(txtNombre.getText());
            registro.setApellidosClientes(txtApellido.getText());
            registro.setDireccionClientes(txtDireccion.getText());
            registro.setTelefonoClientes(txtTelefono.getText());
            registro.setCorreoClientes(txtCorreo.getText());
            Procedimiento.setInt(1, registro.getclienteID());
            Procedimiento.setString(2, registro.getNIT());
            Procedimiento.setString(3, registro.getNombreClientes());
            Procedimiento.setString(4, registro.getApellidosClientes());
            Procedimiento.setString(5, registro.getDireccionClientes());
            Procedimiento.setString(6, registro.getTelefonoClientes());
            Procedimiento.setString(7, registro.getCorreoClientes());
            Procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelar() {

    }

    public void cargarDatos() {
        tblClientes.setItems(getClientes());
        colClienteC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("clienteID"));
        colNitC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("NIT"));
        colNombreC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreClientes"));
        colApellidoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidosClientes"));
        colDireccionC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionClientes"));
        colTelefonoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoClientes"));
        colCorreoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correoClientes"));
    }

    public void seleccionarElemento() {
        txtIdCliente.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getclienteID()));
        txtNit.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNIT());
        txtNombre.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNombreClientes());
        txtApellido.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getApellidosClientes());
        txtDireccion.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getDireccionClientes());
        txtTelefono.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getTelefonoClientes());
        txtCorreo.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getCorreoClientes());

    }

    public void desactivarControles() {
        txtIdCliente.setEditable(false);
        txtNit.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtCorreo.setEditable(false);
    }

    public void activarControles() {
        txtIdCliente.setEditable(true);
        txtNit.setEditable(true);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtCorreo.setEditable(true);
    }

    public void limpiarControles() {
        txtIdCliente.clear();
        txtNit.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtCorreo.clear();
    }

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Clientes(resultado.getInt("clienteID"),
                        resultado.getString("NIT"),
                        resultado.getString("nombreClientes"),
                        resultado.getString("apellidosClientes"),
                        resultado.getString("direccionClientes"),
                        resultado.getString("telefonoClientes"),
                        resultado.getString("correoClientes")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableList(lista);
    }

    public void guardar() {
        Clientes registro = new Clientes();
        registro.setClienteID(Integer.parseInt(txtIdCliente.getText()));
        registro.setNIT(txtNit.getText());
        registro.setNombreClientes(txtNombre.getText());
        registro.setApellidosClientes(txtApellido.getText());
        registro.setDireccionClientes(txtDireccion.getText());
        registro.setTelefonoClientes(txtTelefono.getText());
        registro.setCorreoClientes(txtCorreo.getText());
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarClientes(?,?,?,?,?,?,?)}");
            Procedimiento.setInt(1, registro.getclienteID());
            Procedimiento.setString(2, registro.getNIT());
            Procedimiento.setString(3, registro.getNombreClientes());
            Procedimiento.setString(4, registro.getApellidosClientes());
            Procedimiento.setString(5, registro.getDireccionClientes());
            Procedimiento.setString(6, registro.getTelefonoClientes());
            Procedimiento.setString(7, registro.getCorreoClientes());
            Procedimiento.execute();
            listaClientes.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

}
