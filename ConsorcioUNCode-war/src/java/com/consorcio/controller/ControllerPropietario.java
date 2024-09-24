/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.PropietarioServiceBean;
import com.consorcio.entity.Direccion;
import com.consorcio.entity.Propietario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
@ManagedBean
@ViewScoped
public class ControllerPropietario implements Serializable{
    private @EJB PropietarioServiceBean propietarioService;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String telefono;
    private boolean habitaConsorcio;
    private List<Propietario> propietarios = new ArrayList<Propietario>();
    private boolean eliminado;
    private Propietario propietarioAEliminar;
    
    @PostConstruct
    public void init(){
        listarPropietario();
    }
    
    public void listarPropietario() {
        try {
            propietarios.clear();
            propietarios.addAll(propietarioService.listarPropietarios());
            
            Collections.sort(propietarios, new Comparator<Propietario>() {

                @Override
                public int compare(Propietario o1, Propietario o2) {
                    return o1.getApellido().compareToIgnoreCase(o2.getApellido());
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }
    
    public String alta(){
        try {
            guardarSession("ALTA", null);
            return "modificarPropietario";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void prepararBaja(Propietario propietario){
        this.propietarioAEliminar = propietario;
    }
    
    public void baja() {
        if(propietarioAEliminar != null){
            try {
                propietarioService.eliminarPropietario(propietarioAEliminar.getId());
                listarPropietario();
                propietarioAEliminar = null;
            } catch (Exception e) {
                throw new IllegalArgumentException("El propietario no fue encontrado.");
            }
        }
    }
    
    public String modificar(Propietario propietario) {
        try {
            guardarSession("MODIFICAR", propietario);
            return "modificarPropietario";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String consultar(Propietario propietario) {
        try {
            guardarSession("CONSULTAR", propietario);
            return "modificarPropietario";
        } catch (Exception e) {
            return null;
        }
    }
    
    private void guardarSession(String casoDeUso, Propietario propietario) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("PROPIETARIO", propietario);
    }

    public PropietarioServiceBean getPropietarioService() {
        return propietarioService;
    }

    public void setPropietarioService(PropietarioServiceBean propietarioService) {
        this.propietarioService = propietarioService;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isHabitaConsorcio() {
        return habitaConsorcio;
    }

    public void setHabitaConsorcio(boolean habitaConsorcio) {
        this.habitaConsorcio = habitaConsorcio;
    }

    public List<Propietario> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<Propietario> propietarios) {
        this.propietarios = propietarios;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Propietario getPropietarioAEliminar() {
        return propietarioAEliminar;
    }

    public void setPropietarioAEliminar(Propietario propietarioAEliminar) {
        this.propietarioAEliminar = propietarioAEliminar;
    }

    
    
}
