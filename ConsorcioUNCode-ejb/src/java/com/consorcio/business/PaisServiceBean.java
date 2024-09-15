/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.persist.DAOPais;
import com.consorcio.entity.Pais;
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

    public void crearPais(String nombre) {

        try {
            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el nombre del país");
            }

            if (dao.buscarPaisPorNombre(nombre) != null) {
                // Lanzar IllegalStateException si el país ya existe
                throw new IllegalStateException("El país con el nombre '" + nombre + "' ya existe.");
            } else {
                Pais pais = new Pais(UUID.randomUUID().toString(), nombre, false);
                dao.guardarPais(pais);
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e; 
        }
    }

    public Pais buscarPais(String id) {

        try {
            if (id == null) {
                throw new IllegalArgumentException("Seleccione un pais");
            }

            Pais pais = dao.buscarPaisId(id);

            if (!pais.isEliminado()) {
                return pais;
            }
        } catch (IllegalArgumentException | NoResultException e) {
            e.getMessage();
            throw e;
        }
        return null;
    }
    
    public Pais buscarPaisPorNombre(String nombre){
        try {
            if (nombre == null) {
                throw new IllegalArgumentException("Seleccione un pais");
            }
            return dao.buscarPaisPorNombre(nombre);
        } catch (Exception e) {
            throw e;
        }
    }

    public void modificarPais(String id, String nombre) {

        try {

            Pais pais = buscarPais(id); // Pais seleccionado

            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Indique el nombre del pais");
            }

            try {
                Pais paisExistente = dao.buscarPaisPorNombre(nombre); // Verifico si ya hay un pais con ese nombre

                if (!paisExistente.getId().equals(id)) {
                    throw new IllegalArgumentException("Ya existe un pais con ese nombre");
                }
            } catch (NoResultException e) {
            }

            pais.setNombre(nombre);
            dao.actualizarPais(pais);
        } catch (Exception e) {
            e.getMessage();
            throw e;
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
