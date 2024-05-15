/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ludwinxocoy.bean;

public class Clientes {

    private int clienteID;
    private String NIT;
    private String nombreClientes;
    private String apellidosClientes;
    private String direccionClientes;
    private String telefonoClientes;
    private String correoClientes;

    public Clientes() {

    }

    public Clientes(int clienteID, String NIT, String nombreClientes, String apellidosClientes, String direccionClientes, String telefonoClientes, String correoClientes) {
        this.clienteID = clienteID;
        this.NIT = NIT;
        this.nombreClientes = nombreClientes;
        this.apellidosClientes = apellidosClientes;
        this.direccionClientes = direccionClientes;
        this.telefonoClientes = telefonoClientes;
        this.correoClientes = correoClientes;
    }

    public int getclienteID() {
        return clienteID;
    }

    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }

    public String getNIT() {
        return NIT;
    }

    public void setNIT(String NIT) {
        this.NIT = NIT;
    }

    public String getNombreClientes() {
        return nombreClientes;
    }

    public void setNombreClientes(String nombreClientes) {
        this.nombreClientes = nombreClientes;
    }

    public String getApellidosClientes() {
        return apellidosClientes;
    }

    public void setApellidosClientes(String apellidosClientes) {
        this.apellidosClientes = apellidosClientes;
    }

    public String getDireccionClientes() {
        return direccionClientes;
    }

    public void setDireccionClientes(String direccionClientes) {
        this.direccionClientes = direccionClientes;
    }

    public String getTelefonoClientes() {
        return telefonoClientes;
    }

    public void setTelefonoClientes(String telefonoClientes) {
        this.telefonoClientes = telefonoClientes;
    }

    public String getCorreoClientes() {
        return correoClientes;
    }

    public void setCorreoClientes(String correoClientes) {
        this.correoClientes = correoClientes;
    }

}
