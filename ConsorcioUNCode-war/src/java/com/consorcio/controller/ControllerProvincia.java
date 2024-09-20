package com.consorcio.controller;

import com.consorcio.business.PaisServiceBean;
import com.consorcio.business.ProvinciaServiceBean;
import com.consorcio.entity.Pais;
import com.consorcio.entity.Provincia;
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

@ManagedBean
@ViewScoped
public class ControllerProvincia implements Serializable {

    private @EJB ProvinciaServiceBean provinciaService;
    private @EJB PaisServiceBean paisService;
    private List<Provincia> provincias = new ArrayList<>();
    private String nombreProvincia;
    private String nombreProvinciaModificado;
    private boolean provinciaNoEncontrado = true;
    private boolean eliminado; // Atributo eliminado
    private String idPais;

    @PostConstruct
    public void init() {
        listarProvincias();
    }

    public String getNombreProvincia() {
        return nombreProvincia;
    }

    public void setNombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }

    public String getNombreProvinciaModificado() {
        return nombreProvinciaModificado;
    }

    public void setNombreProvinciaModificado(String nombreProvinciaModificado) {
        this.nombreProvinciaModificado = nombreProvinciaModificado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public boolean isProvinciaNoEncontrado() {
        return provinciaNoEncontrado;
    }

    public void setProvinciaNoEncontrado(boolean provinciaNoEncontrado) {
        this.provinciaNoEncontrado = provinciaNoEncontrado;
    }

    public Collection<Provincia> getProvincias() {
        return provincias;
    }

    public String alta(){
        try {
            guardarSession("ALTA", null);
            return "modificarProvincia";  
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public void baja(Provincia provincia){
        try {
            provinciaService.eliminarProvincia(provincia.getId());
            listarProvincias();
        } catch (Exception e){
            e.getMessage();
        }
    }
    
    public String modificar(Provincia provincia){
        try {
            guardarSession("MODIFICAR", provincia);
            return "modificarProvincia";  
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String consultar(Provincia provincia){
        try {
            guardarSession("CONSULTAR", provincia);
            return "modificarProvincia";  
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void guardarSession(String casoDeUso, Provincia provincia){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());  
        session.setAttribute("PROVINCIA", provincia);  
    }
    

    public void listarProvincias() {
        try {
            provincias.clear();
            provincias.addAll(provinciaService.listarProvincias());

            // Ordenar alfabéticamente por el nombre de la provincia usando un Comparator
            Collections.sort(provincias, new Comparator<Provincia>() {
                @Override
                public int compare(Provincia p1, Provincia p2) {
                    return p1.getNombre().compareToIgnoreCase(p2.getNombre());
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }
}