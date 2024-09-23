/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.LocalidadServiceBean;
import com.consorcio.entity.Localidad;
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
 * @author victo
 */
@ManagedBean
@ViewScoped
public class ControllerLocalidad implements Serializable{

    /**
     * Creates a new instance of ControllerLocalidad
     */
    private @EJB
    LocalidadServiceBean localidadService;
    private List<Localidad> localidades = new ArrayList<>();
    private Localidad localidadEliminar;

    @PostConstruct
    public void init() {
        listarLocalidades();
    }

    public Collection<Localidad> getLocalidades() {
        return localidades;
    }

    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarLocalidad";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void prepararBaja(Localidad localidad) {
        this.localidadEliminar = localidad;
    }

    public void baja() {
        
        if (localidadEliminar != null) {
            try {
                localidadService.eliminarLocalidad(localidadEliminar.getId());
                listarLocalidades();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public String modificar(Localidad localidad) {
        try {
            guardarSession("MODIFICAR", localidad);
            return "modificarLocalidad";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String consultar(Localidad localidad) {
        try {
            guardarSession("CONSULTAR", localidad);
            return "modificarLocalidad";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarSession(String casoDeUso, Localidad localidad) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("LOCALIDAD", localidad);
    }

    public void listarLocalidades() {
        try {
            localidades.clear();
            localidades.addAll(localidadService.listarLocalidades());

            // Ordenar alfabéticamente por el nombre de la provincia usando un Comparator
            Collections.sort(localidades, new Comparator<Localidad>() {
                @Override
                public int compare(Localidad l1, Localidad l2) {
                    return l1.getNombre().compareToIgnoreCase(l2.getNombre());
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }
    
}
