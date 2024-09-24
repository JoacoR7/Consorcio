/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Consorcio;
import com.consorcio.entity.Direccion;
import com.consorcio.persist.DAOConsorcio;
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
public class ConsorcioServiceBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private DAOConsorcio dao;

    public void crearConsorcio(String nombre, Direccion direccion) throws Exception {

        try {

            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el nombre del consorcio");
            }

            Consorcio consorcio = new Consorcio();
            consorcio.setId(UUID.randomUUID().toString());
            consorcio.setEliminado(false);
            consorcio.setDireccion(direccion);
            consorcio.setNombre(nombre);
            dao.guardarConsorcio(consorcio);

        } catch (Exception e) {
            throw e;
        }
    }

    public void modificarConsorcio(String id, String nombre, Direccion direccion) throws Exception {

        try {

            Consorcio consorcio = dao.buscarConsorcioId(id);

            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Indique el nombre del consorcio");
            }

            try {
                Consorcio consorcioExistente = dao.buscarConsorcioPorNombre(nombre);
                if (consorcioExistente != null && !consorcioExistente.getId().equals(id)) {
                    throw new IllegalArgumentException("Ya existe un consorcio con ese nombre");
                }
            } catch (NoResultException e) {
            }

            consorcio.setDireccion(direccion);
            consorcio.setNombre(nombre);

            dao.actualizarConsorcio(consorcio);

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

    public void eliminarConsorcio(String id) {

        try {
            Consorcio consorcio = dao.buscarConsorcioId(id);
            consorcio.setEliminado(true);
            dao.actualizarConsorcio(consorcio);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el consorcio", e);
        }
    }

    public Collection<Consorcio> listarConsorcio() {
        try {
            return dao.listarConsorcioActivo();

        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

}
