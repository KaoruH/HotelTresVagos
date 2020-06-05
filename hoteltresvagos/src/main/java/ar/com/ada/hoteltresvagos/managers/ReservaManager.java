package ar.com.ada.hoteltresvagos.managers;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.*;


import ar.com.ada.hoteltresvagos.entities.*;

public class ReservaManager {

    protected SessionFactory sessionFactory;

    public void setup() {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings
                                                                                                  // from
                                                                                                  // hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw ex;
        }

    }

    public void exit() {
        sessionFactory.close();
    }

    public void create(Reserva reserva) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(reserva);

        session.getTransaction().commit();
        session.close();
    }

    public Reserva read(int reservaId) {
        Session session = sessionFactory.openSession();

        Reserva reserva = session.get(Reserva.class, reservaId);

        session.close();

        return reserva;
    }

    public void update(Reserva reserva) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(reserva);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Reserva reserva) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(reserva);

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Este metodo en la vida real no debe existir ya qeu puede haber miles de
     * usuarios
     * 
     * @return
     */
    public List<Reserva> buscarTodos() {

        Session session = sessionFactory.openSession();

        /// NUNCA HARCODEAR SQLs nativos en la aplicacion.
        // ESTO es solo para nivel educativo
        Query query = session.createNativeQuery("SELECT * FROM reserva", Reserva.class);
        //query = session.createQuery("From Obse")
        List<Reserva> todos = query.getResultList();

        return todos;

    }

    /**
     * Busca una lista de reserva por el nombre completo Esta armado para que se
     * pueda generar un SQL Injection y mostrar commo NO debe programarse.
     * 
     * @param nombre
     * @return
     */
    public List<Reserva> buscarPor(String nombre) {

        Session session = sessionFactory.openSession();

        // SQL Injection vulnerability exposed.
        // Deberia traer solo aquella del nombre y con esto demostrarmos que trae todas
        // si pasamos
        // como nombre: "' or '1'='1"
        //Nunca hacer de esta manera
        //Query query = session.createNativeQuery("SELECT * FROM huesped h inner join reserva r on h.huesped_id = r.huesped_id where nombre = '" + nombre + "'", Huesped.class);

        //Forma sql query nativa con parametro
        Query queryForma1 = session.createNativeQuery("SELECT * FROM huesped h inner join reserva r on h.huesped_id = r.huesped_id where nombre = ?", Reserva.class);
        
        queryForma1.setParameter(1, nombre);

        //forma query utilizando JPQL
        //Query queryForma2 = session.createNativeQuery("SELECT r from Reserva r WHERE r.huesped.nombre = :nombre", Reserva.class);
        //queryForma2.setParameter("nombre", nombre);
        
        List<Reserva> reservas = queryForma1.getResultList();

        return reservas;

    }
    
}