package com.consorcio.controller;

import com.consorcio.business.InmuebleServiceBean;
import com.consorcio.entity.Inmueble;
import java.util.ArrayList;
import java.util.Collection;
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
public class ControllerInmueble {

    @EJB
    private InmuebleServiceBean inmuebleService;
    private Inmueble inmuebleEliminar;
    private List<Inmueble> inmuebles = new ArrayList<>();

    @PostConstruct
    public void init() {
        listarInmueble();
    }

    public Collection<Inmueble> getInmuebles() {
        return inmuebles;
    }

    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarInmueble";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void prepararBaja(Inmueble inmueble) {
        this.inmuebleEliminar = inmueble;
    }

    public void baja() {
        if (inmuebleEliminar != null) {
            try {
                inmuebleService.eliminarInmueble(inmuebleEliminar.getId());
                listarInmueble();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public String modificar(Inmueble inmueble) {
        try {
            guardarSession("MODIFICAR", inmueble);
            return "modificarInmueble";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public String consultar(Inmueble inmueble) {
        try {
            guardarSession("CONSULTAR", inmueble);
            return "modificarInmueble";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private void guardarSession(String casoDeUso, Inmueble inmueble) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("INMUEBLE", inmueble);
    }

    public void listarInmueble() {
        try {
            inmuebles.clear();
            inmuebles.addAll(inmuebleService.listarInmueble());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
