package ar.com.ada.hoteltresvagos.entities.reportes;

import javax.persistence.*;

@Entity
public class ReporteImportesHuesped extends Reporte {

    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}