/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.ExpensaServiceBean;
import com.consorcio.entity.Expensa;
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
public class ControllerExpensa {

    /**
     * Creates a new instance of ControllerExpensa
     */
    private @EJB
    ExpensaServiceBean expensaService;
    private List<Expensa> expensas = new ArrayList<>();
    private Expensa expensaEliminar;

    @PostConstruct
    public void init() {
        listarExpensas();
    }

    public Collection<Expensa> getExpensas() {
        return expensas;
    }

    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarExpensa";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void prepararBaja(Expensa expensa) {
        this.expensaEliminar = expensa;
    }

    public void baja() {
        
        if (expensaEliminar != null) {
            try {
                expensaService.eliminarExpensa(expensaEliminar.getId());
                listarExpensas();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public String modificar(Expensa expensa) {
        try {
            guardarSession("MODIFICAR", expensa);
            return "modificarExpensa";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String consultar(Expensa expensa) {
        try {
            guardarSession("CONSULTAR", expensa);
            return "modificarExpensa";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarSession(String casoDeUso, Expensa expensa) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("EXPENSA", expensa);
    }

    public void listarExpensas() {
        try {
            expensas.clear();
            expensas.addAll(expensaService.listarExpensas());

        } catch (Exception e) {
            throw e;
        }
    }
}
