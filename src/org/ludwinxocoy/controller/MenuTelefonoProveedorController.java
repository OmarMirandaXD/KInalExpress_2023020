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

/**
 * FXML Controller class
 *
 * @author Devyn Orlando Tubac Gomez Carne: 2020247 Codigo Tecnico: IN5BM Fecha
 * de Creación: 10/04/2024 Fecha de Modificaciones: 19/05/2024
 */
public class MenuTelefonoProveedorController implements Initializable {

    private Main escenarioPrincipal;
    /**
     * ID de los elementos utilizados en la interfaz.
     */
    @FXML
    Button btnRegresar;
    @FXML
    private TableView tblTelefonoProveedor;

    @FXML
    private TableColumn colTelefono;

    @FXML
    private TableColumn colNumPrincipal;

    @FXML
    private TableColumn colNumSecundario;

    @FXML
    private TableColumn colObservaciones;

    @FXML
    private TableColumn colCodProveedor;

    @FXML
    private Button btnAgregar;

    @FXML
    private ImageView imgAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    private ImageView imgEditar;

    @FXML
    private Button btnReporte;

    @FXML
    private ImageView imgReporte;

    @FXML
    private TextField txtObservaciones;

    @FXML
    private ComboBox cmbCodProveedor;

    @FXML
    private TextField txtCodigoTelefono;

    @FXML
    private TextField txtNumPrincipal;

    @FXML
    private TextField txtNumSecundario;
    @FXML
    private TextField txtBuscar;

    /**
     * ObservableList para enlistar los datos.
     */
    private ObservableList<TelefonoProveedor> listarTelefonoProveedor;
    private ObservableList<Proveedores> listarProveedores;
    private ObservableList<TelefonoProveedor> buscarTelefonoProveedor;

    /**
     * Enumeradores para las operaciones que se utilizaran en el programa.
     */
    private enum operaciones {
        ELIMINAR, EDITAR, CANCELAR, ACTUALIZAR, NINGUNO
    }
    /**
     * Variable que indica el tipo de operación actual.
     */
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;

    /**
     * Getter y Setter
     */
    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    /**
     * Este metodo tiene la función de listar los datos, y a su vez prepara y
     * ejecuta el procedimiento de listar de la base de datos.
     *
     * @return La lista de Telefono Proveedor
     */
    public ObservableList<TelefonoProveedor> getTelefonoProveedor() {
        ArrayList<TelefonoProveedor> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_listarTelefonoProveedor;");
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

    /**
     * Este metodo tiene la función de listar los datos, y a su vez prepara y
     * ejecuta el procedimiento de listar de la base de datos.
     *
     * @return La lista de Proveedores
     */
    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_listarProveedores();");
            resultado = procedimiento.executeQuery();
            /**
             * El bucle while agrega los datos a la lista.
             */
            while (resultado.next()) {
                lista.add(new Proveedores(resultado.getInt("codigoProveedor"),
                        resultado.getString("NITProvedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("contactoPrincipal"),
                        resultado.getString("paginaWeb")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarProveedores = FXCollections.observableList(lista);
    }

    /**
     * Este metodo tiene la funcionalidad de guardar los datos, y a su vez
     * prepara y ejecuta el procedimiento agregar de la base de datos.
     */
    public void guardar() {
        /**
         * Crear un nuevo objeto y asigna los valores de los campos de entrada
         * de texto.
         */
        TelefonoProveedor registro = new TelefonoProveedor();
        registro.setCodigoTelefonoProveedor(Integer.parseInt(txtCodigoTelefono.getText()));
        registro.setNumeroPrincipal(txtNumPrincipal.getText());
        registro.setNumeroSecundario(txtNumSecundario.getText());
        registro.setObservaciones(txtObservaciones.getText());
        registro.setCodigoProveedor(((Proveedores) cmbCodProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_agregarTelefonoProveedor(?,?,?,?,?);");
            /**
             * Establece los parámetros del procedimiento con los valores del
             * objeto Telefono Proveedor.
             */
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

    /**
     * Este metodo realiza la funcion para agregar los datos, Dependiendo del
     * tipo de operación actual, activa o desactiva los controles
     * correspondientes, actualiza el texto y la apariencia de los botones, y
     * realiza acciones específicas para agregar o guardar datos.
     *
     */
    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgAgregar.setImage(new Image("/org/devyntubac/images/guardarIcono.png"));
                imgEliminar.setImage(new Image("/org/devyntubac/images/cancelarIcono.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            /**
             * Si el tipo de Operaciones es ACTUALIZAR, se llama al metodo
             * guardar para realizar su respectiva función y desactiva y limpia
             * los textField, y a su vez los botones vuelven a su apariencia
             * normal.
             */
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/devyntubac/images/agregarCliente.png"));
                imgEliminar.setImage(new Image("/org/devyntubac/images/eliminarCliente.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }

    /**
     * Este metodo actualiza la información en la base de datos con los nuevos
     * valores ingresados en la interfaz.
     */
    public void actualizar() {
        try {
            /**
             * Prepara y ejecuta el procedimiento de la base de datos.
             */
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarTelefonoProveedor(?,?,?,?,?);");
            TelefonoProveedor registro = (TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem();

            /**
             * Actualizar los datos del cliente con los valores ingresados.
             */
            registro.setCodigoTelefonoProveedor(Integer.parseInt(txtCodigoTelefono.getText()));
            registro.setNumeroPrincipal(txtNumPrincipal.getText());
            registro.setNumeroSecundario(txtNumSecundario.getText());
            registro.setObservaciones(txtObservaciones.getText());
            registro.setCodigoProveedor(((Proveedores) cmbCodProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());

            /**
             * Establece los parámetros del procedimiento con los nuevos
             * valores.
             */
            p.setInt(1, registro.getCodigoTelefonoProveedor());
            p.setString(2, registro.getNumeroPrincipal());
            p.setString(3, registro.getNumeroSecundario());
            p.setString(4, registro.getObservaciones());
            p.setInt(5, registro.getCodigoProveedor());
            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Este metodo realiza la funcion de actualzar algun registro, su vez
     * restaura los controles y botones.
     */
    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                /**
                 * Si tipo de operaciones es NINGUNO, primero verifica si hay
                 * registros en la tabla y si no muestra un cuadro de dialogo
                 * para que el usuario selecciono algun registro.
                 */
                if (tblTelefonoProveedor.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/devyntubac/images/actualizarIcono.png"));
                    imgReporte.setImage(new Image("/org/devyntubac/images/cancelarIcono.png"));
                    activarControles();
                    txtCodigoTelefono.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Telefono para Actualizar");
                }
                break;
            case ACTUALIZAR:
                /**
                 * Si es ACTUALIZAR llama al metodo actualizar y restaura los
                 * botones y textField.
                 */
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/devyntubac/images/iconoEditar.png"));
                imgReporte.setImage(new Image("/org/devyntubac/images/reportesCliente.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                desactivarControles();
                limpiarControles();
                cargarDatos();
                break;
        }
    }

    /**
     * Este metodo realiza la funcion de restaurar los botones a su estado
     * original.
     */
    public void reportes() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/devyntubac/images/iconoEditar.png"));
                imgReporte.setImage(new Image("/org/devyntubac/images/reportesCliente.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }

    /**
     * Carga los datos en una tabla de Telefono Proveedor en la interfaz de usuario.
     */
    public void cargarDatos() {
        tblTelefonoProveedor.setItems(getTelefonoProveedor());
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("codigoTelefonoProveedor"));
        colNumPrincipal.setCellValueFactory(new PropertyValueFactory<>("numeroPrincipal"));
        colNumSecundario.setCellValueFactory(new PropertyValueFactory<>("numeroSecundario"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        colCodProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    /**
     * Este metodo activa los textField.
     */
    public void activarControles() {
        txtCodigoTelefono.setEditable(true);
        txtNumPrincipal.setEditable(true);
        txtNumSecundario.setEditable(true);
        txtObservaciones.setEditable(true);
        cmbCodProveedor.setDisable(false);
    }

    /**
     * Este metodo desactiva los textField.
     */
    public void desactivarControles() {
        txtCodigoTelefono.setEditable(false);
        txtNumPrincipal.setEditable(false);
        txtNumSecundario.setEditable(false);
        txtObservaciones.setEditable(false);
        cmbCodProveedor.setDisable(true);
    }

    /**
     * Este metodo limpia los textField.
     */
    public void limpiarControles() {
        txtCodigoTelefono.clear();
        txtNumPrincipal.clear();
        txtNumSecundario.clear();
        txtObservaciones.clear();
        tblTelefonoProveedor.getSelectionModel().getSelectedItem();
        cmbCodProveedor.getSelectionModel().getSelectedItem();
    }

    /**
     * Este metodo sirve para que los datos de la tupla, se coloquen en los
     * textField.
     */
    public void seleccionarElementos() {
        txtCodigoTelefono.setText(String.valueOf(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor()));
        txtNumPrincipal.setText(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroPrincipal());
        txtNumSecundario.setText(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getNumeroSecundario());
        txtObservaciones.setText(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getObservaciones());
        cmbCodProveedor.getSelectionModel().select(buscarProveedores(((TelefonoProveedor) tblTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
    }

    /**
     * Este metodo tiene la funcion de buscar un registro en la lista.
     * @param codigoProveedor resive como parametro el id.
     * @return resultado
     */
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

    /**
     *
     * Este metodo realiza la funcion de eliminar un registro, a su vez restaura
     * los controles y botones.
     */
    public void eliminar() {
        switch (tipoDeOperaciones) {
            /**
             * Si el tipo de Operaciones es ACTUALIZAR, se llama al metodo
             * guardar para realizar su respectiva función y desactiva y limpia
             * los textField, y a su vez los botones vuelven a su apariencia
             * normal.
             */
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgAgregar.setImage(new Image("/org/devyntubac/images/agregarCliente.png"));
                imgEliminar.setImage(new Image("/org/devyntubac/images/eliminarCliente.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            /**
             * Si no procede a mostrar un cuadro de dialogo para confirmar la
             * eliminación, prepara y ejecuta el procedimiento eliminar de la
             * base de datos.
             */
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

    public void buscarTelefonoProveedor(KeyEvent event) {
        String filtroTelefono = txtBuscar.getText();
        if (filtroTelefono.isEmpty()) {
            tblTelefonoProveedor.setItems(listarTelefonoProveedor);
        } else {
            buscarTelefonoProveedor.clear();
            listarProveedores.clear();
            for (TelefonoProveedor tp : listarTelefonoProveedor) {
                if (String.valueOf(tp.getCodigoTelefonoProveedor()).equals(filtroTelefono) || tp.getNumeroPrincipal().contains(filtroTelefono) || tp.getNumeroSecundario().contains(filtroTelefono) || String.valueOf(tp.getCodigoProveedor()).equals(filtroTelefono)) {
                    buscarTelefonoProveedor.add(tp);
                }
            }
            tblTelefonoProveedor.setItems(buscarTelefonoProveedor);
        }
    }

    /**
     * Este metodo realiza la función para cada boton.
     *
     * @param event recibe este parametro para realizar acción.
     */
    @FXML
    public void handleButtonAction(ActionEvent event) {
        /**
         * Si el evento es igual al boton regresar muestra el menu principal.
         */
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuProveedorView();
        }
    }

    /**
     * Inizializa la clase controller, con el metodo cargarDatos.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbCodProveedor.setItems(getProveedores());
        buscarTelefonoProveedor = FXCollections.observableArrayList();
    }

}
