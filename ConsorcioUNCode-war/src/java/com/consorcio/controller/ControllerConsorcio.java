/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.ConsorcioServiceBean;
import com.consorcio.entity.Consorcio;
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

/**
 *
 * @author victo
 */
@ManagedBean
@ViewScoped
public class ControllerConsorcio {

    /**
     * Creates a new instance of ControllerConsorcio
     */
    @EJB private ConsorcioServiceBean consorcioService;
    private Consorcio consorcioEliminar;
    private List<Consorcio> consorcios = new ArrayList<>();
    
    @PostConstruct
    public void init() {
        listarConsorcio();
    }

    public Collection<Consorcio> getConsorcios() {
        return consorcios;
    }

    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarConsorcio";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void prepararBaja(Consorcio consorcio) {
        this.consorcioEliminar = consorcio;
    }

    public void baja() {
        
        if (consorcioEliminar != null) {
            try {
                consorcioService.eliminarConsorcio(consorcioEliminar.getId());
                listarConsorcio();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public String modificar(Consorcio consorcio) {
        try {
            guardarSession("MODIFICAR", consorcio);
            return "modificarConsorcio";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String consultar(Consorcio consorcio) {
        try {
            guardarSession("CONSULTAR", consorcio);
            return "modificarConsorcio";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarSession(String casoDeUso, Consorcio consorcio) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("CONSORCIO", consorcio);
    }

    public void listarConsorcio() {
        try {
            consorcios.clear();
            consorcios.addAll(consorcioService.listarConsorcio());

        } catch (Exception e) {
            throw e;
        }
    }
    
}
