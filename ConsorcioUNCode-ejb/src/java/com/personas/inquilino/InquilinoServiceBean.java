/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.personas.inquilino;

import com.personas.persona.Persona;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.NoResultException;
/**
 *
 * @author joaqu
 */
@Stateless
@LocalBean
public class InquilinoServiceBean {
    private @EJB DAOInquilino dao;
    
    public void crearInquilino(String nombre, String telefono, String correoElectronico, 
            String documento, String tipoDocumento, SexoEnum sexo, String fechaNacimiento) {
        try {
            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el nombre del inquilino");
            }
            if (telefono == null || telefono.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el número de teléfono del inquilino");
            }
            if (correoElectronico == null || correoElectronico.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el correo electrónico del inquilino");
            }
            if (documento == null || documento.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el documento del inquilino");
            }
            if (tipoDocumento == null || tipoDocumento.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el tipo de documento del inquilino");
            }
            if (sexo == null) {
                throw new IllegalArgumentException("Ingrese el sexo del inquilino");
            }
            if (fechaNacimiento == null || fechaNacimiento.isEmpty()) {
                throw new IllegalArgumentException("Ingrese la fecha de nacimiento del inquilino");
            }

            Inquilino inquilino = new Inquilino();
            inquilino.setId(UUID.randomUUID().toString());
            inquilino.setNombre(nombre);
            inquilino.setTelefono(telefono);
            inquilino.setCorreoElectronico(correoElectronico);
            inquilino.setDocumento(documento);
            inquilino.setTipoDocumento(tipoDocumento);
            inquilino.setSexo(sexo);
            inquilino.setFechaNacimiento(fechaNacimiento);

            dao.guardarInquilino(inquilino);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Inquilino buscarInquilino(String id){
        try {
            if (id == null){
                throw new IllegalArgumentException("Seleccione un inquilino");
            }
            
            Inquilino inquilino = dao.buscarPorId(id);
            
            if (!inquilino.isEliminado()){
                return inquilino;
            }
        }catch (IllegalArgumentException | NoResultException e){
            e.getMessage();
        }
        return null;
    }
    
    public void modificarInquilino(String id, String nombre, String telefono, String correoElectronico,
            String documento, String tipoDocumento, SexoEnum sexo, String fechaNacimiento) {
        try {
            Inquilino inquilino = buscarInquilino(id);
            
            if (nombre != null || !nombre.isEmpty()) {
                inquilino.setNombre(nombre);
            }
            if (telefono != null || !telefono.isEmpty()) {
                inquilino.setTelefono(telefono);
            }
            if (correoElectronico != null || !correoElectronico.isEmpty()) {
                inquilino.setCorreoElectronico(correoElectronico);
            }
            if (documento != null || !documento.isEmpty()) {
                inquilino.setDocumento(documento);
            }
            if (tipoDocumento != null || !tipoDocumento.isEmpty()) {
                inquilino.setTipoDocumento(tipoDocumento);
            }
            if (sexo != null) {
                inquilino.setSexo(sexo);
            }
            if (fechaNacimiento != null || !fechaNacimiento.isEmpty()) {
                inquilino.setFechaNacimiento(fechaNacimiento);
            }
            
            dao.actualizarInquilino(inquilino);
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void eliminarInquilino (String id) {
        try {
            Inquilino inquilino = buscarInquilino(id);
            inquilino.setEliminado(true);
            dao.actualizarInquilino(inquilino);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Collection<Inquilino> listarInquilinos() {
        try {
            return dao.listarInquilinos();
        } catch (Exception e) {
            throw e;
        }
    }

}
