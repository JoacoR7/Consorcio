/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.entity;

import com.consorcio.enums.EstadoExpensaInmueble;
import com.consorcio.enums.Mes;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author victo
 */
@Entity
public class ExpensaInmueble implements Serializable {
    
    @Id
    private String id;
    @ManyToOne
    private Expensa expensa;
    @ManyToOne Inmueble inmueble;
    private Mes mes;
    private long anio;
    private EstadoExpensaInmueble estado;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    private boolean eliminado;

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Expensa getExpensa() {
        return expensa;
    }

    public void setExpensa(Expensa expensa) {
        this.expensa = expensa;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public long getAnio() {
        return anio;
    }

    public void setAnio(long anio) {
        this.anio = anio;
    }

    public EstadoExpensaInmueble getEstado() {
        return estado;
    }

    public void setEstado(EstadoExpensaInmueble estado) {
        this.estado = estado;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpensaInmueble)) {
            return false;
        }
        ExpensaInmueble other = (ExpensaInmueble) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.consorcio.entity.ExpensaInmueble[ id=" + id + " ]";
    }
    
}
