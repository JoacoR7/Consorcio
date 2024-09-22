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

    private @EJB
    ProvinciaServiceBean provinciaService;
    private List<Provincia> provincias = new ArrayList<>();
    private Provincia provinciaEliminar;

    @PostConstruct
    public void init() {
        listarProvincias();
    }

    public Collection<Provincia> getProvincias() {
        return provincias;
    }

    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarProvincia";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void prepararBaja(Provincia provincia) {
        this.provinciaEliminar = provincia;
    }

    public void baja() {
        
        if (provinciaEliminar != null) {
            try {
                provinciaService.eliminarProvincia(provinciaEliminar.getId());
                listarProvincias();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public String modificar(Provincia provincia) {
        try {
            guardarSession("MODIFICAR", provincia);
            return "modificarProvincia";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String consultar(Provincia provincia) {
        try {
            guardarSession("CONSULTAR", provincia);
            return "modificarProvincia";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarSession(String casoDeUso, Provincia provincia) {
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
