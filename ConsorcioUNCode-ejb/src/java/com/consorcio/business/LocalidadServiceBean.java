/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Departamento;
import com.consorcio.entity.Localidad;
import com.consorcio.persist.DAOLocalidad;
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
public class LocalidadServiceBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private @EJB
    DAOLocalidad dao;
    private @EJB
    DepartamentoServiceBean departamentoService;

    public void crearLocalidad(String nombre, String codigoPostal, String idDpto) throws ErrorServiceException, Exception {

        try {

            Departamento depto = departamentoService.buscarDepartamento(idDpto);

            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el nombre de la localidad");
            } else if (codigoPostal == null || codigoPostal.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el codigo postal");
            }
            
            boolean existeLocalidad = false;
            try {
                Localidad localidadExistente = dao.buscarLocalidadPorDeptoYNombre(idDpto, nombre);
                if (localidadExistente != null) {
                    existeLocalidad = true;
                }
            } catch (Exception ex) {}

            if(existeLocalidad){
                throw new ErrorServiceException("Existe esa localidad para el departamento indicado");
            }
            
            Localidad localidad = new Localidad();
            localidad.setId(UUID.randomUUID().toString());
            localidad.setNombre(nombre);
            localidad.setCodigoPostal(codigoPostal);
            localidad.setEliminado(false);
            localidad.setDepartamento(depto);

            dao.guardarLocalidad(localidad);

        } catch (IllegalArgumentException e) {
            throw new ErrorServiceException(e.getMessage());
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error de sistema");
        }

    }

    public Localidad buscarLocalidad(String id) {

        try {
            if (id == null) {
                throw new IllegalArgumentException("Seleccione una localidad");
            }

            Localidad localidad = dao.buscarLocalidadId(id);

            if (!localidad.isEliminado()) {
                return localidad;
            }
        } catch (IllegalArgumentException | NoResultException e) {
            e.getMessage();
            throw e;
        }
        return null;
    }

    public void modificarLocalidad(String id, String nombre, String codigoPostal, String idDpto) throws Exception {

        try {

            Localidad localidad = dao.buscarLocalidadId(id);

            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Indique el nombre de la localidad");
            } else if (codigoPostal == null || codigoPostal.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el codigo postal");
            }
            boolean existeLocalidad = false;
            try {
                Localidad localidadExistente = dao.buscarLocalidadPorNombre(nombre);
                if (localidadExistente != null && !localidadExistente.getId().equals(id)) {
                    existeLocalidad = true;
                }
            } catch (Exception e) {
            }

            if (existeLocalidad) {
                throw new ErrorServiceException("Ya existe una localidad con ese nombre");
            }

            Departamento departamento = departamentoService.buscarDepartamento(idDpto);
            localidad.setDepartamento(departamento);
            localidad.setCodigoPostal(codigoPostal);
            localidad.setNombre(nombre);

            dao.actualizarLocalidad(localidad);

        } catch (ErrorServiceException e) {
            e.getMessage();
            throw e;
        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

    public void eliminarLocalidad(String id) {

        try {
            Localidad localidad = dao.buscarLocalidadId(id);
            localidad.setEliminado(true);
            dao.actualizarLocalidad(localidad);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la localidad", e);
        }

    }

    public Collection<Localidad> listarLocalidades() {
        try {
            return dao.listarLocalidadActivo();

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

    public Collection<Localidad> listarLocalidadesPorDpto(String idDpto) {
        try {
            return dao.listarLocalidadPorDepartamento(idDpto);

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

}
