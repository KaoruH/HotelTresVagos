package ar.com.ada.hoteltresvagos.services;

import java.math.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import ar.com.ada.hoteltresvagos.ABM;
import ar.com.ada.hoteltresvagos.entities.*;
import ar.com.ada.hoteltresvagos.managers.*;

public class ReservaService {

    public static Scanner Teclado = new Scanner(System.in);

    protected ReservaManager ABMReserva = new ReservaManager();
    protected HuespedService huespedService;

    private DateTimeFormatter dFormat = DateTimeFormatter.ofPattern("dd/MM/yy");

    public void iniciarSetup() {

        ABMReserva.setup();
    }

    public void iniciarExit() {

        ABMReserva.exit();
    }

    public void alta() throws Exception {

        Reserva reserva = ingresarDatos();

        // Poner "Querés ingresar un nuevo huesped o utilizar un huesped ya registrado"

        Huesped huesped = huespedService.existe();

        // criar um novo hospede ou adicionar um novo

        reserva.setHuesped(huesped);

        // Actualizo todos los objectos
        if (huesped.getHuespedId() == 0) {

            huespedService.iniciarCreate(huesped);

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

    public void altaVersion2() throws Exception {

        Reserva reserva = ingresarDatos();

        // Poner "Querés ingresar un nuevo huesped o utilizar un huesped ya registrado"

        Huesped huesped = huespedService.existe();

        // Actualizo todos los objectos
        if (huesped.getHuespedId() == 0) {

            huespedService.iniciarCreate(huesped);

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
        System.out.println("Ingrese el ID de Reserva:");
        int id = Teclado.nextInt();

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

    public void modifica() throws Exception {
        // System.out.println("Ingrese el nombre de la huesped a modificar:");
        // String n = Teclado.nextLine();

        System.out.println("Ingrese el ID de la reserva a modificar:");
        int id = Teclado.nextInt();

        Reserva reservaEncontrada = ABMReserva.read(id);

        if (reservaEncontrada != null) {

            // RECOMENDACION NO USAR toString(), esto solo es a nivel educativo.
            System.out.println(reservaEncontrada.getReservaId() + " seleccionado para modificacion.");

            System.out.println(
                    "Elija qué dato de la reserva desea modificar: \n1: fecha ingreso, \n2: fecha egreso, \n3: importe total, \n4: importe pagado, \n5: huesped id");
            int selecper = Teclado.nextInt();

            switch (selecper) { // TODO Ver para modificar importe total junto ou não
                case 1:
                    System.out.println("Ingrese la nueva fecha de ingreso:");
                    reservaEncontrada.setFechaIngreso(LocalDate.parse(Teclado.nextLine(), dFormat));

                    break;
                case 2:
                    System.out.println("Ingrese la nueva fecha de egreso:");
                    reservaEncontrada.setFechaEgreso(LocalDate.parse(Teclado.nextLine(), dFormat));

                    break;
                case 3:
                    System.out.println("Ingrese el nuevo importe total:");

                    reservaEncontrada.setImporteTotal(Teclado.nextBigDecimal());
                    Teclado.nextLine();

                    break;
                case 4:
                    System.out.println("Ingrese el nuevo importe pagado:");

                    reservaEncontrada.setImportePagado(Teclado.nextBigDecimal());
                    Teclado.nextLine();

                    break;

                case 5:

                    System.out.println("Ingrese el nuevo huesped id:");

                    reservaEncontrada.getHuesped().setHuespedId(Teclado.nextInt());
                    Teclado.nextLine();

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

        List<Reserva> todos = ABMReserva.buscarTodos();
        for (Reserva c : todos) {
            mostrar(c);
        }

    }

    public void listarPorNombre() {

        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Reserva> reservas = ABMReserva.buscarPor(nombre);

        printReservaEncontrada(reservas);
    }

    public void listarPorDni() {

        System.out.println("Ingrese el DNI de Huesped:");
        int dni = Teclado.nextInt();
        Teclado.nextLine();

        List<Reserva> reservas = ABMReserva.readByDNI(dni);

        printReservaEncontrada(reservas);
    }

    public void printReservaEncontrada(List<Reserva> reservas) {

        if (reservas == null) {
            System.out.println("Reserva no encontrada.");

        } else {
            for (Reserva reserva : reservas) {
                mostrar(reserva);
            }
        }
    }

    public void mostrar(Reserva reserva) {

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

    public Reserva ingresarDatos() throws Exception {

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
            ABM abm = new ABM();
            abm.iniciar();
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

    public ReservaManager getABMReserva() {
        return ABMReserva;
    }

    public void setABMReserva(ReservaManager aBMReserva) {
        ABMReserva = aBMReserva;
    }

    public HuespedService getHuespedService() {
        return huespedService;
    }

    public void setHuespedService(HuespedService huespedService) {
        this.huespedService = huespedService;
    }

    public DateTimeFormatter getdFormat() {
        return dFormat;
    }

    public void setdFormat(DateTimeFormatter dFormat) {
        this.dFormat = dFormat;
    }

    public void iniciarCreate(Reserva reserva) {

        ABMReserva.create(reserva);
    }

}