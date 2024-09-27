/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.ErrorServiceException;
import com.consorcio.business.ExpensaInmuebleServiceBean;
import com.consorcio.entity.ExpensaInmueble;
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
public class ControllerExpensaInmueble {

    /**
     * Creates a new instance of ControllerExpensaInmueble
     */
    private @EJB
    ExpensaInmuebleServiceBean expensaInmuebleService;
    private List<ExpensaInmueble> expensasInmuebles = new ArrayList<>();
    private ExpensaInmueble expensaInmuebleEliminar;

    @PostConstruct
    public void init() {
        listarExpensasInmuebles();
    }

    public Collection<ExpensaInmueble> getExpensasInmuebles() {
        return expensasInmuebles;
    }

    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarExpensaInmueble";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void prepararBaja(ExpensaInmueble expensaInmueble) {
        this.expensaInmuebleEliminar = expensaInmueble;
    }

    public void baja() throws Exception {
        
        if (expensaInmuebleEliminar != null) {
            try {
                expensaInmuebleService.eliminarExpensaInmueble(expensaInmuebleEliminar.getId());
                listarExpensasInmuebles();
            } catch (ErrorServiceException e) {
                e.getMessage();
            }catch(Exception ex){
                throw ex;
            }
        }
    }

    public String modificar(ExpensaInmueble expensaInmueble) {
        try {
            guardarSession("MODIFICAR", expensaInmueble);
            return "modificarExpensaInmueble";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String consultar(ExpensaInmueble expensaInmueble) {
        try {
            guardarSession("CONSULTAR", expensaInmueble);
            return "modificarExpensaInmueble";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarSession(String casoDeUso, ExpensaInmueble expensaInmueble) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("EXPENSAINMUEBLE", expensaInmueble);
    }

    public void listarExpensasInmuebles()  {
        try {
            expensasInmuebles.clear();
            expensasInmuebles.addAll(expensaInmuebleService.listarExpensaInmueble());

        } catch (Exception e) {
            e.getMessage();
        }
    }
    
}
