package org.ludwinxocoy.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.swing.JOptionPane;
import org.ludwinxocoy.bean.CargoDeEmpleado;
import org.ludwinxocoy.bean.Empleados;
import org.ludwinxocoy.db.Conexion;
import org.ludwinxocoy.system.Principal;

public class MenuEmpleadoController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Empleados> listaEmpleados;

    private enum operaciones {
        AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAgregarEmpleado;
    @FXML
    private Button btnEliminarEmpleado;
    @FXML
    private Button btnReportesEmpleados;
    @FXML
    private Button btnEditarEmpleado;
    @FXML
    private TextField txtCodigoEmpleado;
    @FXML
    private TextField txtNombresEmpleado;
    @FXML
    private TextField txtApellidosEmpleado;
    @FXML
    private TextField txtSueldo;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTurno;
    @FXML
    private TableView tblEmpleados;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colNombresEmpleado;
    @FXML
    private TableColumn colApellidosEmpleado;
    @FXML
    private TableColumn colSueldo;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colTurno;
    @FXML
    private TableColumn colCargoEmpleadoCodigo;
    @FXML
    private ComboBox cmbCodigoCargoEmpleado;
    
    private ObservableList<CargoDeEmpleado> listaCargoE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaEmpleados = FXCollections.observableArrayList();
        cargarDatos();
        cmbCodigoCargoEmpleado.setItems(getCargoE());
        
    }

    public void seleccionarElemento() {
        if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
            Empleados empleado = (Empleados) tblEmpleados.getSelectionModel().getSelectedItem();
            txtCodigoEmpleado.setText(String.valueOf(empleado.getCodigoEmpleado()));
            txtNombresEmpleado.setText(empleado.getNombresEmpleado());
            txtApellidosEmpleado.setText(empleado.getApellidosEmpleado());
            txtSueldo.setText(String.valueOf(empleado.getSueldo()));
            txtDireccion.setText(empleado.getDireccion());
            txtTurno.setText(empleado.getTurno());
            cmbCodigoCargoEmpleado.getSelectionModel().select(buscarCodigoCargoEmpleado(empleado.getCodigoCargoEmpleado()));
        }
    }

    public CargoDeEmpleado buscarCodigoCargoEmpleado(int codigoCargoEmpleado) {
        CargoDeEmpleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_buscarCargoEmpleado(?)}");
            procedimiento.setInt(1, codigoCargoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new CargoDeEmpleado(registro.getInt("codigoCargoEmpleado"), registro.getString("nombreCargo"), registro.getString("descripcionCargo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void agregar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnAgregarEmpleado.setText("Guardar");
                btnEliminarEmpleado.setText("Cancelar");
                btnEditarEmpleado.setDisable(true);
                btnReportesEmpleados.setDisable(true);
                tipoDeOperaciones = operaciones.AGREGAR;
                break;
            case AGREGAR:
                guardar();
                limpiarControles();
                cargarDatos();
                desactivarControles();
                btnAgregarEmpleado.setText("Agregar");
                btnEliminarEmpleado.setText("Eliminar");
                btnEditarEmpleado.setDisable(false);
                btnReportesEmpleados.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregarEmpleado.setText("Agregar");
                btnEliminarEmpleado.setText("Eliminar");
                btnEditarEmpleado.setDisable(false);
                btnReportesEmpleados.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminación del registro",
                            "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                            procedimiento.setInt(1, ((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Necesitas seleccionar un Empleado antes..");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnEditarEmpleado.setText("Actualizar");
                    btnReportesEmpleados.setText("Cancelar");
                    btnAgregarEmpleado.setDisable(true);
                    btnEliminarEmpleado.setDisable(true);
                    activarControles();
                    txtCodigoEmpleado.setEditable(false);
                    tipoDeOperaciones = operaciones.EDITAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un empleado para editar");
                }
                break;
            case EDITAR:
                actualizar();
                btnEditarEmpleado.setText("Editar");
                btnReportesEmpleados.setText("Reporte");
                btnAgregarEmpleado.setDisable(false);
                btnEliminarEmpleado.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_editarEmpleado(?, ?, ?, ?, ?, ?, ?)}");
            Empleados registro = (Empleados) tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
            registro.setDireccion(txtDireccion.getText());
            registro.setTurno(txtTurno.getText());
            registro.setCodigoCargoEmpleado(((CargoDeEmpleado) cmbCodigoCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setString(2, registro.getNombresEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getTurno());
            procedimiento.setInt(7, registro.getCodigoCargoEmpleado());
            procedimiento.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombresEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<>("apellidosEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));
        colCargoEmpleadoCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoCargoEmpleado"));
    }

    public void limpiarControles() {
        txtCodigoEmpleado.clear();
        txtNombresEmpleado.clear();
        txtApellidosEmpleado.clear();
        txtSueldo.clear();
        txtDireccion.clear();
        txtTurno.clear();
        tblEmpleados.getSelectionModel().getSelectedItem();
        cmbCodigoCargoEmpleado.getSelectionModel().clearSelection();
    }

    public void activarControles() {
        txtCodigoEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtSueldo.setEditable(true);
        txtDireccion.setEditable(true);
        txtTurno.setEditable(true);
        cmbCodigoCargoEmpleado.setDisable(false);

    }

    public void desactivarControles() {
        txtCodigoEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtSueldo.setEditable(false);
        txtDireccion.setEditable(false);
        txtTurno.setEditable(false);
        cmbCodigoCargoEmpleado.setDisable(true);
    }

    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleados(resultado.getInt("codigoEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getDouble("sueldo"),
                        resultado.getString("direccion"),
                        resultado.getString("turno"),
                        resultado.getInt("codigoCargoEmpleado")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableArrayList(lista);
    }
    
     

    public ObservableList<CargoDeEmpleado> getCargoE() {
        ArrayList<CargoDeEmpleado> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargoEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new CargoDeEmpleado(resultado.getInt("codigoCargoEmpleado"),
                        resultado.getString("nombreCargo"),
                        resultado.getString("descripcionCargo")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaCargoE = FXCollections.observableList(lista);
    }

    public void guardar() {
        Empleados registro = new Empleados();
        registro.setCodigoEmpleado(Integer.parseInt(txtCodigoEmpleado.getText()));
        registro.setNombresEmpleado(txtNombresEmpleado.getText());
        registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
        registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setTurno(txtTurno.getText());
        registro.setCodigoCargoEmpleado(((CargoDeEmpleado) cmbCodigoCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setString(2, registro.getNombresEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setDouble(4, registro.getSueldo());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getTurno());
            procedimiento.setInt(7, registro.getCodigoCargoEmpleado());
            procedimiento.execute();
            listaEmpleados.add(registro);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresar) {
            escenarioPrincipal.menuPrincipalView();
        }
    }
}
