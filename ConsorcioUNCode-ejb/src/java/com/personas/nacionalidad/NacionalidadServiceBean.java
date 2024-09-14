/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.personas.nacionalidad;

import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author joaqu
 */
@Stateless
@LocalBean
public class NacionalidadServiceBean {
    private @EJB DAONacionalidadBean dao;
    
    public void crearNacionalidad(String nombre){
        try {
            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Ingrese la nacionalidad");
            }
            Nacionalidad nacionalidad = new Nacionalidad();
            nacionalidad.setId(UUID.randomUUID().toString());
            nacionalidad.setNombre(nombre);
            nacionalidad.setEliminado(false);
            
            dao.guardarNacionalidad(nacionalidad);
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Nacionalidad buscarNacionalidad(String nombre){
        try {
            if(nombre == null || nombre.equals("")){
                throw new IllegalArgumentException("Seleccione una nacionalidad");
            }
            
            Nacionalidad nacionalidad = dao.buscarNacionalidadPorNombre(nombre);
            
            if(!nacionalidad.isEliminado()){
                return nacionalidad;
            }
            
        } catch (Exception e) {
            throw e;
        }
        return null;
    }
    
    public void modificarNacionalidad(String nombre){
        try {
            Nacionalidad nacionalidad = buscarNacionalidad(nombre);
            
            if(nombre != null || !nombre.isEmpty()){
                nacionalidad.setNombre(nombre);
            }
            
            dao.actualizarNacionalidad(nacionalidad);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void eliminarNacionalidad(String nombre){
        try {
            Nacionalidad nacionalidad = buscarNacionalidad(nombre);
            nacionalidad.setEliminado(true);
            dao.actualizarNacionalidad(nacionalidad);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Collection<Nacionalidad> listarNacionalidades(){
        try {
            return dao.listarNacionalidades();
        } catch (Exception e) {
            throw e;
        }
    }
}
