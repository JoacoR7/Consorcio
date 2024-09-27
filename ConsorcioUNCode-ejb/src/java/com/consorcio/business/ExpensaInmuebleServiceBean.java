/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Expensa;
import com.consorcio.entity.ExpensaInmueble;
import com.consorcio.entity.Inmueble;
import com.consorcio.enums.EstadoExpensaInmueble;
import com.consorcio.enums.Mes;
import com.consorcio.persist.DAOExpensaInmueble;
import com.consorcio.persist.error.NoResultDAOException;
import com.consorcio.util.UtilFechaBean;
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
public class ExpensaInmuebleServiceBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private @EJB
    DAOExpensaInmueble dao;
    private @EJB
    ExpensaServiceBean expensaService;
    private @EJB
    InmuebleServiceBean inmuebleService;

    public void validarFecha(Mes mes, long anio) throws ErrorServiceException {

        try {
            if (mes == null) {
                throw new ErrorServiceException("Debe indicar el mes");
            }

            if (anio < 2024) {
                throw new ErrorServiceException("El año indicado debe ser mayor o igual que 2024");
            }
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public void crearExpensaInmueble(String idExpensa, String idInmueble, Mes mes, long anio) throws ErrorServiceException {

        try {

            Expensa expensa = null;
            Inmueble inmueble = null;
            validarExpensaInmueble(idExpensa, idInmueble);
            try {
                expensa = expensaService.buscarExpensaPorId(idExpensa);
                inmueble = inmuebleService.buscarInmueblePorId(idInmueble);
            } catch (ErrorServiceException e) {
                throw e;
            }

            validarFecha(mes, anio);

            try {
                dao.buscarExpensaInmueblePorFecha(idExpensa, idInmueble, mes, anio);
                throw new ErrorServiceException("Ya existe una expensa con esa fecha indicada");
            } catch (NoResultDAOException e) {
            }

            Date fechaInicioMes = UtilFechaBean.llevarInicioDia(UtilFechaBean.llevarInicioMes(new Date()));
            Date fechaVencimiento = UtilFechaBean.agregaDiasAFecha(fechaInicioMes, 5);

            ExpensaInmueble expensaInmueble = new ExpensaInmueble();
            expensaInmueble.setAnio(anio);
            expensaInmueble.setMes(mes);
            expensaInmueble.setId(UUID.randomUUID().toString());
            expensaInmueble.setInmueble(inmueble);
            expensaInmueble.setExpensa(expensa);
            expensaInmueble.setFechaVencimiento(fechaVencimiento);
            expensaInmueble.setEstado(EstadoExpensaInmueble.PENDIENTE);
            expensaInmueble.setEliminado(false);
            dao.guardarExpensaInmueble(expensaInmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw ex;
        }

    }

    public void validarExpensaInmueble(String idExpensa, String idInmueble) throws ErrorServiceException {

        try {

            if (idExpensa == null || idExpensa.isEmpty()) {
                throw new ErrorServiceException("Seleccione una expensa");
            }
            if (idInmueble == null || idInmueble.isEmpty()) {
                throw new ErrorServiceException("Seleccione un inmueble");
            }

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void modificarExpensaInmueble(String idExpensaInmueble, String idExpensa, String idInmueble,
            Mes mes, long anio, EstadoExpensaInmueble estado, Date fechaVencimiento) throws ErrorServiceException, Exception {

        try {

            validarExpensaInmueble(idExpensa, idInmueble);
            validarFecha(mes, anio);
            Expensa expensa = null;
            Inmueble inmueble = null;

            try {
                expensa = expensaService.buscarExpensaPorId(idExpensa);
                inmueble = inmuebleService.buscarInmueblePorId(idInmueble);
            } catch (ErrorServiceException e) {
                throw e;
            }

            if (estado == null) {
                throw new ErrorServiceException("Debe indicar el nuevo estado");
            }
            ExpensaInmueble expensaInmueble = dao.buscarExpensaInmuebleId(idExpensaInmueble);
            expensaInmueble.setAnio(anio);
            expensaInmueble.setMes(mes);
            expensaInmueble.setInmueble(inmueble);
            expensaInmueble.setExpensa(expensa);
            expensaInmueble.setFechaVencimiento(fechaVencimiento);
            expensaInmueble.setEstado(estado);
            dao.actualizarExpensaInmueble(expensaInmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw ex;
        }

    }

    public void eliminarExpensaInmueble(String idExpensaInmueble) throws ErrorServiceException, NoResultDAOException {

        try {

            ExpensaInmueble expensaInmueble = dao.buscarExpensaInmuebleId(idExpensaInmueble);
            expensaInmueble.setEliminado(true);
            dao.actualizarExpensaInmueble(expensaInmueble);

        } catch (NoResultDAOException ex) {
            throw new ErrorServiceException("No se encontró información de esa expensa");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    public ExpensaInmueble buscarExpensaInmueblePorId(String idExpensaInmueble) throws ErrorServiceException{
    
        try {
            return dao.buscarExpensaInmuebleId(idExpensaInmueble);
        } catch (NoResultDAOException e) {
            throw new ErrorServiceException("No se encontró información de esa expensa");
        }catch(Exception ex){
            throw ex;
        }
    }
    
    public Collection<ExpensaInmueble> listarExpensaInmueble() throws ErrorServiceException{
    
        try {
            return dao.listarExpensaInmuebleActivo();
        } catch (NoResultDAOException e) {
            throw new ErrorServiceException("No se encontraron expensas activas");
        }catch (Exception ex){
            throw ex;
        }
    }
    

}
