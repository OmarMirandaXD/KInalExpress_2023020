package org.ludwinxocoy.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.swing.JOptionPane;
import org.ludwinxocoy.bean.Compras;
import org.ludwinxocoy.bean.Producto;
import org.ludwinxocoy.bean.Proveedores;
import org.ludwinxocoy.bean.TipoDeProducto;
import org.ludwinxocoy.system.Principal;
import org.ludwinxocoy.db.Conexion;
import org.ludwinxocoy.reportes.GenerarReportes;

/**
 * FXML Controller class
 *
 * @author mauco
 */
public class MenuProductosController implements Initializable {

    @FXML
    private TableView tblProductos;

    @FXML
    private TableColumn colCodigoProducto;

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

    @FXML
    private Button btnReportesProducto;

    @FXML
    private Button btnEliminarProducto;

    @FXML
    private Button btnEditarProducto;

    @FXML
    private Button btnAgregarProducto;

    @FXML
    private Button btnRegresar;

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
    private ComboBox cmbCodigoTipoProducto;

    @FXML
    private ComboBox cmbCodigoProveedor;

    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProductos;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<TipoDeProducto> listaTipoP;

    private enum operador {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operador tipoDeOperador = operador.NINGUNO;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbCodigoTipoProducto.setItems(getTipoP());
        cmbCodigoProveedor.setItems(getProveedores());
        desactivarControles();
    }

    public void cargarDatos() {
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colDescripcionProducto.setCellValueFactory(new PropertyValueFactory<>("descripcionProducto"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colPrecioDocena.setCellValueFactory(new PropertyValueFactory<>("precioDocena"));
        colPrecioMayor.setCellValueFactory(new PropertyValueFactory<>("precioMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        colCodTipoProd.setCellValueFactory(new PropertyValueFactory<>("codigoTipoProducto"));
        colCodProv.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
    }

    public void seleccionarElemento() {
        txtCodigoProducto.setText((((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtDescripcionProducto.setText((((Producto) tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto()));
        txtPrecioUnitario.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtPrecioDocena.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
        txtPrecioMayor.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
        txtExistencia.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        cmbCodigoTipoProducto.getSelectionModel().select(buscarTipoProducto(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        cmbCodigoProveedor.getSelectionModel().select(buscarProveedor(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProveedor()));

    }

    public TipoDeProducto buscarTipoProducto(int codigoTipoProducto) {
        TipoDeProducto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarTipoProducto(?)}");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoDeProducto(registro.getInt("codigoTipoProducto"),
                        registro.getString("descripcion")
                );
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
                resultado = new Proveedores(registro.getInt("codigoProveedor"),
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
        ArrayList<Producto> listaP = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaP.add(new Producto(resultado.getString("codigoProducto"),
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
        return listaProductos = FXCollections.observableList(listaP);
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaPro = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaPro.add(new Proveedores(resultado.getInt("codigoProveedor"),
                        resultado.getString("NitProveedor"),
                        resultado.getString("nombresProveedor"),
                        resultado.getString("apellidosProveedor"),
                        resultado.getString("direccionProveedor"),
                        resultado.getString("razonSocial"),
                        resultado.getString("ContactoPrincipal"),
                        resultado.getString("paginaWeb"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableList(listaPro);
    }

    public ObservableList<TipoDeProducto> getTipoP() {
        ArrayList<TipoDeProducto> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoDeProducto(resultado.getInt("CodigoTipoProducto"),
                        resultado.getString("descripcion")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoP = FXCollections.observableList(lista);
    }

    public void agregar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregarProducto.setText("Guardar");
                btnEliminarProducto.setText("Cancelar");
                btnEditarProducto.setDisable(true);
                btnReportesProducto.setDisable(true);
                tipoDeOperador = operador.AGREGAR;
                break;
            case AGREGAR:
                guardar();
                limpiarControles();
                cargarDatos();
                desactivarControles();
                btnAgregarProducto.setText("Agregar");
                btnEliminarProducto.setText("Eliminar");
                btnEditarProducto.setDisable(false);
                btnReportesProducto.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        Producto registro = new Producto();
        registro.setCodigoProducto(txtCodigoProducto.getText());
        registro.setDescripcionProducto(txtDescripcionProducto.getText());
        registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
        registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));;
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
        registro.setCodigoProveedor(((Proveedores) cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoTipoProducto(((TipoDeProducto) cmbCodigoTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_agregarProducto(?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setInt(6, registro.getExistencia());
            procedimiento.setInt(7, registro.getCodigoTipoProducto());
            procedimiento.setInt(8, registro.getCodigoProveedor());
            procedimiento.execute();

            listaProductos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperador) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    btnEditarProducto.setText("Actualizar");
                    btnReportesProducto.setText("Cancelar");
                    btnEliminarProducto.setDisable(true);
                    btnAgregarProducto.setDisable(true);
                    activarControles();
                    txtCodigoProducto.setEditable(false);
                    tipoDeOperador = operador.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un Detalle Compra para editar");
                }
                break;
            case ACTUALIZAR:
                try {
                    actualizar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                btnEditarProducto.setText("Editar");
                btnReportesProducto.setText("Reportes");
                btnAgregarProducto.setDisable(false);
                btnEliminarProducto.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperador = operador.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarProducto(?,?,?,?,?,?,?,?)}");
            Producto registro = (Producto) tblProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProveedor(((Proveedores) cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setCodigoTipoProducto(((TipoDeProducto) cmbCodigoTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
            registro.setDescripcionProducto(txtDescripcionProducto.getText());
            registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setInt(6, registro.getExistencia());
            procedimiento.setInt(7, registro.getCodigoTipoProducto());
            procedimiento.setInt(8, registro.getCodigoProveedor());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() throws SQLException {
        switch (tipoDeOperador) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarProducto.setText("Agregar");
                btnEliminarProducto.setText("Eliminar");
                btnEditarProducto.setDisable(false);
                btnReportesProducto.setDisable(false);
                tipoDeOperador = operador.NINGUNO;
                break;
            default:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirma esta Acción", "Verificación",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            Producto productoSeleccionado = (Producto) tblProductos.getSelectionModel().getSelectedItem();
                            String codigoProducto = productoSeleccionado.getCodigoProducto();

                            PreparedStatement eliminarDetalleCompraStmt = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_EliminarCompra(?)}");
                            eliminarDetalleCompraStmt.setString(1, codigoProducto);
                            eliminarDetalleCompraStmt.execute();

                            PreparedStatement eliminarProductoStmt = Conexion.getInstance().getConexion()
                                    .prepareCall("{call sp_EliminarProducto(?)}");
                            eliminarProductoStmt.setString(1, codigoProducto);
                            eliminarProductoStmt.execute();

                            listaProductos.remove(productoSeleccionado);
                            limpiarControles();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un Producto para eliminar");
                }
                break;
        }
    }

    public void reportes(){
        switch (tipoDeOperador) {
            case NINGUNO:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEliminarProducto.setText("Editar");
                btnReportesProducto.setText("Reportes");
                btnAgregarProducto.setDisable(false);
                btnEliminarProducto.setDisable(false);
                tipoDeOperador = MenuProductosController.operador.NINGUNO;
        }
    }
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        GenerarReportes.mostrarReportesProductos("ReporteProducto.jasper", "reporte de Producto", parametros);
    }

    public void desactivarControles() {
        txtCodigoProducto.setEditable(false);
        txtDescripcionProducto.setEditable(false);
        txtExistencia.setEditable(false);
        txtPrecioDocena.setEditable(false);
        txtPrecioMayor.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        cmbCodigoTipoProducto.setDisable(true);
        cmbCodigoProveedor.setDisable(true);
    }

    public void activarControles() {
        txtCodigoProducto.setEditable(true);
        txtDescripcionProducto.setEditable(true);
        txtExistencia.setEditable(true);;
        txtPrecioDocena.setEditable(true);
        txtPrecioMayor.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        cmbCodigoTipoProducto.setDisable(false);
        cmbCodigoProveedor.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoProducto.clear();
        txtDescripcionProducto.clear();
        txtExistencia.clear();
        txtPrecioDocena.clear();
        txtPrecioMayor.clear();
        txtPrecioUnitario.clear();
        tblProductos.getSelectionModel().getSelectedItem();
        cmbCodigoTipoProducto.getSelectionModel().clearSelection();
        cmbCodigoProveedor.getSelectionModel().clearSelection();

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }
    }

}
