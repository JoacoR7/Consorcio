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

    private @EJB
    PaisServiceBean paisService;
    private Collection<Pais> paises = new ArrayList<>();
    private String nombrePais;
    private String nombrePaisModificado;
    private boolean paisNoEncontrado = true;
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

    public String getNombrePaisModificado() {
        return nombrePaisModificado;
    }

    public void setNombrePaisModificado(String nombrePaisModificado) {
        this.nombrePaisModificado = nombrePaisModificado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public boolean isPaisNoEncontrado() {
        return paisNoEncontrado;
    }

    public void setPaisNoEncontrado(boolean paisNoEncontrado) {
        this.paisNoEncontrado = paisNoEncontrado;
    }

    public Collection<Pais> getPaises() {
        return paises;
    }

    public void guardarPais() {
        try {
            paisService.crearPais(nombrePais.toUpperCase());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void listarPais() {
        try {
            paises.clear();
            paises.addAll(paisService.listarPais());
        } catch (Exception e) {
            throw e;
        }
    }

    public void buscarPais() {
        Pais pais = paisService.buscarPaisPorNombre(nombrePais.toUpperCase());
        if (pais == null) {
            setNombrePaisModificado("No existe");
            setPaisNoEncontrado(true);
        } else {
            setNombrePaisModificado(nombrePais.toUpperCase());
            setPaisNoEncontrado(false);
        }
    }

    public void actualizarPais() {
        try {
            Pais pais = paisService.buscarPaisPorNombre(nombrePais.toUpperCase());

            if (pais == null) {
                throw new IllegalArgumentException("El país no fue encontrado.");
            }

            paisService.modificarPais(pais.getId(), nombrePaisModificado.toUpperCase());
        } catch (Exception e) {
            // Manejo de excepciones
        }
    }
    
    public void eliminarPais() {
        try {
            Pais pais = paisService.buscarPaisPorNombre(nombrePais.toUpperCase());
            if (pais == null) {
                throw new IllegalArgumentException("El país no fue encontrado.");
            }
            paisService.eliminarPais(pais.getId());
            
        } catch (Exception e) {
        }
    }

}
