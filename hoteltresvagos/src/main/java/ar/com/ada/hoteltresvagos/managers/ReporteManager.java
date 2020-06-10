package ar.com.ada.hoteltresvagos.managers;

import javax.persistence.Query;

import java.util.List;
import java.util.logging.Level;

import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.*;

import ar.com.ada.hoteltresvagos.entities.reportes.*;

public class ReporteManager {

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

    public void create(Reporte reporte) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(reporte);

        session.getTransaction().commit();
        session.close();
    }

    public Reporte read(int reporteId) {
        Session session = sessionFactory.openSession();

        Reporte reporte = session.get(Reporte.class, reporteId);

        session.close();

        return reporte;
    }

    public void update(Reporte reporte) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(reporte);

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Reporte reporte) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(reporte);

        session.getTransaction().commit();
        session.close();
    }

    public List<ReporteImportesEstado> generarReporteEstadoId(int estado_id) {

        Session session = sessionFactory.openSession();

        Query query = session.createNativeQuery(
                "SELECT r.estado_id, count(r.reserva_id) as cantidad_reservas, sum(r.importe_reserva) as total_importe_reserva, sum(r.importe_total) as total_importe_total, sum(r.importe_pagado) as total_importe_pagado FROM huesped h inner join reserva r on h.huesped_id = r.huesped_id where r.estado_id = ? group by r.estado_id",
                ReporteImportesEstado.class);

        query.setParameter(1, estado_id);

        List<ReporteImportesEstado> reporte = query.getResultList();

        return reporte;
    }

    public List<ReporteImportesEstado> generarReporteEstado() {

        Session session = sessionFactory.openSession();

        Query query = session.createNativeQuery(
                "SELECT r.estado_id, count(r.reserva_id) as cantidad_reservas, sum(r.importe_reserva) as total_importe_reserva, sum(r.importe_total) as total_importe_total, sum(r.importe_pagado) as total_importe_pagado FROM huesped h inner join reserva r on h.huesped_id = r.huesped_id group by r.estado_id",
                ReporteImportesEstado.class);

        List<ReporteImportesEstado> reporte = query.getResultList();

        return reporte;
    }

    public List<ReporteImportesHuesped> generarReporteHuespedId(int huesped_id) {

        Session session = sessionFactory.openSession();

        Query query = session.createNativeQuery(
                "SELECT h.huesped_id, h.nombre, count(r.reserva_id) as cantidad_reservas, sum(r.importe_reserva) as total_importe_reserva, sum(r.importe_total) as total_importe_total, sum(r.importe_pagado) as total_importe_pagado FROM huesped h inner join reserva r on h.huesped_id = r.huesped_id WHERE h.huesped_id = ? group by h.huesped_id, h.nombre",
                ReporteImportesHuesped.class);

        query.setParameter(1, huesped_id);

        List<ReporteImportesHuesped> reporte = query.getResultList();

        return reporte;
    }

    public List<ReporteImportesHuesped> generarReporteHuesped() {

        Session session = sessionFactory.openSession();

        Query query = session.createNativeQuery(
                "SELECT h.huesped_id, h.nombre, count(r.reserva_id) as cantidad_reservas, sum(r.importe_reserva) as total_importe_reserva, sum(r.importe_total) as total_importe_total, sum(r.importe_pagado) as total_importe_pagado FROM huesped h inner join reserva r on h.huesped_id = r.huesped_id group by h.huesped_id, h.nombre",
                ReporteImportesHuesped.class);

        List<ReporteImportesHuesped> reporte = query.getResultList();

        return reporte;
    }

}