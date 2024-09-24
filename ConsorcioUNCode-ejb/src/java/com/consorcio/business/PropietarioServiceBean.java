/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Direccion;
import com.consorcio.entity.Propietario;
import com.consorcio.persist.DAOPropietario;
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
public class PropietarioServiceBean {
    private @EJB DAOPropietario dao;
    
    public void crearPropietario(String nombre, String apellido, String telefono,
            String correoElectronico, boolean habitaConsorcio, Direccion direccion){
        try {
            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el nombre del propietario");
            }
            if (apellido == null || apellido.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el apellido del propietario");
            }
            if (telefono == null || telefono.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el número de teléfono del propietario");
            }
            if (correoElectronico == null || correoElectronico.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el correo electrónico del propietario");
            }
            if (direccion == null){
                throw new IllegalArgumentException("Ingrese la dirección del propietario");
            }
            
            Propietario propietario = new Propietario();
            propietario.setId(UUID.randomUUID().toString());
            propietario.setNombre(nombre);
            propietario.setApellido(apellido);
            propietario.setTelefono(telefono);
            propietario.setCorreoElectronico(correoElectronico);
            propietario.setHabitaConsorcio(habitaConsorcio);
            propietario.setDireccion(direccion);
            
            dao.guardarPropietario(propietario);
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Propietario buscarPropietario(String id){
        try {
            if (id == null){
                throw new IllegalArgumentException("Seleccione un propietario");
            }
            
            Propietario propietario = dao.buscarPorId(id);
            
            if(!propietario.isEliminado()){
                return propietario;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
    
    public void modificarPropietario(String id, String nombre, String apellido, String telefono, 
            String correoElectronico, boolean habitaConsorcio, Direccion direccion){
        try {
            Propietario propietario = buscarPropietario(id);
            
            if (nombre != null || !nombre.isEmpty()) {
                propietario.setNombre(nombre);
            }
            if (apellido != null || !apellido.isEmpty()) {
                propietario.setApellido(apellido);
            }
            if (telefono != null || !telefono.isEmpty()) {
                propietario.setTelefono(telefono);
            }
            if (correoElectronico != null || !correoElectronico.isEmpty()) {
                propietario.setCorreoElectronico(correoElectronico);
            }
            propietario.setHabitaConsorcio(habitaConsorcio);
            if (direccion != null){
                propietario.setDireccion(direccion);
            }
            
            dao.actualizarPropietario(propietario);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void eliminarPropietario (String id) {
        try {
            Propietario propietario = buscarPropietario(id);
            propietario.setEliminado(true);
            dao.actualizarPropietario(propietario);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Collection<Propietario> listarPropietarios() {
        try {
            return dao.listarPropietarios();
        } catch (Exception e) {
            throw e;
        }
    }
    
}
