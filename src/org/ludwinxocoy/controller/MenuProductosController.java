package org.ludwinxocoy.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ludwinxocoy.bean.Producto;
import org.ludwinxocoy.bean.Proveedores;
import org.ludwinxocoy.bean.TipoDeProducto;
import org.ludwinxocoy.db.Conexion;
import org.ludwinxocoy.system.Principal;

public class MenuProductosController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProductos;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<TipoDeProducto> listaTipoDeProducto;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAgregarProducto;
    @FXML
    private Button btnEliminarProducto;
    @FXML
    private Button btnReportesProducto;
    @FXML
    private Button btnEditarProducto;
    @FXML
    private ComboBox<TipoDeProducto> cmbCodigoTipoProducto;
    @FXML
    private ComboBox<Proveedores> cmbCodigoProveedor;
    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TextField txtCodigoProducto;
    @FXML
    private TextField txtDescripcionProducto;
    @FXML
    private TextField txtPrecioUnitario;
    @FXML
    private TextField txtPrecioDocena;
    @FXML
    private TextField txtPrecioMayor;
    @FXML
    private TextField txtExistencia;
    @FXML
    private TableColumn colCodigoProduccion;
    @FXML
    private TableColumn colDescripcionProducto;
    @FXML
    private TableColumn colPrecioUnitario;
    @FXML
    private TableColumn colPrecioDocena;
    @FXML
    private TableColumn colPrecioMayor;
    @FXML
    private TableColumn colExistencia;
    @FXML
    private TableColumn colCodTipoProd;
    @FXML
    private TableColumn colCodProv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoProducto.setItems(getTipoP());
        cmbCodigoProveedor.setItems(getProveedores());
    }

    public void cargarDatos() {
        tblProductos.setItems(getProducto());
        colCodigoProduccion.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colDescripcionProducto.setCellValueFactory(new PropertyValueFactory<>("descripcionProducto"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colPrecioDocena.setCellValueFactory(new PropertyValueFactory<>("precioDocena"));
        colPrecioMayor.setCellValueFactory(new PropertyValueFactory<>("precioMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<>("codigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    public void seleccionarElementos() {
        if (tblProductos.getSelectionModel().getSelectedItem() != null) {
            Producto productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();
            txtCodigoProducto.setText(productoSeleccionado.getCodigoProducto());
            txtDescripcionProducto.setText(productoSeleccionado.getDescripcionProducto());
            txtPrecioUnitario.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
            txtPrecioDocena.setText(String.valueOf(productoSeleccionado.getPrecioDocena()));
            txtPrecioMayor.setText(String.valueOf(productoSeleccionado.getPrecioMayor()));
            txtExistencia.setText(String.valueOf(productoSeleccionado.getExistencia()));
            cmbCodigoTipoProducto.getSelectionModel().select(buscarTipoProducto(productoSeleccionado.getCodigoTipoProducto()));
            cmbCodigoProveedor.getSelectionModel().select(buscarProveedor(productoSeleccionado.getCodigoProveedor()));
        }
    }

    public TipoDeProducto buscarTipoProducto(int codigoTipoProducto) {
        TipoDeProducto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarTipoProducto(?)}");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoDeProducto(registro.getInt("codigoTipoProducto"),
                        registro.getString("descripcionProducto"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Proveedores buscarProveedor(int codigoProveedor) {
        Proveedores resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarProveedor(?)}");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Proveedores(
                        registro.getInt("codigoProveedor"),
                        registro.getString("NitProveedor"),
                        registro.getString("nombresProveedor"),
                        registro.getString("apellidosProveedor"),
                        registro.getString("direccionProveedor"),
                        registro.getString("razonSocial"),
                        registro.getString("contactoPrincipal"),
                        registro.getString("paginaWeb")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(
                        resultado.getString("codigoProducto"),
                        resultado.getString("descripcionProducto"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getDouble("precioDocena"),
                        resultado.getDouble("precioMayor"),
                        resultado.getInt("existencia"),
                        resultado.getInt("codigoTipoProducto"),
                        resultado.getInt("codigoProveedor")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaPro = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarProveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedores(
                        resultado.getInt("codigoProveedor"),
                        resultado.getString("NitProveedor"),
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
        return listaProveedores = FXCollections.observableList(listaPro);
    }

    public ObservableList<TipoDeProducto> getTipoP() {
        ArrayList<TipoDeProducto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_mostrarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoDeProducto(
                        resultado.getInt("codigoTipoProducto"),
                        resultado.getString("descripcionProducto")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoDeProducto = FXCollections.observableList(lista);
    }

    public void agregar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                btnAgregarProducto.setText("Guardar");
                btnEliminarProducto.setText("Cancelar");
                btnEditarProducto.setDisable(true);
                btnReportesProducto.setDisable(true);
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregarProducto.setText("Agregar");
                btnEliminarProducto.setText("Eliminar");
                btnEditarProducto.setDisable(false);
                btnReportesProducto.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        Producto registro = new Producto();
        registro.setCodigoProducto(txtCodigoProducto.getText());
        registro.setCodigoProveedor(((Proveedores) cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoTipoProducto(((TipoDeProducto) cmbCodigoTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
        registro.setDescripcionProducto(txtDescripcionProducto.getText());
        registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
        registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_agregarProducto(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setInt(6, registro.getExistencia());
            procedimiento.setInt(7, registro.getCodigoProveedor());
            procedimiento.setInt(8, registro.getCodigoTipoProducto());
            procedimiento.execute();
            listaProductos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarControles() {
        txtCodigoProducto.setEditable(false);
        txtDescripcionProducto.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtPrecioDocena.setEditable(false);
        txtPrecioMayor.setEditable(false);
        txtExistencia.setEditable(false);
        cmbCodigoTipoProducto.setDisable(true);
        cmbCodigoProveedor.setDisable(true);
    }

    public void activarControles() {
        txtCodigoProducto.setEditable(true);
        txtDescripcionProducto.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        txtPrecioDocena.setEditable(true);
        txtPrecioMayor.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoTipoProducto.setDisable(false);
        cmbCodigoProveedor.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoProducto.clear();
        txtDescripcionProducto.clear();
        txtPrecioUnitario.clear();
        txtPrecioDocena.clear();
        txtPrecioMayor.clear();
        txtExistencia.clear();
        cmbCodigoTipoProducto.getSelectionModel().clearSelection();
        cmbCodigoProveedor.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}
