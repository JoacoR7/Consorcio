/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Direccion;
import com.consorcio.entity.Propietario;
import com.consorcio.persist.DAO;
import com.consorcio.persist.DAOImpl;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author adrzanbar
 */
@Stateless
@LocalBean
public class PropietarioServiceBean {

    private @EJB
    DAO<Propietario> dao;

    public void validar(String nombre, String apellido, String correoElectronico, String telefono, Direccion direccion) throws IllegalArgumentException {

        try {

            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Debe indicar el nombre");
            }

            if (apellido == null || apellido.isEmpty()) {
                throw new IllegalArgumentException("Debe indicar el apellido");
            }

            if (telefono == null || telefono.isEmpty()) {
                throw new IllegalArgumentException("Debe indicar el teléfono");
            }

            if (correoElectronico == null || correoElectronico.isEmpty()) {
                throw new IllegalArgumentException("Debe indicar el tipo");
            }

        } catch (Exception ex) {
            throw new IllegalArgumentException("Error de Sistemas");
        }
    }

    public void crearPropietario(String nombre, String apellido, String correoElectronico, String telefono, boolean habitaConsorcio, Direccion direccion) throws ErrorServiceException {

        try {

            validar(nombre, apellido, correoElectronico, telefono, direccion);

            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("nombre", nombre);
            searchCriteria.put("apellido", apellido);
            if (!dao.listarLike(searchCriteria).isEmpty()) {
                throw new IllegalArgumentException("Existe un propietario con el nombre y apellido indicado");
            }

            if (habitaConsorcio == false && direccion == null) {
                throw new IllegalArgumentException("Debe indicar la dirección personal, el propietario no habita el consorcio");
            }

            Propietario propietario = new Propietario();
            propietario.setId(UUID.randomUUID().toString());
            propietario.setNombre(nombre);
            propietario.setApellido(apellido);
            propietario.setTelefono(telefono);
            propietario.setCorreoElectronico(correoElectronico);
            propietario.setHabitaConsorcio(habitaConsorcio);
            propietario.setDireccion(habitaConsorcio ? null : direccion);
            propietario.setEliminado(false);

            dao.guardar(propietario);

        } catch (Exception ex) {
            throw new IllegalArgumentException("Error de Sistemas");
        }
    }

    public void modificarPropietario(String idPropietario, String nombre, String apellido, String correoElectronico, String telefono, boolean habitaConsorcio, Direccion direccion) throws ErrorServiceException {

        try {
            validar(nombre, apellido, correoElectronico, telefono, direccion);

            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("nombre", nombre);
            searchCriteria.put("apellido", apellido);
            
            if (!dao.listarLike(searchCriteria).isEmpty()) {
                throw new IllegalArgumentException("Existe un propietario con el nombre y apellido indicado");   
            }
            
            
            Propietario propietarioAux = dao.listarLike(searchCriteria).iterator().next();

        }

        if (!dao.findLike(searchCriteria).isEmpy() && !propietario.getId) {
            try {

                try {
                    Propietario propietarioAux = dao.buscarPropietarioPorNombreApellido(nombre, apellido);
                    if (!propietarioAux.getId().equals(idPropietario)) {
                        throw new ErrorServiceException("Existe un propietario con el nombre y apellido indicado");
                    }
                } catch (NoResultDAOException e) {
                }

                if (habitaConsorcio == false && direccion == null) {
                    throw new ErrorServiceException("Debe indicar la dirección personal, el propietario no habita el consorcio");
                }

                propietario.setNombre(nombre);
                propietario.setApellido(apellido);
                propietario.setTelefono(telefono);
                propietario.setCorreoElectronico(correoElectronico);
                propietario.setHabitaConsorcio(habitaConsorcio);
                propietario.setDireccion(habitaConsorcio ? null : direccion);

                dao.actualizarPropietario(propietario);

            } catch (ErrorServiceException e) {
                throw e;
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ErrorServiceException("Error de Sistemas");
            }
        }
    }

    public void eliminarPropietario(String idInmueble) throws ErrorServiceException {

        try {

            Propietario propietario = buscarPropietario(idInmueble);
            propietario.setEliminado(true);
            dao.actualizarPropietario(propietario);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public Propietario buscarPropietario(String idPropietario) throws ErrorServiceException {

        try {

            if (idPropietario == null || idPropietario.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el propietario");
            }

            Propietario propietario = dao.buscarPropietario(idPropietario);

            if (propietario.isEliminado()) {
                throw new ErrorServiceException("No se encuentra el propietario indicado");
            }

            return propietario;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public Collection<Propietario> listarPropietarioActivo() throws ErrorServiceException {

        try {

            return dao.listarPropietarioActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
