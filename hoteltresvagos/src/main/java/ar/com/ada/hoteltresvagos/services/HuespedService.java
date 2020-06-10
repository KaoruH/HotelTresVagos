package ar.com.ada.hoteltresvagos.services;

import java.util.List;
import java.util.Scanner;

import ar.com.ada.hoteltresvagos.ABM;
import ar.com.ada.hoteltresvagos.entities.*;
import ar.com.ada.hoteltresvagos.managers.HuespedManager;

public class HuespedService {

    public static Scanner Teclado = new Scanner(System.in);

    protected HuespedManager ABMHuesped = new HuespedManager();
    protected ReservaService reservaService;

    public HuespedService(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    public void iniciarSetup() {

        ABMHuesped.setup();
    }

    public void iniciarExit() {

        ABMHuesped.exit();
    }

    public void alta() throws Exception {

        Huesped huesped = existe();

        // Actualizo todos los objectos
        if (huesped.getHuespedId() == 0) {

            ABMHuesped.create(huesped);

            /*
             * Si concateno el OBJETO directamente, me trae todo lo que este en el metodo
             * toString() mi recomendacion es NO usarlo para imprimir cosas en pantallas, si
             * no para loguear info Lo mejor es usar:
             * System.out.println("Huesped generada con exito.  " + huesped.getHuespedId);
             */

            System.out.println("Huesped generado con exito.  " + huesped);

        }

        System.out.println("Crear una reserva?");
        System.out.println("1. Si.");
        System.out.println("2. No.");

        if (Teclado.nextInt() == 1) {

            Reserva reserva = reservaService.ingresarDatos();

            reserva.setHuesped(huesped);

            reservaService.iniciarCreate(reserva);

            System.out.println("Reserva generada con exito.  " + reserva.getReservaId());

        }

    }

    public void baja() {
        System.out.println("Ingrese el nombre:");
        Teclado.nextLine();
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el ID de Huesped:");
        int id = Teclado.nextInt();
        Teclado.nextLine();

        Huesped huespedEncontrado = ABMHuesped.read(id);

        if (huespedEncontrado == null) {
            System.out.println("Huesped no encontrado.");

        } else {

            try {

                ABMHuesped.delete(huespedEncontrado);

                // No es necesario porque el Cascade.All ya lo deleta.

                // Ademas, en la condicion de if, el dato ya no existe en la base de datos
                // (porque ya fue feletado en la linea 221), pero si existe en java

                // if (!huespedEncontrado.getReservas().isEmpty()) {

                // for (Reserva reserva : huespedEncontrado.getReservas()) {

                // ABMReserva.delete(reserva);

                // }

                // }

                System.out
                        .println("El registro del huesped " + huespedEncontrado.getHuespedId() + " ha sido eliminado.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error al eliminar una huesped. Error: ");
                throw e;
            }

        }

    }

    public void bajaPorDNI() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el DNI de Huesped:");
        int dni = Teclado.nextInt();
        Teclado.nextLine();
        Huesped huespedEncontrado = ABMHuesped.readByDNI(dni);

        if (huespedEncontrado == null) {
            System.out.println("Huesped no encontrado.");

        } else {
            ABMHuesped.delete(huespedEncontrado);
            System.out.println("El registro del DNI " + huespedEncontrado.getDni() + " ha sido eliminado.");
        }
    }

    public void modifica() throws Exception {
        // System.out.println("Ingrese el nombre de la huesped a modificar:");
        // String n = Teclado.nextLine();

        System.out.println("Ingrese el ID del huesped a modificar:");
        int id = Teclado.nextInt();
        Teclado.nextLine();

        Huesped huespedEncontrado = ABMHuesped.read(id);

        if (huespedEncontrado != null) {

            // RECOMENDACION NO USAR toString(), esto solo es a nivel educativo.
            System.out.println(huespedEncontrado.toString() + " seleccionado para modificacion.");

            System.out.println(
                    "Elija qué dato de la huesped desea modificar: \n1: nombre, \n2: DNI, \n3: domicilio, \n4: domicilio alternativo");
            int selecper = Teclado.nextInt();
            Teclado.nextLine();

            switch (selecper) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre:");
                    huespedEncontrado.setNombre(Teclado.nextLine());

                    break;
                case 2:
                    System.out.println("Ingrese el nuevo DNI:");
                    huespedEncontrado.setDni(Teclado.nextInt());
                    Teclado.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese el nuevo domicilio:");
                    huespedEncontrado.setDomicilio(Teclado.nextLine());

                    break;
                case 4:
                    System.out.println("Ingrese el nuevo domicilio alternativo:");
                    String domAlt = Teclado.nextLine();

                    if (!domAlt.isEmpty()) {
                        huespedEncontrado.setDomicilioAlternativo(domAlt);
                    }

                    break;

                default:
                    break;
            }

            // Teclado.nextLine();

            ABMHuesped.update(huespedEncontrado);

            System.out.println("El registro de " + huespedEncontrado.getNombre() + " ha sido modificado.");

        } else {
            System.out.println("Huesped no encontrado.");
        }

    }

    public void listar() {

        List<Huesped> todos = ABMHuesped.buscarTodos();
        for (Huesped c : todos) {
            mostrar(c);
        }
    }

    public void listarPorNombre() {

        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Huesped> huespedes = ABMHuesped.buscarPor(nombre);
        for (Huesped huesped : huespedes) {
            mostrar(huesped);
        }
    }

    public void listarPorDni() {

        System.out.println("Ingrese el DNI de Huesped:");
        int dni = Teclado.nextInt();
        Teclado.nextLine();

        Huesped huesped = ABMHuesped.readByDNI(dni);

        if (huesped == null) {
            System.out.println("Huesped no encontrado.");

        } else {
            mostrar(huesped);
        }
    }

    public void mostrar(Huesped huesped) {

        System.out.print("Id: " + huesped.getHuespedId() + " Nombre: " + huesped.getNombre() + " DNI: "
                + huesped.getDni() + " Domicilio: " + huesped.getDomicilio());

        if (huesped.getDomicilioAlternativo() != null)
            System.out.println(" Alternativo: " + huesped.getDomicilioAlternativo());
        else
            System.out.println();
    }

    public Huesped ingresarDatosHuesped(Huesped huesped) throws Exception {

        System.out.println("Ingrese el domicilio:");
        huesped.setDomicilio(Teclado.nextLine());

        System.out.println("Ingrese el Domicilio alternativo(OPCIONAL):");
        String domAlternativo = Teclado.nextLine();

        if (!domAlternativo.isEmpty()) {
            huesped.setDomicilioAlternativo(domAlternativo);
        }

        return huesped;
    }

    public Huesped existe() throws Exception {

        Huesped huesped = new Huesped();

        System.out.println("Ingrese el nombre:");

        huesped.setNombre(Teclado.nextLine());

        System.out.println("Ingrese el DNI:");

        int dni = Teclado.nextInt();
        Teclado.nextLine();

        Huesped huespedEncontrado = ABMHuesped.readByDNI(dni);

        if (huespedEncontrado == null) {

            huesped.setDni(dni);
            return ingresarDatosHuesped(huesped);

        } else {

            System.out.println("Ya existe un registro con el DNI " + huespedEncontrado.getDni());
            return iniciarDniRepetido(huespedEncontrado);
        }
    }

    public void printOpcionesDniRepetido() {

        System.out.println("==============================");
        System.out.println("");
        System.out.println("1. Intentar de nuevo.");
        System.out.println("2. Usar huesped ya registrado.");
        System.out.println("3. Volver al menu inicial.");
        System.out.println("");
        System.out.println("==============================");
    }

    public Huesped iniciarDniRepetido(Huesped huesped) throws Exception {

        printOpcionesDniRepetido();

        int opcion = Teclado.nextInt();
        Teclado.nextLine();

        switch (opcion) {
            case 1:

                return existe();

            case 2:

                return huesped;

            case 3:

                ABM abm = new ABM();

                abm.iniciar();
                return null;

            default:
                return null;

        }
    }

    public void iniciarCreate(Huesped huesped) {

        ABMHuesped.create(huesped);
    }

}