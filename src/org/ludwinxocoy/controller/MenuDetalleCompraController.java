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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.ludwinxocoy.bean.Compras;
import org.ludwinxocoy.bean.DetalleCompra;
import org.ludwinxocoy.bean.Producto;
import org.ludwinxocoy.db.Conexion;
import org.ludwinxocoy.system.Principal;

public class MenuDetalleCompraController implements Initializable {

    private Principal escenarioPrincipal;

     @FXML
    private TableView tblDetalleCompra;

    @FXML
    private TableColumn colCodigoDetalleCompra;

    @FXML
    private TableColumn colCostoUnitario;

    @FXML
    private TableColumn colCantidad;

    @FXML
    private TableColumn colCodigoProducto;

    @FXML
    private TableColumn colNumeroDocumento;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnReporte;

    @FXML
    private Button btnRegresar;

    @FXML
    private TextField txtCodDetalleC;

    @FXML
    private TextField txtCostoUnitario;

    @FXML
    private TextField txtCantidadDetalleC;

    @FXML
    private ComboBox cmbCodigoProducto;

    @FXML
    private ComboBox cmbNumeroDocumento;

    private ObservableList<DetalleCompra> listarDetalleCompra;
    private ObservableList<Producto> listarProductos;
    private ObservableList<Compras> listarCompras;
    private ObservableList<DetalleCompra> buscarDetalleCompra;

    private enum operador {
        ELIMINAR, EDITAR, CANCELAR, ACTUALIZAR, NINGUNO
    }

    private operador tipoDeOperaciones = operador.NINGUNO;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbCodigoProducto.setItems(getProductos());
        cmbNumeroDocumento.setItems(getCompras());
        

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public ObservableList<DetalleCompra> getDetalleCompra() {
        ArrayList<DetalleCompra> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_listarDetalleCompra();");
            resultado = p.executeQuery();
            while (resultado.next()) {
                lista.add(new DetalleCompra(resultado.getInt("codigoDetalleCompra"),
                        resultado.getDouble("costoUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getString("codigoProducto"),
                        resultado.getInt("numeroDocumento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarDetalleCompra = FXCollections.observableList(lista);
    }

    public ObservableList<Compras> getCompras() {
        ArrayList<Compras> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement pc = Conexion.getInstance().getConexion().prepareCall("call sp_ListarCompras();");
            resultado = pc.executeQuery();

            while (resultado.next()) {
                lista.add(new Compras(resultado.getInt("numeroDocumento"),
                        resultado.getDate("fechaDocumento"),
                        resultado.getString("descripcion"),
                        resultado.getBigDecimal("totalDocumento")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarCompras = FXCollections.observableList(lista);
    }

    public ObservableList<Producto> getProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos();");
            resultado = p.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(resultado.getString("codigoProducto"),
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
        return listarProductos = FXCollections.observableList(lista);

    }

    public void cargarDatos() {
        tblDetalleCompra.setItems(getDetalleCompra());
        colCodigoDetalleCompra.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleCompra"));
        colCostoUnitario.setCellValueFactory(new PropertyValueFactory<>("costoUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));
    }

    public void desactivarControles() {
        txtCodDetalleC.setEditable(false);
        txtCostoUnitario.setEditable(false);
        txtCantidadDetalleC.setEditable(false);
        cmbCodigoProducto.setDisable(true);
        cmbNumeroDocumento.setEditable(true);
    }
    public void limpiarControles() {
        txtCodDetalleC.clear();
        txtCostoUnitario.clear();
        txtCantidadDetalleC.clear();
        tblDetalleCompra.getSelectionModel().getSelectedItem();
        cmbCodigoProducto.getSelectionModel().getSelectedItem();
        cmbNumeroDocumento.getSelectionModel().getSelectedItem();
    }

    public void activarControles() {
        txtCodDetalleC.setEditable(true);
        txtCostoUnitario.setEditable(true);
        txtCantidadDetalleC.setEditable(true);
        cmbCodigoProducto.setDisable(false);
        cmbNumeroDocumento.setEditable(false);
    }

    public void guardar() {

        DetalleCompra registro = new DetalleCompra();
        registro.setCodigoDetalleCompra(Integer.parseInt(txtCodDetalleC.getText()));
        registro.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidadDetalleC.getText()));
        registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setNumeroDocumento(((Compras) cmbNumeroDocumento.getSelectionModel().getSelectedItem()).getNumeroDocumento());
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_agregarDetalleCompra(?,?,?,?,?);");

            p.setInt(1, registro.getCodigoDetalleCompra());
            p.setDouble(2, registro.getCostoUnitario());
            p.setInt(3, registro.getCantidad());
            p.setString(4, registro.getCodigoProducto());
            p.setInt(5, registro.getNumeroDocumento());
            p.execute();
            listarDetalleCompra.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seleccionarElementos() {
        txtCodDetalleC.setText(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
        txtCostoUnitario.setText(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCostoUnitario()));
        txtCantidadDetalleC.setText(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(String.valueOf(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoProducto())));
        cmbNumeroDocumento.getSelectionModel().select(buscarCompra(((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
    }

    public Producto buscarProducto(String codigoProducto) {
        Producto resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_buscarProducto(?);");
            p.setString(1, codigoProducto);
            ResultSet registro = p.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getString("codigoProducto"),
                        registro.getString("descripcionProducto"),
                        registro.getDouble("precioUnitario"),
                        registro.getDouble("precioDocena"),
                        registro.getDouble("precioMayor"),
                        registro.getInt("existencia"),
                        registro.getInt("codigoTipoProducto"),
                        registro.getInt("codigoProveedor"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Compras buscarCompra(int numeroDocumento) {
        Compras resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_buscarCompra(?);");
            p.setInt(1, numeroDocumento);
            ResultSet registro = p.executeQuery();
            while (registro.next()) {
                resultado = new Compras(registro.getInt("numeroDocumento"),
                        registro.getDate("fechaDocumento"),
                        registro.getString("descripcion"),
                        registro.getBigDecimal("totalDocumento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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
                if (tblDetalleCompra.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminación del registro", "Eliminar Detalle Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarDetalleCompra(?);");
                            procedimiento.setInt(1, ((DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
                            procedimiento.execute();
                            listarDetalleCompra.remove(tblDetalleCompra.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Detalle Compra para Eliminar");
                }
                break;
        }
    }

    public void actualizar() {
        try {

            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarDetalleCompra(?,?,?,?,?);");
            DetalleCompra registro = (DetalleCompra) tblDetalleCompra.getSelectionModel().getSelectedItem();

            registro.setCodigoDetalleCompra(Integer.parseInt(txtCodDetalleC.getText()));
            registro.setCostoUnitario(Double.parseDouble(txtCostoUnitario.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidadDetalleC.getText()));
            registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setNumeroDocumento(((Compras) cmbNumeroDocumento.getSelectionModel().getSelectedItem()).getNumeroDocumento());

            p.setInt(1, registro.getCodigoDetalleCompra());
            p.setDouble(2, registro.getCostoUnitario());
            p.setInt(3, registro.getCantidad());
            p.setString(4, registro.getCodigoProducto());
            p.setInt(5, registro.getNumeroDocumento());
            p.execute();
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
                cargarDatos();
                break;
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:

                if (tblDetalleCompra.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtCodDetalleC.setEditable(false);
                    tipoDeOperaciones = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Detalle Compra para Actualizar");
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

 

    @FXML
    public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

    

}
