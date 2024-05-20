package org.ludwinxocoy.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.ludwinxocoy.system.Principal;
import org.ludwinxocoy.bean.*;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.ludwinxocoy.db.Conexion;

public class MenuDetalleFacturaController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView tblDetalleFactura;

    @FXML
    private TableColumn colCodDetalleFactura;

    @FXML
    private TableColumn colPrecioUnitario;

    @FXML
    private TableColumn colCantidad;

    @FXML
    private TableColumn colNumeroFactura;

    @FXML
    private TableColumn colCodProducto;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnReporte;

    @FXML
    private ComboBox cmbCodigoProducto;

    @FXML
    private ComboBox cmbNumeroFactura;

    @FXML
    private TextField txtCodDetalleFactura;

    @FXML
    private TextField txtPrecioUnitario;


    @FXML
    private TextField txtCantidad;

    public ObservableList<DetalleFactura> listarDetalleFactura;
    public ObservableList<DetalleFactura> buscarDetalleFactura;
    public ObservableList<Factura> listarFacturas;
    public ObservableList<Producto> listarProductos;

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
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbNumeroFactura.setItems(getFacturas());
        cmbCodigoProducto.setItems(getProductos());
    }

    public ObservableList<DetalleFactura> getDetalleFactura() {
        ArrayList<DetalleFactura> lista = new ArrayList<>();
        ResultSet resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_listarDetalleFactura();");
            resultado = p.executeQuery();
            while (resultado.next()) {
                lista.add(new DetalleFactura(resultado.getInt("codigoDetalleFactura"),
                        resultado.getDouble("precioUnitario"),
                        resultado.getInt("cantidad"),
                        resultado.getInt("numeroFactura"),
                        resultado.getString("codigoProducto")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarDetalleFactura = FXCollections.observableList(lista);
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

    public void cargarDatos() {
        tblDetalleFactura.setItems(getDetalleFactura());
        colCodDetalleFactura.setCellValueFactory(new PropertyValueFactory<>("codigoDetalleFactura"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colCodProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));

    }

    public void guardar() {

        DetalleFactura registro = new DetalleFactura();

        registro.setCodigoDetalleFactura(Integer.parseInt(txtCodDetalleFactura.getText()));
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNumeroFactura(((Factura) cmbNumeroFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
        registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_agregarDetalleFactura(?,?,?,?,?);");
            p.setInt(1, registro.getCodigoDetalleFactura());
            p.setDouble(2, registro.getPrecioUnitario());
            p.setInt(3, registro.getCantidad());
            p.setInt(4, registro.getNumeroFactura());
            p.setString(5, registro.getCodigoProducto());
            p.execute();
            listarDetalleFactura.add(registro);
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

    public Factura buscarFactura(int numeroFactura) {
        Factura resultado = null;
        try {
            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_buscarFactura(?);");
            p.setInt(1, numeroFactura);
            ResultSet registro = p.executeQuery();
            while (registro.next()) {
                resultado = new Factura(registro.getInt("numeroFactura"),
                        registro.getString("estado"),
                        registro.getDouble("totalFactura"),
                        registro.getDate("fechaFactura"),
                        registro.getInt("clienteID"),
                        registro.getInt("codigoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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
                if (tblDetalleFactura.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmas la eliminación del registro", "Eliminar Detalle Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_eliminarDetalleFactura(?);");
                            procedimiento.setInt(1, ((DetalleFactura) tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
                            procedimiento.execute();
                            listarDetalleFactura.remove(tblDetalleFactura.getSelectionModel().getSelectedItem());
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

            PreparedStatement p = Conexion.getInstance().getConexion().prepareCall("call sp_actualizarDetalleFactura(?,?,?,?,?);");
            DetalleFactura registro = (DetalleFactura) tblDetalleFactura.getSelectionModel().getSelectedItem();

            registro.setCodigoDetalleFactura(Integer.parseInt(txtCodDetalleFactura.getText()));
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNumeroFactura(((Factura) cmbNumeroFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
            registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());

            p.setInt(1, registro.getCodigoDetalleFactura());
            p.setDouble(2, registro.getPrecioUnitario());
            p.setInt(3, registro.getCantidad());
            p.setInt(4, registro.getNumeroFactura());
            p.setString(5, registro.getCodigoProducto());
            p.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:

                if (tblDetalleFactura.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    txtCodDetalleFactura.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de Seleccionar un Registro para Actualizar");
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

    public void seleccionarElementos() {
        txtCodDetalleFactura.setText(String.valueOf(((DetalleFactura) tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura()));
        txtPrecioUnitario.setText(String.valueOf(((DetalleFactura) tblDetalleFactura.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtCantidad.setText(String.valueOf(((DetalleFactura) tblDetalleFactura.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbNumeroFactura.getSelectionModel().select(buscarFactura(((DetalleFactura) tblDetalleFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((DetalleFactura) tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoProducto()));
    }

    public void activarControles() {
        txtCodDetalleFactura.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        txtCantidad.setEditable(true);
        cmbNumeroFactura.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }

    public void desactivarControles() {
        txtCodDetalleFactura.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtCantidad.setEditable(false);
        cmbNumeroFactura.setDisable(true);
        cmbCodigoProducto.setDisable(true);
    }

    public void limpiarControles() {
        txtCodDetalleFactura.clear();
        txtPrecioUnitario.clear();
        txtCantidad.clear();
        tblDetalleFactura.getSelectionModel().getSelectedItem();
        cmbNumeroFactura.getSelectionModel().getSelectedItem();
        cmbCodigoProducto.getSelectionModel().getSelectedItem();
    }


    @FXML
    public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuFacturaView();
        }
    }

}
