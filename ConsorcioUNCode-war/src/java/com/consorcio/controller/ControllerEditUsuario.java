/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.UsuarioServiceBean;
import com.consorcio.entity.Usuario;
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
@ManagedBean
@ViewScoped
public class ControllerEditUsuario {

    private @EJB
    UsuarioServiceBean usuarioService;

    private String casoDeUso;
    private Usuario usuario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correoElectronico;
    private String nombreUsuario;
    private String clave;
    private boolean disableButton;

    @PostConstruct
    public void init() {
        casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
        usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("USUARIO");
        if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {
            nombre = usuario.getNombre();
            apellido = usuario.getApellido();
            telefono = usuario.getTelefono();
            correoElectronico = usuario.getCorreoElectronico();
            nombreUsuario = usuario.getUsuario();
            clave = usuario.getClave();
        }
        if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("ALTA")) {
            setDisableButton(false);
        } else {
            setDisableButton(true);
        }
    }

    public String aceptar() {
        try {
            if (casoDeUso.equals("ALTA")) {
                usuarioService.crearUsuario(nombre, apellido, correoElectronico, telefono, nombreUsuario, clave);
            } else {
                usuarioService.modificarUsuario(nombre, apellido, correoElectronico, telefono, nombreUsuario, clave);
            }
            limpiarSession();
            return "listarUsuario";

        } catch (Exception e) {
            return null;
        }
    }

    public void limpiarSession() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.removeAttribute("ACCION");
        session.removeAttribute("INQUILINO");
    }

    public UsuarioServiceBean getUsuarioService() {
        return usuarioService;
    }

    public void setUsuarioService(UsuarioServiceBean usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public boolean isDisableButton() {
        return disableButton;
    }

}
