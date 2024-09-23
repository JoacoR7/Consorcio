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
            System.out.println("La provincia actual es: " + provincia.getNombre());
            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el nombre del departamento");
            }

            try {
                Departamento departamentoExistente = dao.buscarDepartamentoPorProvinciaYNombre(nombre, idProvincia);
                if (departamentoExistente != null) {
                    throw new IllegalArgumentException("Existe un departamento con el nombre para la provincia indicada");
                }
            } catch (Exception ex) {
            }

            Departamento departamento = new Departamento();
            departamento.setId(UUID.randomUUID().toString());
            departamento.setNombre(nombre);
            departamento.setEliminado(false);
            departamento.setProvincia(provincia);
            System.out.println("El departamento será creado con ID: " + departamento.getId() + " " + departamento.getNombre()
                    + " " + departamento.getProvincia().getNombre());

            dao.guardarDepartamento(departamento);

        } catch (IllegalArgumentException e) {
            throw e;
        }catch (Exception ex){
            throw ex;
        }

    }

    public Departamento buscarDepartamento(String id) {

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
            throw e;
        }
        return null;
    }

    public void modificarDepartamento(String nombreDepartamento, String idDepartamento, String idProvincia) {

        try {

            Departamento departamento = buscarDepartamento(idDepartamento);
            Provincia provincia = provinciaService.buscarProvincia(idProvincia);

            if (nombreDepartamento == null || nombreDepartamento.isEmpty()) {
                throw new IllegalArgumentException("Indique el nombre del departamento");
            }

            try {
                Departamento departamentoExistente = dao.buscarDepartamentoPorProvinciaYNombre(nombreDepartamento, idProvincia);
                if (departamentoExistente != null && !departamentoExistente.getId().equals(idDepartamento)) {
                    throw new IllegalArgumentException("Ya existe una provincia con ese nombre");
                }
            } catch (NoResultException e) {
            }

            departamento.setNombre(nombreDepartamento);
            departamento.setProvincia(provincia);
            dao.actualizarDepartamento(departamento);

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
