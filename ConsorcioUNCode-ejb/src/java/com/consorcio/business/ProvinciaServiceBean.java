/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Provincia;
import com.consorcio.persist.DAOProvincia;
import com.consorcio.entity.Pais;
import com.consorcio.business.PaisServiceBean;
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
public class ProvinciaServiceBean {

    private @EJB DAOProvincia dao;
    private @EJB PaisServiceBean paisService;
    
    public void crearProvincia(String nombre, String idPais){
    
        try {
            
            Pais pais = paisService.buscarPais(idPais);
            
            if (nombre == null || nombre.isEmpty()){
                throw new  IllegalArgumentException("Ingrese el nombre de la provincia");
            }
            
            try {
                dao.buscarProvinciaPorPaisYNombre(idPais, nombre);
                throw new IllegalArgumentException("Existe una provincia con el nombre para el país indicado");
            } catch (Exception ex) {}
            
            Provincia provincia = new Provincia();
            provincia.setId(UUID.randomUUID().toString());
            provincia.setNombre(nombre);
            provincia.setEliminado(false);
            provincia.setPais(pais);
            
            dao.guardarProvincia(provincia);
            
        } catch(IllegalArgumentException e){
            throw e;
        }
        
    }
    
    public Provincia buscarProvincia(String id){
        
        try {
            if (id == null){
                throw new IllegalArgumentException("Seleccione una provincia");
            }
            
            Provincia provincia = dao.buscarProvinciaId(id);
            
            if (!provincia.isEliminado()){
                return provincia;
            }
        }catch (IllegalArgumentException | NoResultException e){
            e.getMessage();
            throw e;
        }
        return null;
    }
    
    public Provincia buscarProvinciaPorNombre(String nombre) {
        try {
            if (nombre == null) {
                throw new IllegalArgumentException("Seleccione una provincia");
            }
            return dao.buscarProvinciaPorNombre(nombre);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void modificarProvincia(String id , String nombre){
    
        try {
        
            Provincia provincia = dao.buscarProvinciaId(id); 
            
            if (nombre == null || nombre.isEmpty()){
                throw new IllegalArgumentException("Indique el nombre de la provincia");
            }
            
            try {
                Provincia provinciaExistente = dao.buscarProvinciaPorNombre(nombre); 
                if (!provinciaExistente.getId().equals(id)){
                    throw new IllegalArgumentException("Ya existe una provincia con ese nombre");
                }
            }catch (NoResultException e){}
            
            provincia.setNombre(nombre);
            dao.actualizarProvincia(provincia);
            
        } catch (Exception e){
            e.getMessage();
            throw e;
        }
    }
   
    public void eliminarProvincia(String id){
    
        try {
            Provincia provincia = dao.buscarProvinciaId(id);
            provincia.setEliminado(true);
            dao.actualizarProvincia(provincia); 
        }catch (Exception e){
            throw new RuntimeException("Error al eliminar la provincia",e);
        }

    }
    
    public Collection<Provincia> listarPais(){
        try {
            return dao.listarProvinciaActivo();
            
        } catch (Exception e){
            e.getMessage();
            throw e;
        }
    }
}
