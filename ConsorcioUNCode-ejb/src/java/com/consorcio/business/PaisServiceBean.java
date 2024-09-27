/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.persist.DAOPais;
import com.consorcio.entity.Pais;
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
public class PaisServiceBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private @EJB
    DAOPais dao;

    public void crearPais(String nombre) throws NoResultDAOException, ErrorServiceException, Exception {

        try {
            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el nombre del país");
            }
            try {
                dao.buscarPaisPorNombre(nombre);
                throw new ErrorServiceException("El país con el nombre '" + nombre + "' ya existe.");
            } catch (NoResultDAOException e) {
            }
            Pais pais = new Pais(UUID.randomUUID().toString(), nombre, false);
            dao.guardarPais(pais);

        } catch (IllegalArgumentException | ErrorServiceException e) {
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error de sistema");
        }
    }

    public Pais buscarPais(String id) throws Exception {

        try {
            if (id == null) {
                throw new IllegalArgumentException("Seleccione un pais");
            }

            Pais pais = dao.buscarPaisId(id);

            if (!pais.isEliminado()) {
                return pais;
            }else{
                throw new ErrorServiceException("El país se encuentra eliminado");
            }
        } catch (IllegalArgumentException | NoResultException | ErrorServiceException e) {
            e.getMessage();
            throw new Exception(e.getMessage());
        } catch(Exception e){
            throw new Exception("Error de sistemas");
        }
    }

    public Pais buscarPaisPorNombre(String nombre) throws Exception {
        try {
            if (nombre == null) {
                throw new IllegalArgumentException("Seleccione un pais");
            }
            return dao.buscarPaisPorNombre(nombre);
        } catch (Exception e) {
            throw e;
        }
    }

    public void modificarPais(String id, String nombre) throws Exception {
        try {
            Pais pais = buscarPais(id); // País seleccionado

            if (pais == null) {
                throw new IllegalArgumentException("No se encontró el país con el ID proporcionado.");
            }

            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Indique el nombre del país.");
            }

            try {
                Pais paisExistente = dao.buscarPaisPorNombre(nombre);

                if (paisExistente != null && !paisExistente.getId().equals(id)) {
                    throw new IllegalArgumentException("Ya existe un país con ese nombre.");
                }
            } catch (NoResultException e) {
                // Si no se encuentra ningún país con el nombre, continúo con la modificación
            }

            pais.setNombre(nombre);
            dao.actualizarPais(pais);
        } catch(IllegalArgumentException e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error de sistemas");
        }
    }

    public void eliminarPais(String id) {

        try {
            Pais pais = dao.buscarPaisId(id);
            pais.setEliminado(true);
            dao.actualizarPais(pais);
        } catch (Exception e) {
            e.getMessage();
            throw e;
        }

    }

    public Collection<Pais> listarPais() {
        try {
            return dao.listarPaisActivo();

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

}
