package org.ludwinxocoy.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.ludwinxocoy.system.Principal;

/**
 *
 * @author lphg3
 */
public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    MenuItem btnMenuClientes;
    @FXML
    MenuItem btnMenuProgramador;
    @FXML
    MenuItem btnMenuProveedores;
    @FXML
    MenuItem btnMenuCompras;
    @FXML
    MenuItem btnCargoEmplado;
    @FXML
    MenuItem btnTipoProducto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnMenuClientes) {
            escenarioPrincipal.menuClienteView();
        } else if (event.getSource() == btnMenuProgramador) {
            escenarioPrincipal.menuProgramadorView();
        }else if (event.getSource() == btnMenuProveedores) {
            escenarioPrincipal.menuProveedoresView();
        }else if (event.getSource() == btnMenuCompras) {
            escenarioPrincipal.menuComprasView(); 
         }else if (event.getSource() == btnCargoEmplado) {
            escenarioPrincipal.menuCargoEmpleadoView(); 
         }else if (event.getSource() == btnTipoProducto) {
            escenarioPrincipal.menuTipoProductoView(); 
           
    }  
           
    }  
    

}