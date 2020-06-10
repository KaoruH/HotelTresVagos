package ar.com.ada.hoteltresvagos.entities.reportes;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Reporte {

    @Id
    @Column(name = "estado_id")
    private int estadoId;
    @Column(name = "cantidad_reservas")
    private int cantidadReservas;
    @Column(name = "total_importe_reserva")
    private BigDecimal totalImporteReserva;
    @Column(name = "total_importe_total")
    private BigDecimal totalImporteTotal;
    @Column(name = "total_importe_pagado")
    private BigDecimal totalImportePagado;

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public int getCantidadReservas() {
        return cantidadReservas;
    }

    public void setCantidadReservas(int cantidadReservas) {
        this.cantidadReservas = cantidadReservas;
    }

    public BigDecimal getTotalImporteReserva() {
        return totalImporteReserva;
    }

    public void setTotalImporteReserva(BigDecimal totalImporteReserva) {
        this.totalImporteReserva = totalImporteReserva;
    }

    public BigDecimal getTotalImporteTotal() {
        return totalImporteTotal;
    }

    public void setTotalImporteTotal(BigDecimal totalImporteTotal) {
        this.totalImporteTotal = totalImporteTotal;
    }

    public BigDecimal getTotalImportePagado() {
        return totalImportePagado;
    }

    public void setTotalImportePagado(BigDecimal totalImportePagado) {
        this.totalImportePagado = totalImportePagado;
    }

}