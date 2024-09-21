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
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "ControllerPais")
@ViewScoped
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
 
    public String alta(){
        try {
            guardarSession("ALTA", null);
            return "modificarPais";  
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public void baja(Pais pais){
        try {
            paisService.eliminarPais(pais.getId());
            listarPais();
        } catch (Exception e){
            throw new IllegalArgumentException("El país no fue encontrado.");
        }
    }
    
    public String modificar(Pais pais){
        try {
            guardarSession("MODIFICAR", pais);
            return "modificarPais";  
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String consultar(Pais pais){
        try {
            guardarSession("CONSULTAR", pais);
            return "modificarPais";  
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    private void guardarSession(String casoDeUso, Pais pais){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());  
        session.setAttribute("PAIS", pais);  
    }
    
    
}