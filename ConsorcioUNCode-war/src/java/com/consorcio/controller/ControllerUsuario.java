/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.UsuarioServiceBean;
import com.consorcio.entity.Usuario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author joaqu
 */
@ManagedBean (name = "ControllerUsuario")
@ViewScoped
public class ControllerUsuario {
    private @EJB UsuarioServiceBean usuarioService;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String telefono;
    private String usuario;
    private String clave;
    private boolean eliminado;
    private List<Usuario> usuarios = new ArrayList<Usuario>();
    private Usuario usuarioAEliminar;
    
    @PostConstruct
    public void init(){
        listarUsuario();
    }
    
    public void listarUsuario(){
        try {
            usuarios.clear();
            usuarios.addAll(usuarioService.listarUsuarios());
            
            Collections.sort(usuarios, new Comparator<Usuario>(){
                
                @Override
                public int compare(Usuario o1, Usuario o2) {
                    return o1.getApellido().compareToIgnoreCase(o2.getApellido());
                }
                
            });
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void guardarUsuario(){
        try {
            usuarioService.crearUsuario(nombre, apellido, correoElectronico, telefono, usuario, clave);
            listarUsuario();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarUsuario";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void prepararBaja(Usuario usuario){
        this.usuarioAEliminar = usuario;
    }
    
    public void baja(){
        if(usuarioAEliminar != null){
            try {
                usuarioService.eliminarUsuario(usuarioAEliminar.getUsuario());
                listarUsuario();
                usuarioAEliminar = null;
            } catch (Exception e) {
                throw new IllegalArgumentException("El usuario no fue encontrado");
            }
        }
    }
    
    public String modificar(Usuario usuario){
        try {
            guardarSession("MODIFICAR", usuario);
            return "modificarUsuario";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String consultar(Usuario usuario){
        try {
            guardarSession("CONSULTAR", usuario);
            return "modificarUsuario";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void guardarSession(String casoDeUso, Usuario usuario) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("USUARIO", usuario);
    }

    public UsuarioServiceBean getUsuarioService() {
        return usuarioService;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Usuario getUsuarioAEliminar() {
        return usuarioAEliminar;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getClave() {
        return clave;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
    
    
}
