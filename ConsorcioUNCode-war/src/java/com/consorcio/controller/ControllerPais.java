package com.consorcio.controller;

import com.consorcio.business.PaisServiceBean;
import com.consorcio.entity.Pais;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "paisController")
@SessionScoped
public class ControllerPais implements Serializable {

    private @EJB
    PaisServiceBean paisService;
    private List<Pais> paises = new ArrayList<>();
    private String nombrePais;
    private String nombrePaisModificado;
    private boolean paisNoEncontrado = true;
    private boolean eliminado; // Atributo eliminado
    private String idPais;

    @PostConstruct
    public void init() {
        listarPais();
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
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
            listarPais();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void actualizarContexto(ControllerSession session){
    
        Pais pais = (Pais) session.getEntidadSeleccionada("Pais");
        setNombrePais(pais.getNombre());
        setIdPais(pais.getId());
    }
    public void listarPais() {
        try {
            paises.clear();
            paises.addAll(paisService.listarPais());

            // Ordenar alfabéticamente por el nombre del país usando un Comparator
            Collections.sort(paises, new Comparator<Pais>() {
                @Override
                public int compare(Pais p1, Pais p2) {
                    return p1.getNombre().compareToIgnoreCase(p2.getNombre());
                }
            });

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
            paisService.modificarPais(idPais , nombrePaisModificado.toUpperCase());
        } catch (Exception e) {
             throw new IllegalArgumentException("El país no fue encontrado.");
            // Manejo de excepciones
        }
    }

    public void eliminarPais() {
        try {
            paisService.eliminarPais(idPais);

        } catch (Exception e) {
            throw new IllegalArgumentException("El país no fue encontrado.");
        }
    }

}