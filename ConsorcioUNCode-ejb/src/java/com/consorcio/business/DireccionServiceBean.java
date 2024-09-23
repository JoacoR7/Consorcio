/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Direccion;
import com.consorcio.entity.Localidad;
import com.consorcio.persist.DAODireccion;
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
public class DireccionServiceBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private DAODireccion dao;
    @EJB
    private LocalidadServiceBean localidadService;

    public void crearDireccion(String idLocalidad, String calle, String numeracion,
            String barrio, String pisoCasa, String puertaManzana, String coordenadaX,
            String coordenadaY, String observacion) {

        try {

            Localidad localidad = localidadService.buscarLocalidad(idLocalidad);
            
            comprobarCampos(calle, numeracion, barrio, pisoCasa, puertaManzana, coordenadaX, coordenadaY);

            Direccion direccion = new Direccion();
            direccion.setLocalidad(localidad);
            direccion.setCalle(calle);
            direccion.setNumeracion(numeracion);
            direccion.setBarrio(barrio);
            direccion.setPisoCasa(pisoCasa);
            direccion.setPuertaManzana(puertaManzana);
            direccion.setCoordenadaX(coordenadaX);
            direccion.setCoordenadaY(coordenadaY);
            direccion.setObservacion(observacion);

            dao.guardarDireccion(direccion);

        } catch (IllegalArgumentException e) {
            throw e;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }

    }

    public void comprobarCampos(String calle, String numeracion, String barrio, String pisoCasa, String puertaManzana, String coordenadaX, String coordenadaY) {

        if (comprobarCampoVacio(calle)) {
            throw new IllegalArgumentException("Ingrese la calle");
        }
        if (comprobarCampoVacio(numeracion)) {
            throw new IllegalArgumentException("Ingrese la numeracion");
        }
        if (comprobarCampoVacio(barrio)) {
            throw new IllegalArgumentException("Ingrese el barrio");
        }
        if (comprobarCampoVacio(pisoCasa)) {
            throw new IllegalArgumentException("Ingrese el piso o Casa");
        }
        if (comprobarCampoVacio(puertaManzana)) {
            throw new IllegalArgumentException("Ingrese la puerta o manzana");
        }
        if (comprobarCampoVacio(coordenadaX)) {
            throw new IllegalArgumentException("Ingrese la coordenadaX");
        }
        if (comprobarCampoVacio(coordenadaY)) {
            throw new IllegalArgumentException("Ingrese la coordenadaY");
        }
    }

    public boolean comprobarCampoVacio(String value) {
        return (value == null || value.isEmpty());
    }

    public Direccion buscarDireccion(String id) {

        try {
            if (id == null) {
                throw new IllegalArgumentException("Seleccione una direccion");
            }

            Direccion direccion = dao.buscarDireccionId(id);

            if (!direccion.isEliminado()) {
                return direccion;
            }
        } catch (IllegalArgumentException | NoResultException e) {
            e.getMessage();
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
        return null;
    }

    public void modificarDireccion(String idDireccion , String idLocalidad, String calle, String numeracion,
            String barrio, String pisoCasa, String puertaManzana, String coordenadaX,
            String coordenadaY, String observacion) {

        try {
            Direccion direccion = dao.buscarDireccionId(idDireccion);
            Localidad localidad = localidadService.buscarLocalidad(idLocalidad);
            
            comprobarCampos(calle, numeracion, barrio, pisoCasa, puertaManzana, coordenadaX, coordenadaY);
            
            direccion.setLocalidad(localidad);
            direccion.setCalle(calle);
            direccion.setNumeracion(numeracion);
            direccion.setBarrio(barrio);
            direccion.setPisoCasa(pisoCasa);
            direccion.setPuertaManzana(puertaManzana);
            direccion.setCoordenadaX(coordenadaX);
            direccion.setCoordenadaY(coordenadaY);
            direccion.setObservacion(observacion);

        } catch (IllegalArgumentException e) {
            throw e;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public void eliminarDireccion(String id) {

        try {
            Direccion direccion = dao.buscarDireccionId(id);
            direccion.setEliminado(true);
            dao.actualizarDireccion(direccion);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

}
