package ar.com.ada.hoteltresvagos;

import java.util.*;

import ar.com.ada.hoteltresvagos.excepciones.*;
import ar.com.ada.hoteltresvagos.services.*;

public class ABM {

    public static Scanner Teclado = new Scanner(System.in);

    protected ReservaService reservaService = new ReservaService();
    protected HuespedService huespedService = new HuespedService(reservaService);
    protected ReporteService reporteService = new ReporteService();

    public void iniciar() throws Exception {

        reservaService.setHuespedService(huespedService);

        huespedService.iniciarSetup();
        reservaService.iniciarSetup();
        reporteService.iniciarSetup();

        try {

            printMenuPrincipal();

            int opcion = Teclado.nextInt();

            while (opcion > 0) {

                switch (opcion) {
                    case 1:

                        iniciarHuesped();

                        break;

                    case 2:

                        iniciarReserva();

                        break;

                    case 3:

                        iniciarReporte();

                        break;

                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printMenuPrincipal();

                opcion = Teclado.nextInt();

            }

        } catch (Exception e) {
            System.out.println("Que lindo mi sistema,se rompio mi sistema.");
            throw e;
        } finally {
            System.out.println("Saliendo del sistema, bye bye...");

        }

    }

    public void iniciarHuesped() throws Exception {

        try {

            printMenuHuesped();

            int opcion = Teclado.nextInt();

            while (opcion > 0) {

                switch (opcion) {
                    case 1:

                        try {
                            huespedService.alta();
                        } catch (HuespedDNIException exdni) {
                            System.out.println("Error en el DNI. Indique uno valido");
                        }
                        break;

                    case 2:
                        huespedService.baja();
                        break;

                    case 3:
                        huespedService.modifica();
                        break;

                    case 4:
                        huespedService.listar();
                        break;

                    case 5:
                        huespedService.listarPorNombre();
                        break;

                    case 6:
                        huespedService.listarPorDni();
                        break;

                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printMenuHuesped();

                opcion = Teclado.nextInt();

            }

            // Hago un safe exit del manager
            huespedService.iniciarExit();

        } catch (Exception e) {
            System.out.println("Que lindo mi sistema,se rompio mi sistema. H.");
            throw e;
        } finally {
            System.out.println("Saliendo de huespedes...");

        }

    }

    public void iniciarReserva() throws Exception {

        try {

            printMenuReserva();

            int opcion = Teclado.nextInt();

            while (opcion > 0) {

                switch (opcion) {
                    case 1:

                        try {
                            reservaService.alta();
                        } catch (HuespedDNIException exdni) {
                            // System.out.println("Error en el DNI. Indique uno valido");
                        }
                        break;

                    case 2:
                        reservaService.baja();
                        break;

                    case 3:
                        reservaService.modifica();
                        break;

                    case 4:
                        reservaService.listar();
                        break;

                    case 5:
                        reservaService.listarPorNombre();
                        break;

                    case 6:
                        reservaService.listarPorDni();
                        break;

                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printMenuReserva();

                opcion = Teclado.nextInt();

            }

            // Hago un safe exit del manager
            reservaService.iniciarExit();

        } catch (Exception e) {
            System.out.println("Que lindo mi sistema,se rompio mi sistema. R.");
            throw e;
        } finally {
            System.out.println("Saliendo de reservas...");

        }

    }

    public void iniciarReporte() throws Exception {

        try {

            printMenuReporte();

            int opcion = Teclado.nextInt();

            while (opcion > 0) {

                switch (opcion) {
                    case 1:

                        break;

                    case 2:

                        break;

                    case 3:

                        break;

                    case 4:

                        break;

                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printMenuReporte();

                opcion = Teclado.nextInt();

            }

            // Hago un safe exit del manager
            reporteService.iniciarExit();

        } catch (Exception e) {
            System.out.println("Que lindo mi sistema,se rompio mi sistema. Rp.");
            throw e;
        } finally {
            System.out.println("Saliendo de reportes...");

        }

    }

    public static void printMenuPrincipal() {

        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. Huespedes");
        System.out.println("2. Reservas");
        System.out.println("3. Reportes");
        System.out.println("0. Para terminar");
        System.out.println("");
        System.out.println("=======================================");

    }

    public static void printMenuHuesped() {

        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. Para agregar un huesped");
        System.out.println("2. Para eliminar un huesped");
        System.out.println("3. Para modificar un huesped");
        System.out.println("4. Para ver el listado de huespedes");
        System.out.println("5. Buscar un huesped por nombre especifico(SQL Injection))");
        System.out.println("6. Buscar un huesped por DNI");
        System.out.println("0. Para terminar");
        System.out.println("");
        System.out.println("=======================================");
    }

    public static void printMenuReserva() {

        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. Para agregar una reserva");
        System.out.println("2. Para eliminar una reserva");
        System.out.println("3. Para modificar una reserva");
        System.out.println("4. Para ver el listado de reservas");
        System.out.println("5. Buscar un huesped por nombre especifico(SQL Injection))");
        System.out.println("6. Buscar un huesped por DNI");
        System.out.println("0. Para terminar");
        System.out.println("");
        System.out.println("=======================================");

    }

    public static void printMenuReporte() {

        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. Para ver importes por el ID del huesped");
        System.out.println("2. Para ver importes por huesped");
        System.out.println("3. Para ver importes por el ID del estado de pago");
        System.out.println("4. Para ver importes por estado de pago");
        System.out.println("0. Para terminar");
        System.out.println("");
        System.out.println("=======================================");
    }

}