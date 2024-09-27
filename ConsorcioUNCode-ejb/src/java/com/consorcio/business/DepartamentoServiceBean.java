/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Departamento;
import com.consorcio.entity.Provincia;
import com.consorcio.persist.DAODepartamento;
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
public class DepartamentoServiceBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private @EJB
    ProvinciaServiceBean provinciaService;
    private @EJB
    DAODepartamento dao;

    public void crearDepartamento(String nombre, String idProvincia) throws Exception {

        try {

            Provincia provincia = provinciaService.buscarProvincia(idProvincia);
            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el nombre del departamento");
            }
            boolean existeDepto = false;
            try {
                Departamento departamentoExistente = dao.buscarDepartamentoPorProvinciaYNombre(nombre, idProvincia);
                if (departamentoExistente != null) {
                    existeDepto = true;
                }
            } catch (Exception ex) {
            }

            if (existeDepto) {
                throw new ErrorServiceException("Existe un departamento con el nombre para la provincia indicada");
            }
            Departamento departamento = new Departamento();
            departamento.setId(UUID.randomUUID().toString());
            departamento.setNombre(nombre);
            departamento.setEliminado(false);
            departamento.setProvincia(provincia);

            dao.guardarDepartamento(departamento);

        } catch (IllegalArgumentException e) {
            throw new ErrorServiceException(e.getMessage());
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error de sistema");
        }

    }

    public Departamento buscarDepartamento(String id) throws ErrorServiceException {

        try {
            if (id == null) {
                throw new IllegalArgumentException("Seleccione un departamento");
            }

            Departamento departamento = dao.buscarDepartamentoId(id);

            if (!departamento.isEliminado()) {
                return departamento;
            }
        } catch (IllegalArgumentException | NoResultException e) {
            e.getMessage();
            throw new ErrorServiceException(e.getMessage());
        }
        return null;
    }

    public void modificarDepartamento(String nombreDepartamento, String idDepartamento, String idProvincia) throws Exception {

        try {

            Departamento departamento = buscarDepartamento(idDepartamento);
            Provincia provincia = provinciaService.buscarProvincia(idProvincia);

            if (nombreDepartamento == null || nombreDepartamento.isEmpty()) {
                throw new IllegalArgumentException("Indique el nombre del departamento");
            }

            boolean existeDepto = false;
            try {
                Departamento departamentoExistente = dao.buscarDepartamentoPorProvinciaYNombre(nombreDepartamento, idProvincia);
                if (departamentoExistente != null) {
                    existeDepto = true;
                }
            } catch (Exception ex) {
            }

            if (existeDepto) {
                throw new ErrorServiceException("Existe un departamento con el nombre para la provincia indicada");
            }

            departamento.setNombre(nombreDepartamento);
            departamento.setProvincia(provincia);
            dao.actualizarDepartamento(departamento);

        } catch (ErrorServiceException e) {
            e.getMessage();
            throw e;
        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

    public void eliminarDepartamento(String id) {

        try {
            Departamento departamento = buscarDepartamento(id);
            departamento.setEliminado(true);
            dao.actualizarDepartamento(departamento);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el departamento", e);
        }

    }

    public Collection<Departamento> listarDepartamentos() {
        try {
            return dao.listarDepartamentoActivo();

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

    public Collection<Departamento> listarDepartamentosPorProvincia(String idProvincia) {
        try {
            return dao.listarDepartamentoPorProvincia(idProvincia);

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

}
