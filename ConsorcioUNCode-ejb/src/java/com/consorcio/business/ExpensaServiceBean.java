/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Expensa;
import com.consorcio.persist.DAOExpensa;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author victo
 */
@Stateless
@LocalBean
public class ExpensaServiceBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private @EJB
    DAOExpensa dao;

    public void crearExpensa(Date fechaDesde, Date fechaHasta, double importe) {

        try {
            validarDatos(fechaDesde, fechaHasta, importe);
            Expensa expensa = new Expensa();

            expensa.setId(UUID.randomUUID().toString());
            expensa.setEliminado(false);
            expensa.setFechaDesde(fechaDesde);
            expensa.setFechaHasta(fechaHasta);
            expensa.setImporte(importe);

            dao.guardarExpensa(expensa);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception ex) {
            throw ex;
        }

    }

    public void modificarExpensa(String idExpensa, Date fechaDesde, Date fechaHasta, double importe) {

        try {
            validarDatos(fechaDesde, fechaHasta, importe);

            if (idExpensa == null) {
                throw new IllegalArgumentException("Seleccione una expensa");
            }

            Expensa expensa = dao.buscarExpensaId(idExpensa);

            expensa.setFechaDesde(fechaDesde);
            expensa.setFechaHasta(fechaHasta);
            expensa.setImporte(importe);

            dao.actualizarExpensa(expensa);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception ex) {
            throw ex;
        }

    }
    
    public void eliminarExpensa(String id) {

        try {
            Expensa expensa = dao.buscarExpensaId(id);
            expensa.setEliminado(true);
            dao.actualizarExpensa(expensa);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la expensa", e);
        }

    }
    
    public void validarDatos(Date fechaDesde, Date fechaHasta, double importe) {

        if (fechaDesde == null) {
            throw new IllegalArgumentException("Ingrese una fecha de inicio");
        }

        if (fechaHasta == null) {
            throw new IllegalArgumentException("Ingrese una fecha de finalizacion");
        }
        if (importe <= 0.0) {
            throw new IllegalArgumentException("El importe debe ser mayor a 0");
        }
    }

    public Collection<Expensa> listarExpensas() {
        try {
            return dao.listarExpensaActivo();

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

}
