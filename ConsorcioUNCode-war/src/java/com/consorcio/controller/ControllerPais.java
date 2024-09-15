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

@ManagedBean(name = "paisController")
@SessionScoped
public class ControllerPais implements Serializable {
    
    private @EJB PaisServiceBean paisService;
    private Collection<Pais> paises = new ArrayList<>();
    private String nombrePais;
    private String nombrePaisModificado;
    private boolean eliminado; // Atributo eliminado

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

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Collection<Pais> getPaises() {
        return paises;
    }

    public void guardarPais() {
        try {
            paisService.crearPais(nombrePais); 
        } catch (Exception e) {
            e.printStackTrace();
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
    
    public void buscarPais(){}

    public void actualizarPais(){
        try {
            Pais pais = paisService.buscarPaisPorNombre(nombrePais);
            paisService.eliminarPais(nombrePais);
        } catch (Exception e) {
            // Manejo de excepciones
        }
    }
}
