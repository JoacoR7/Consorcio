package com.consorcio.controller;

import com.consorcio.business.PaisServiceBean;
import com.consorcio.entity.Pais;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "paisController")
@SessionScoped
public class ControllerPais implements Serializable {
    
    //Servicio Capa de Negocio
    private @EJB PaisServiceBean paisService;
    
    //Variable Capa de Negocio
    private Collection<Pais> paises=new ArrayList();
    private String nombrePais;
    
    @PostConstruct
    public void init() {
        listarPais();
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
    
    public Collection<Pais> getPaises() {
        return paises;
    }

    public void guardarPais() {
        try {
            paisService.crearPais(nombrePais); // Método para guardar el país en la base de datos
            // Agrega mensajes de éxito o redirección si es necesario
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción y mostrar mensaje de error
        }
    }
    
    public void listarPais(){
        try {
            paises.clear();
            paises.addAll(paisService.listarPais());
        } catch (Exception e) {
            throw e;
        }
    }
}
