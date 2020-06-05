package ar.com.ada.hoteltresvagos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

import org.hibernate.exception.ConstraintViolationException;

import ar.com.ada.hoteltresvagos.entities.*;
import ar.com.ada.hoteltresvagos.excepciones.*;
import ar.com.ada.hoteltresvagos.managers.*;

public class ABM {

    public static Scanner Teclado = new Scanner(System.in);

    protected HuespedManager ABMHuesped = new HuespedManager();

    protected ReservaManager ABMReserva = new ReservaManager();

    private DateTimeFormatter dFormat = DateTimeFormatter.ofPattern("dd/MM/yy");

    public void iniciar() throws Exception {

        try {

            ABMHuesped.setup();
            ABMReserva.setup();

            printOpciones();

            int opcion = Teclado.nextInt();
            Teclado.nextLine();

            while (opcion > 0) {

                switch (opcion) {
                    case 1:

                        try {
                            altaHuesped();
                        } catch (HuespedDNIException exdni) {
                            System.out.println("Error en el DNI. Indique uno valido");
                        }
                        break;

                    case 2:
                        baja();
                        break;

                    case 3:
                        modifica();
                        break;

                    case 4:
                        listar();
                        break;

                    case 5:
                        listarPorNombre();
                        break;

                    case 6:
                        listarPorDni();
                        break;

                    case 7:
                        altaReservaVersion2();
                        break;

                    case 8:
                        bajaReserva();

                        break;

                    case 9:
                        modificaReserva();
                        break;

                    case 10:
                        listarReserva();
                        break;

                    default:
                        System.out.println("La opcion no es correcta.");
                        break;
                }

                printOpciones();

                opcion = Teclado.nextInt();
                Teclado.nextLine();
            }

            // Hago un safe exit del manager
            ABMHuesped.exit();

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Que lindo mi sistema,se rompio mi sistema");
            throw e;
        } finally {
            System.out.println("Saliendo del sistema, bye bye...");

        }

    }

    public void altaHuesped() throws Exception {

        Huesped huesped = chequearDniRepetido();

        System.out.println("Crear una reserva?");
        System.out.println("1. Si.");
        System.out.println("2. No.");

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

        if (Teclado.nextInt() == 1) {

            Reserva reserva = ingresarDatosReserva();

            reserva.setHuesped(huesped);

        }

    }

    public void altaReserva() throws Exception {

        Reserva reserva = ingresarDatosReserva();

        // Poner "Querés ingresar un nuevo huesped o utilizar un huesped ya registrado"

        Huesped huesped = chequearDniRepetido();

        // criar um novo hospede ou adicionar um novo

        reserva.setHuesped(huesped);

        // Actualizo todos los objectos
        if (huesped.getHuespedId() == 0) {

            ABMHuesped.create(huesped);

        }
        // else creo que no es necesario
        // ABMHuesped.update(huesped);

        /*
         * Si concateno el OBJETO directamente, me trae todo lo que este en el metodo
         * toString() mi recomendacion es NO usarlo para imprimir cosas en pantallas, si
         * no para loguear info Lo mejor es usar:
         * System.out.println("Huesped generada con exito.  " + huesped.getHuespedId);
         */

        System.out.println("Reserva generada con exito.  " + reserva.getReservaId());

    }

    public void altaReservaVersion2() throws Exception {

        Reserva reserva = ingresarDatosReserva();

        // Poner "Querés ingresar un nuevo huesped o utilizar un huesped ya registrado"

        Huesped huesped = chequearDniRepetido();

        // Actualizo todos los objectos
        if (huesped.getHuespedId() == 0) {

            ABMHuesped.create(huesped);

        }

        // criar um novo hospede ou adicionar um novo

        reserva.setHuesped(huesped);

        ABMReserva.create(reserva);

        /*
         * Si concateno el OBJETO directamente, me trae todo lo que este en el metodo
         * toString() mi recomendacion es NO usarlo para imprimir cosas en pantallas, si
         * no para loguear info Lo mejor es usar:
         * System.out.println("Huesped generada con exito.  " + huesped.getHuespedId);
         */

        System.out.println("Reserva generada con exito.  " + reserva.getReservaId());

    }

    public void baja() {
        System.out.println("Ingrese el nombre:");
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

    public void bajaReserva() {
        System.out.println("Ingrese el ID de Reserva:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Reserva reservaEncontrada = ABMReserva.read(id);

        if (reservaEncontrada == null) {
            System.out.println("Reserva no encontrado.");

        } else {

            try {

                ABMReserva.delete(reservaEncontrada);

                System.out.println(
                        "El registro de la reserva " + reservaEncontrada.getReservaId() + " ha sido eliminado.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error al eliminar una reserva. Error: " + e.getCause());
            }

        }

    }

    public void bajaPorDNI() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el DNI de Huesped:");
        int dni = Teclado.nextInt();
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

            switch (selecper) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre:");
                    Teclado.nextLine();
                    huespedEncontrado.setNombre(Teclado.nextLine());

                    break;
                case 2:
                    System.out.println("Ingrese el nuevo DNI:");
                    Teclado.nextLine();
                    huespedEncontrado.setDni(Teclado.nextInt());
                    Teclado.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese el nuevo domicilio:");
                    Teclado.nextLine();
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

    public void modificaReserva() throws Exception {
        // System.out.println("Ingrese el nombre de la huesped a modificar:");
        // String n = Teclado.nextLine();

        System.out.println("Ingrese el ID de la reserva a modificar:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Reserva reservaEncontrada = ABMReserva.read(id);

        if (reservaEncontrada != null) {

            // RECOMENDACION NO USAR toString(), esto solo es a nivel educativo.
            System.out.println(reservaEncontrada.getReservaId() + " seleccionado para modificacion.");

            System.out.println(
                    "Elija qué dato de la reserva desea modificar: \n1: fecha ingreso, \n2: fecha egreso, \n3: importe total, \n4: importe pagado, \n5: huesped id");
            int selecper = Teclado.nextInt();

            switch (selecper) { //TODO Ver para modificar importe total junto ou não
                case 1:
                    System.out.println("Ingrese la nueva fecha de ingreso:");
                    Teclado.nextLine();
                    reservaEncontrada.setFechaIngreso(LocalDate.parse(Teclado.nextLine(), dFormat));

                    break;
                case 2:
                    System.out.println("Ingrese la nueva fecha de egreso:");
                    Teclado.nextLine();
                    reservaEncontrada.setFechaEgreso(LocalDate.parse(Teclado.nextLine(), dFormat));
                    Teclado.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese el nuevo importe total:");
                    // Teclado.nextLine();
                    reservaEncontrada.setImporteTotal(Teclado.nextBigDecimal());

                    break;
                case 4:
                    System.out.println("Ingrese el nuevo importe pagado:");

                    reservaEncontrada.setImportePagado(Teclado.nextBigDecimal());

                    break;

                case 5:

                    System.out.println("Ingrese el nuevo huesped id:");

                    reservaEncontrada.getHuesped().setHuespedId(Teclado.nextInt());

                    break;

                default:
                    break;
            }

            // Teclado.nextLine();

            ABMReserva.update(reservaEncontrada);

            System.out
                    .println("El registro de la reserva " + reservaEncontrada.getReservaId() + " ha sido modificado.");

        } else {
            System.out.println("Reserva no encontrada.");
        }

    }

    public void listar() {

        List<Huesped> todos = ABMHuesped.buscarTodos();
        for (Huesped c : todos) {
            mostrarHuesped(c);
        }
    }

    public void listarReserva() {

        List<Reserva> todos = ABMReserva.buscarTodos();
        for (Reserva c : todos) {
            mostrarReserva(c); // TODO AQUI
        }

    }

    public void listarPorNombre() {

        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Huesped> huespedes = ABMHuesped.buscarPor(nombre);
        for (Huesped huesped : huespedes) {
            mostrarHuesped(huesped);
        }
    }

    public void listarPorDni() {

        System.out.println("Ingrese el DNI de Huesped:");
        int dni = Teclado.nextInt();

        Huesped huesped = ABMHuesped.readByDNI(dni);

        if (huesped == null) {
            System.out.println("Huesped no encontrado.");

        } else {
            mostrarHuesped(huesped);
        }
    }

    public void mostrarHuesped(Huesped huesped) {

        System.out.print("Id: " + huesped.getHuespedId() + " Nombre: " + huesped.getNombre() + " DNI: "
                + huesped.getDni() + " Domicilio: " + huesped.getDomicilio());

        if (huesped.getDomicilioAlternativo() != null)
            System.out.println(" Alternativo: " + huesped.getDomicilioAlternativo());
        else
            System.out.println();
    }

    public void mostrarReserva(Reserva reserva) {

        System.out.print("Id: " + reserva.getReservaId() + " Fecha Reserva: " + reserva.getFechaReserva()
                + " Fecha Ingreso: " + reserva.getFechaIngreso() + " Fecha Egreso: " + reserva.getFechaEgreso()
                + "\nImporte Reserva: " + reserva.getImporteReserva() + " Importe Total: " + reserva.getImporteTotal()
                + " Importe Pagado: " + reserva.getImportePagado() + " Huesped ID: "
                + reserva.getHuesped().getHuespedId());

        if (reserva.getHabitacion() != null)
            System.out.println(" Habitación: " + reserva.getHabitacion());
        else
            System.out.println();
    }

    public static void printOpciones() {
        System.out.println("=======================================");
        System.out.println("");
        System.out.println("1. Para agregar un huesped.");
        System.out.println("2. Para eliminar un huesped.");
        System.out.println("3. Para modificar un huesped.");
        System.out.println("4. Para ver el listado de huespedes.");
        System.out.println("5. Buscar un huesped por nombre especifico(SQL Injection)).");
        System.out.println("6. Buscar un huesped por DNI.");
        System.out.println("7. Para agregar una reserva.");
        System.out.println("8. Para eliminar una reserva.");
        System.out.println("9. Para modificar una reserva.");
        System.out.println("10. Para ver el listado de reservas.");
        System.out.println("0. Para terminar.");
        System.out.println("");
        System.out.println("=======================================");
    }

    public Reserva ingresarDatosReserva() throws Exception {

        Reserva reserva = new Reserva();
        LocalDate fechaIngreso = null;
        LocalDate fechaEgreso = null;

        reserva.setFechaReserva(LocalDate.now()); // pone automaticamente la fecha de hoy

        System.out.println("Ingrese la fecha de ingreso (dd/mm/yy)");

        try {
            fechaIngreso = LocalDate.parse(Teclado.nextLine(), dFormat);

        } catch (Exception e) {
            System.out.println("Ingreso una fecha inválida");
            System.out.println("Vuelva a empezar");
            iniciar();
        }

        System.out.println("Ingrese la fecha de egreso (dd/mm/yy)");
        fechaEgreso = LocalDate.parse(Teclado.nextLine(), dFormat);

        reserva.setFechaIngreso(fechaIngreso);
        reserva.setFechaEgreso(fechaEgreso);

        long diasTotais = ChronoUnit.DAYS.between(fechaIngreso, fechaEgreso);

        BigDecimal importe = BigDecimal.valueOf(diasTotais * 1200); // 1200 es el precio de la diaria

        reserva.setImporteTotal(importe);

        System.out.println("El importe total es de: " + importe);

        BigDecimal porcentajeReserva = new BigDecimal(0.15);

        importe = importe.multiply(porcentajeReserva);

        reserva.setImporteReserva(importe);

        System.out.println("El importe de reserva es de: " + importe.setScale(0, RoundingMode.HALF_DOWN));
        System.out.println("Insira el importe pagado:");

        importe = Teclado.nextBigDecimal();
        Teclado.nextLine();

        reserva.setImportePagado(importe);

        if (importe.compareTo(BigDecimal.ZERO) == 0) {

            System.out.println("NOTA: sin pago no podemos garantir la reserva.");
            reserva.setTipoEstadoId(0); // no pagado

        } else if (importe.compareTo(reserva.getImporteTotal()) == 0) {

            reserva.setTipoEstadoId(1); // pagado
        } else {
            reserva.setTipoEstadoId(3); // parcial
        }

        // System.out.println("Insira el numero de la habitación:");

        // reserva.setHabitacion(Teclado.nextInt());

        return reserva;
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

    public Huesped chequearDniRepetido() throws Exception {

        Huesped huesped = new Huesped();

        System.out.println("Ingrese el nombre:");

        huesped.setNombre(Teclado.nextLine());

        System.out.println("Ingrese el DNI:");

        int dni = Teclado.nextInt();

        Huesped huespedEncontrado = ABMHuesped.readByDNI(dni);

        if (huespedEncontrado == null) {

            huesped.setDni(dni);
            Teclado.nextLine();
            return ingresarDatosHuesped(huesped);

        } else {

            System.out.println("Ya existe un registro con el DNI " + huespedEncontrado.getDni());
            return iniciarDniRepetido(huespedEncontrado);
        }
    }

    public Huesped iniciarDniRepetido(Huesped huesped) throws Exception {

        printOpcionesDniRepetido();

        int opcion = Teclado.nextInt();
        Teclado.nextLine();

        switch (opcion) {
            case 1:

                return chequearDniRepetido();

            case 2:

                return huesped;

            case 3:

                iniciar();
                return null;

            default:
                return null;

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

    public DateTimeFormatter getdFormat() {
        return dFormat;
    }

    public void setdFormat(DateTimeFormatter dFormat) {
        this.dFormat = dFormat;
    }
}