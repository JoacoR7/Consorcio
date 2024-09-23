/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Usuario;
import com.consorcio.persist.DAOUsuario;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Collection;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;


/**
 *
 * @author joaqu
 */
@Stateless
@LocalBean
public class UsuarioServiceBean {
    private @EJB DAOUsuario dao;
    
    public void crearUsuario(String nombre, String apellido, String correoElectronico,
            String telefono, String usuario, String clave) {
        try {
            if (nombre == null || nombre.isEmpty()){
                throw new IllegalArgumentException("Ingrese el nombre");
            }
            if (apellido == null || apellido.isEmpty()){
                throw new IllegalArgumentException("Ingrese el apellido");
            }
            if (correoElectronico == null || correoElectronico.isEmpty()){
                throw new IllegalArgumentException("Ingrese el correo electrónico");
            }
            if (telefono == null || telefono.isEmpty()){
                throw new IllegalArgumentException("Ingrese el teléfono");
            }
            if (usuario == null || usuario.isEmpty()){
                throw new IllegalArgumentException("Ingrese el usuario");
            }
            if (clave == null || clave.isEmpty()){
                throw new IllegalArgumentException("Ingrese la clave");
            }
            
            Usuario user = new Usuario();
            user.setId(UUID.randomUUID().toString());
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setCorreoElectronico(correoElectronico);
            user.setTelefono(telefono);
            user.setUsuario(usuario);
            user.setClave(hashPassword(clave));
            user.setEliminado(false);
            
            dao.guardarUsuario(user);
            
        } catch (Exception e) {
        }
    }
    
    public String hashPassword(String clave) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Generar salt aleatorio
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        // Crear hash usando PBKDF2
        KeySpec spec = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        
        String saltBase64 = DatatypeConverter.printBase64Binary(salt);
        String hashBase64 = DatatypeConverter.printBase64Binary(hash);
        
        return saltBase64 + ":" + hashBase64;
    }
    
    public boolean verificarContraseña(String claveIngresada, String hashAlmacenado) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] partes = hashAlmacenado.split(":");
        String saltBase64 = partes[0];
        String hashBase64 = partes[1];

        byte[] salt = DatatypeConverter.parseBase64Binary(saltBase64);

        // Crear el hash de la contraseña ingresada usando el salt 
        KeySpec spec = new PBEKeySpec(claveIngresada.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        String hashIngresadoBase64 = DatatypeConverter.printBase64Binary(hash);

        return hashIngresadoBase64.equals(hashBase64);
    }
    
    public Usuario buscarUsuario(String usuario){
        try {
            if (usuario == null || usuario.equals("")){
                throw new IllegalArgumentException("Seleccione un usuario");
            }
            
            Usuario user = dao.buscarUsuario(usuario);
            
            if(!user.isEliminado()){
                return user;
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }
    
    public void modificarUsuario(String nombre, String apellido, String correoElectronico, 
            String telefono, String usuario, String clave) throws Exception{
        try {
            Usuario user = buscarUsuario(usuario);
            if(nombre != null || !nombre.isEmpty()){
                user.setNombre(nombre);
            }
            if(apellido != null || !apellido.isEmpty()){
                user.setApellido(apellido);
            }
            if(correoElectronico != null || !correoElectronico.isEmpty()){
                user.setCorreoElectronico(correoElectronico);
            }
            if(telefono != null || !telefono.isEmpty()){
                user.setTelefono(telefono);
            }
            if(usuario != null || !usuario.isEmpty()){
                user.setUsuario(usuario);
            }
            if(clave != null || !clave.isEmpty()){
                user.setClave(hashPassword(clave));
            }
            
            dao.actualizarUsuario(user);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void eliminarUsuario(String usuario){
        try {
            Usuario user = buscarUsuario(usuario);
            user.setEliminado(true);
            dao.actualizarUsuario(user);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Collection<Usuario> listarUsuarios(){
        try {
            return dao.listarUsuarios();
        } catch (Exception e) {
            throw e;
        }
    }
}
