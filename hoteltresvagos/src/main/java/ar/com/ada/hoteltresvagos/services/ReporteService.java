package ar.com.ada.hoteltresvagos.services;

import java.util.*;

import ar.com.ada.hoteltresvagos.entities.reportes.*;
import ar.com.ada.hoteltresvagos.managers.ReporteManager;

public class ReporteService {

    public static Scanner Teclado = new Scanner(System.in);

    protected ReporteManager ABMReporte = new ReporteManager();

    public void iniciarSetup() {

        ABMReporte.setup();
    }

    public void iniciarExit() {

        ABMReporte.exit();
    }

    public void listarImportesEstadoId() {

        System.out.println("Ingrese el estado id:");
        int estadoId = Teclado.nextInt();

        List<ReporteImportesEstado> todos = ABMReporte.generarReporteEstadoId(estadoId);
        for (Reporte c : todos) {
            mostrar(c);
        }

    }

    public void listarImportesEstado() {

        List<ReporteImportesEstado> todos = ABMReporte.generarReporteEstado();
        for (Reporte c : todos) {
            mostrar(c);
        }

    }

    public void listarImportesHuespedId() {

        System.out.println("Ingrese el huesped id:");
        int huespedId = Teclado.nextInt();

        List<ReporteImportesHuesped> todos = ABMReporte.generarReporteHuespedId(huespedId);
        for (Reporte c : todos) {
            mostrar(c);
        }

    }

    public void listarImportesHuesped() {

        List<ReporteImportesHuesped> todos = ABMReporte.generarReporteHuesped();
        for (Reporte c : todos) {
            mostrar(c);
        }

    }

    public void mostrar(Reporte reporte) {

        System.out.print("Id: " + reporte.getEstadoId() + " Cantidad de Reservas: " + reporte.getCantidadReservas()
                + " Total Importes Reserva: " + reporte.getTotalImporteReserva() + " Total Importes Total: "
                + reporte.getTotalImporteTotal() + " Total Importes Pagado: " + reporte.getTotalImportePagado());

        if (reporte instanceof ReporteImportesHuesped) {
            ReporteImportesHuesped reporteImpHue = (ReporteImportesHuesped) reporte;
            System.out.print(" Nombre: " + reporteImpHue.getNombre());
        }

    }

}