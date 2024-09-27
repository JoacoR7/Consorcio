/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.EstadoInmueble;
import com.consorcio.entity.Inmueble;
import com.consorcio.entity.Inquilino;
import com.consorcio.entity.Propietario;
import com.consorcio.persist.DAOInmueble;
import com.consorcio.persist.error.NoResultDAOException;
import java.util.Collection;
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
public class InmuebleServiceBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private @EJB
    DAOInmueble dao;

    private @EJB
    PropietarioServiceBean propietarioService;
    private @EJB
    InquilinoServiceBean inquiliService;

    public void crearInmueble(String idPropietario, String idInquilino, String piso, String puerta) throws Exception {

        try {

            validar(piso, puerta);

            Propietario propietario = propietarioService.buscarPropietario(idPropietario);
            if (propietario == null || propietario.isEliminado()) {
                throw new ErrorServiceException("Debe ingresar el propietario");
            }
            Inquilino inquilino = inquiliService.buscarInquilino(idInquilino);

            try {
                dao.buscarInmueblePorPisoYPuerta(piso, puerta);
                throw new ErrorServiceException("Ya existe un inmueble con ese piso y puerta");
            } catch (NoResultDAOException e) {
            }

            Inmueble inmueble = new Inmueble();
            inmueble.setId(UUID.randomUUID().toString());
            inmueble.setEliminado(false);
            inmueble.setPropietario(propietario);
            inmueble.setInquilino(inquilino);
            inmueble.setPiso(piso);
            inmueble.setPuerta(puerta);
            inmueble.setEstado((inquilino == null ? (propietario.isHabitaConsorcio() ? EstadoInmueble.HABITADO : EstadoInmueble.DESOCUPADO) : EstadoInmueble.HABITADO));
            dao.guardarInmueble(inmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void validar(String piso, String puerta) throws ErrorServiceException {

        if (piso == null || piso.isEmpty()) {
            throw new ErrorServiceException("Ingrese el piso");
        }
        if (puerta == null || piso.isEmpty()) {
            throw new ErrorServiceException("Ingrese el número de puerta");
        }
    }

    public void modificarInmueble(String idInmueble, String idPropietario, String idInquilino,
            String piso, String puerta) throws Exception {

        try {
            validar(piso, puerta);

            Inmueble inmueble = dao.buscarInmuebleId(idInmueble);

            if (idInquilino == null || idInquilino.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el inquilino");
            }
            if (idPropietario == null || idPropietario.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el propietario");
            }

            Inquilino inquilino = inquiliService.buscarInquilino(idInquilino);
            Propietario propietario = propietarioService.buscarPropietario(idPropietario);

            try {
                Inmueble inmuebleExistente = dao.buscarInmueblePorPisoYPuerta(piso, puerta);
                if (inmuebleExistente != null && !inmuebleExistente.getId().equals(idInmueble)) {
                    throw new ErrorServiceException("Ya existe un inmueble con ese piso y puerta");
                }
            } catch (NoResultDAOException e) {
            }

            inmueble.setPuerta(puerta);
            inmueble.setPiso(piso);
            inmueble.setPropietario(propietario);
            inmueble.setInquilino(inquilino);
            inmueble.setEstado((inquilino == null ? (propietario.isHabitaConsorcio() ? EstadoInmueble.HABITADO : EstadoInmueble.DESOCUPADO) : EstadoInmueble.HABITADO));

            dao.actualizarInmueble(inmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public void eliminarInmueble(String id) throws ErrorServiceException {

        try {
            Inmueble inmueble = dao.buscarInmuebleId(id);
            inmueble.setEliminado(true);
            dao.actualizarInmueble(inmueble);
        } catch (Exception e) {
            throw new ErrorServiceException("Error al eliminar el inmueble");
        }
    }

    public Inmueble buscarInmueblePorId(String id) throws ErrorServiceException {

        try {
            Inmueble inmueble = dao.buscarInmuebleId(id);
            return inmueble;
        } catch (NoResultException e) {
            throw new ErrorServiceException("Debe indicar el inmueble");
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Collection<Inmueble> listarInmueble() {
        try {
            return dao.listarInmuebleActivo();

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }
}
