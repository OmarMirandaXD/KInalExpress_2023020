package org.ludwinxocoy.bean;

import java.sql.Date;


public class Factura {

    private int numeroFactura;
    private String estado;
    private Double totalFactura;
    private Date fechaFactura;
    private int clienteID;
    private int codigoEmpleado;

    
    public Factura() {
    }

    
    public Factura(int numeroFactura, String estado, Double totalFactura, Date fechaFactura, int clienteID, int codigoEmpleado) {
        this.numeroFactura = numeroFactura;
        this.estado = estado;
        this.totalFactura = totalFactura;
        this.fechaFactura = fechaFactura;
        this.clienteID = clienteID;
        this.codigoEmpleado = codigoEmpleado;
    }

    
    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(Double totalFactura) {
        this.totalFactura = totalFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public int getClienteID() {
        return clienteID;
    }

    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    @Override
    public String toString() {
        return "| " + getNumeroFactura() + " | " + getTotalFactura();
    }

}
