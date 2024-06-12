package org.ludwinxocoy.reportes;

import java.io.InputStream;

import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;

import net.sf.jasperreports.engine.JasperPrint;

import net.sf.jasperreports.engine.JasperReport;

import net.sf.jasperreports.engine.util.JRLoader;

import net.sf.jasperreports.view.JasperViewer;

import org.ludwinxocoy.db.Conexion;

public class GenerarReportes {

    public static void mostrarReportes(String nombreReporte, String titulo, Map parametros) {
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);

        try {

            JasperReport ReporteClientes = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteimpreso = JasperFillManager.fillReport(ReporteClientes, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteimpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void mostrarReportesProductos(String nombreReporte, String titulo, Map parametros) {
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try {
            JasperReport ReporteProducto = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteimpreso = JasperFillManager.fillReport(ReporteProducto, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteimpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mostrarReportesEmpleados(String nombreReporte, String titulo, Map parametros) {
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try {
            JasperReport ReportesEmpleados = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteimpreso = JasperFillManager.fillReport(ReportesEmpleados, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteimpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mostrarReportesFactura(String nombreReporte, String titulo, Map parametros) {
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try {
            JasperReport Reporte_Facturas = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteimpreso = JasperFillManager.fillReport(Reporte_Facturas, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteimpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

