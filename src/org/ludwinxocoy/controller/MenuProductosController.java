package org.ludwinxocoy.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.ludwinxocoy.bean.Producto;
import org.ludwinxocoy.system.Principal;

public class MenuProductosController implements Initializable {

    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Producto> listaProductos;

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
    private ComboBox cmbCodigoTipoProducto;
    @FXML
    private ComboBox cmbCodigoProveedor;
    @FXML
    private TextField txtIdCliente;
    @FXML
    private TextField txtCodigoProducto;
    @FXML
    private TextField txtDescripcionProducto;
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
}

