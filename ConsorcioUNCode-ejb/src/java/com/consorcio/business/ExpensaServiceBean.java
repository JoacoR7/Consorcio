/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Expensa;
import com.consorcio.persist.DAOExpensa;
import com.consorcio.persist.error.ErrorDAOException;
import com.consorcio.persist.error.NoResultDAOException;
import com.consorcio.util.UtilFechaBean;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.NoResultException;

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

    public void crearExpensa(Date fechaDesde, Date fechaHasta, double importe) throws ErrorServiceException {

        try {

            validarDatos(fechaDesde, importe);
            // Busco alguna expensa q tenga fechaHasta = null
            try {
                Expensa expensaActual = dao.buscarExpensaActual();
                Date fechaHastaExpensaActual = UtilFechaBean.restarDiasAFecha(fechaDesde, 1);
                expensaActual.setFechaHasta(fechaHastaExpensaActual);
            } catch (NoResultDAOException e) {
            }

            // El fechaDesde siempre es inicio de mes y el importe > 0
            Expensa expensa = new Expensa();

            expensa.setId(UUID.randomUUID().toString());
            expensa.setEliminado(false);
            expensa.setFechaDesde(fechaDesde);
            expensa.setFechaHasta(null); // Seteo null para indicar la fecha actual
            expensa.setImporte(importe);

            dao.guardarExpensa(expensa);

        } catch (ErrorDAOException e) {
            throw new ErrorServiceException("Se produjo un error");
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void modificarExpensa(String idExpensa, Date fechaDesde, Date fechaHasta, double importe) throws ErrorServiceException {

        try {

            if (idExpensa == null) {
                throw new ErrorServiceException("Seleccione una expensa");
            }
            if (importe <= 0.0) {
                throw new ErrorServiceException("El importe debe ser mayor a 0");
            }

            Expensa expensa = dao.buscarExpensaId(idExpensa);

            expensa.setFechaDesde(fechaDesde);
            expensa.setFechaHasta(fechaHasta);
            expensa.setImporte(importe);

            dao.actualizarExpensa(expensa);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

    }

    public void eliminarExpensa(String id) {

        try {
            Expensa penultimaExpensa = dao.buscarPenultimaExpensa();
            if (penultimaExpensa != null) {
                penultimaExpensa.setFechaHasta(null);
            }

            Expensa expensa = dao.buscarExpensaId(id);
            expensa.setEliminado(true);
            dao.actualizarExpensa(expensa);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la expensa", e);
        }

    }

    public void validarDatos(Date fechaDesde, double importe) throws ErrorServiceException {

        if (fechaDesde == null) {
            throw new IllegalArgumentException("Ingrese una fecha de inicio");
        }

        try {
            dao.buscarExpensaPorFechaDesde(fechaDesde);
            throw new ErrorServiceException("Ya hay una expensa con ese mes de inicio");
        } catch (NoResultDAOException e) {
        }

        if (importe <= 0.0) {
            throw new ErrorServiceException("El importe debe ser mayor a 0");
        }
    }

    public Expensa buscarExpensaPorId(String id) throws ErrorServiceException {

        try {
            Expensa expensa = dao.buscarExpensaId(id);
            return expensa;
        } catch (NoResultException e) {
            throw new ErrorServiceException("Debe indicar la expensa");
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection<Expensa> listarExpensas() {
        try {
            return dao.listarExpensas();

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

    public Expensa expensaActual() throws Exception {
        try {
            return dao.buscarExpensaActual();
        } catch (Exception e) {
            throw e;
        }
    }

}
