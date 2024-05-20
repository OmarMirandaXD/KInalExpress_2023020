package org.ludwinxocoy.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.ludwinxocoy.controller.MenuCargoEmpleadoController;
import org.ludwinxocoy.controller.MenuClienteController;
import org.ludwinxocoy.controller.MenuComprasController;
import org.ludwinxocoy.controller.MenuEmpleadoController;
import org.ludwinxocoy.controller.MenuPrincipalController;
import org.ludwinxocoy.controller.MenuProductosController;
import org.ludwinxocoy.controller.MenuProgramadorController;
import org.ludwinxocoy.controller.MenuProveedoresController;
import org.ludwinxocoy.controller.MenuTipoProductoController;

public class Principal extends Application {

    private Stage escenarioPrincipal;
    private Scene escena;

    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("KINAL SHOP");
        menuPrincipalView();
        //       Parent root = FXMLLoader.load(getClass().getResource("/org/ludwinxocoy/view/MenuPrincipalView.fxml"));
        //       Scene escena = new Scene(root);
        //       escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
    }

    public Initializable cambiarEscena(String fxmlname, int width, int heigth) throws Exception {
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        InputStream file = Principal.class.getResourceAsStream("/org/ludwinxocoy/view/" + fxmlname);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Principal.class.getResource("/org/ludwinxocoy/view/" + fxmlname));

        escena = new Scene((AnchorPane) loader.load(file), width, heigth);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();

        resultado = (Initializable) loader.getController();

        return resultado;

    }

    public void menuPrincipalView() {
        try {
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 648, 571);
            menuPrincipalView.setEscenarioPrincipal(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuClienteView() {
        try {
            MenuClienteController menuCliente = (MenuClienteController) cambiarEscena("MenuClienteView.fxml", 876, 734);
            menuCliente.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void menuProgramadorView() {
        try {
            MenuProgramadorController menuProgramadorView = (MenuProgramadorController) cambiarEscena("MenuProgramadorView.fxml", 567, 400);
            menuProgramadorView.setEscenarioPrincipal(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void menuProveedoresView() {
        try {
            MenuProveedoresController menuProveedoresView = (MenuProveedoresController) cambiarEscena("MenuProveedoresView.fxml", 864, 717);
            menuProveedoresView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuComprasView() {
        try {
            MenuComprasController menuComprasView = (MenuComprasController) cambiarEscena("MenuComprasView.fxml", 734, 613);
            menuComprasView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuCargoEmpleadoView() {
        try {
            MenuCargoEmpleadoController MenuCargoEmpleadoView = (MenuCargoEmpleadoController) cambiarEscena("MenuCargoEmpleadoView.fxml", 722, 604);
            MenuCargoEmpleadoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void menuTipoProductoView() {
        try {
            MenuTipoProductoController MenuTipoProductoView = (MenuTipoProductoController) cambiarEscena("MenuTipoProductoView.fxml", 764, 640);
            MenuTipoProductoView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void menuProductosView(){
         try {
            MenuProductosController MenuProductosView = (MenuProductosController) cambiarEscena("MenuProductosView.fxml", 971, 814);
            MenuProductosView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void menuEmpleadosView(){
        try {
            MenuEmpleadoController MenuEmpleadosView = (MenuEmpleadoController) cambiarEscena("MenuEmpleadosView.fxml", 868, 728);
            MenuEmpleadosView.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
    }}
    

    public static void main(String[] args) {
        launch(args);
    }

}
