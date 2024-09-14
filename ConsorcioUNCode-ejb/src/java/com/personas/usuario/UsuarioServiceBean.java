/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.personas.usuario;

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
    private @EJB DAOUsuarioBean dao;
    
    public void crearUsuario(String usuario, String clave) {
        try {
            if (usuario == null || usuario.isEmpty()){
                throw new IllegalArgumentException("Ingrese el usuario");
            }
            if (clave == null || clave.isEmpty()){
                throw new IllegalArgumentException("Ingrese la clave");
            }
            
            Usuario user = new Usuario();
            user.setId(UUID.randomUUID().toString());
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
    
    public void modificarUsuario(String usuario, String clave) throws Exception{
        try {
            Usuario user = buscarUsuario(usuario);
            
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
