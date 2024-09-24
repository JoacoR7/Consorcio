/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Direccion;
import com.consorcio.entity.Localidad;
import com.consorcio.persist.DAODireccion;
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
public class DireccionServiceBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private DAODireccion dao;
    @EJB
    private LocalidadServiceBean localidadService;

    public Direccion crearDireccion(String idLocalidad, String calle, String numeracion,
            String barrio, String pisoCasa, String puertaManzana, String coordenadaX,
            String coordenadaY, String observacion) throws Exception {

        try {

            Localidad localidad = localidadService.buscarLocalidad(idLocalidad);
            
            comprobarCampos(calle, numeracion, barrio, pisoCasa, puertaManzana, coordenadaX, coordenadaY);

            Direccion direccion = new Direccion();
            direccion.setId(UUID.randomUUID().toString());
            direccion.setLocalidad(localidad);
            direccion.setCalle(calle);
            direccion.setNumeracion(numeracion);
            direccion.setBarrio(barrio);
            direccion.setPisoCasa(pisoCasa);
            direccion.setPuertaManzana(puertaManzana);
            direccion.setCoordenadaX(coordenadaX);
            direccion.setCoordenadaY(coordenadaY);
            direccion.setObservacion(observacion);

            return direccion;

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

    public Direccion buscarDireccion(String id) throws Exception {

        try {
            if (id == null) {
                throw new IllegalArgumentException("Seleccione una direccion");
            }

            Direccion direccion = dao.buscarDireccionId(id);

            if (!direccion.isEliminado()) {
                return direccion;
            }else{
                throw new IllegalArgumentException("No se encontro la direccion");
            }
        } catch (IllegalArgumentException | NoResultException e) {
            e.getMessage();
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public Direccion modificarDireccion(String idDireccion , String idLocalidad, String calle, String numeracion,
            String barrio, String pisoCasa, String puertaManzana, String coordenadaX,
            String coordenadaY, String observacion) throws Exception {

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
            return direccion;

        } catch (IllegalArgumentException e) {
            throw e;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public void eliminarDireccion(String id) throws Exception {

        try {
            Direccion direccion = dao.buscarDireccionId(id);
            direccion.setEliminado(true);
            dao.actualizarDireccion(direccion);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }
    
    public String search(String calle, String numero, String localidad, String cp, String provincia) {
        // Expresión regular para eliminar los prefijos "Avenida", "Avenida ", "Av." o "Av. "
        String regex = "^(Avenida |Avenida|Av\\. |Av\\.)";

        // Eliminar el prefijo de la dirección en calle si existe
        calle = calle.replaceFirst(regex, "");

        // Separar la dirección restante por palabras utilizando el espacio como delimitador
        String[] palabrasCalle = calle.split(" ");

        // Crear un StringBuilder para formar la URL
        StringBuilder url = new StringBuilder("https://www.google.com/maps/dir/");

        // Concatenar las palabras de la calle en la URL
        for (int i = 0; i < palabrasCalle.length; i++) {
            url.append(palabrasCalle[i]);
            if (i < palabrasCalle.length - 1) {
                url.append("+");
            }
        }

        // Concatenar el número, código postal (cp)
        url.append("+").append(numero).append("+").append(cp);

        // Concatenar localidad, reemplazando espacios por "+"
        String[] palabrasLocalidad = localidad.split(" ");
        for (int i = 0; i < palabrasLocalidad.length; i++) {
            url.append("+").append(palabrasLocalidad[i]);
        }

        // Concatenar provincia, reemplazando espacios por "+"
        String[] palabrasProvincia = provincia.split(" ");
        for (int i = 0; i < palabrasProvincia.length; i++) {
            url.append("+").append(palabrasProvincia[i]);
        }

        // Retornar la URL final
        return url.toString();
    }

}
