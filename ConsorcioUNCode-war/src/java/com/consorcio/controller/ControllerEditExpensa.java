/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.ExpensaServiceBean;
import com.consorcio.entity.Expensa;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author victo
 */
@ManagedBean
@ViewScoped
public class ControllerEditExpensa {

    /**
     * Creates a new instance of ControllerEditExpensa
     */
    private @EJB ExpensaServiceBean expensaService;

    private String casoDeUso;
    private Expensa expensa;
    private double importe;
    private Date fechaDesde;
    private Date fechaHasta;
    private boolean disableButton;
    
    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            expensa = (Expensa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("EXPENSA");

            if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {
                importe = expensa.getImporte();
                fechaDesde = expensa.getFechaDesde();
                fechaHasta = expensa.getFechaHasta();

                if (casoDeUso.equals("CONSULTAR")) {
                    disableButton = true;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String aceptar() {
        try {
            if (casoDeUso.equals("ALTA")) {
                expensaService.crearExpensa(fechaDesde,fechaHasta,importe);
            } else if (casoDeUso.equals("MODIFICAR")) {
                expensaService.modificarExpensa(expensa.getId(),fechaDesde,fechaHasta,importe);
            }

            return "listarExpensa";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String cancelar(){
        return "listarExpensa";
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public Expensa getExpensa() {
        return expensa;
    }

    public void setExpensa(Expensa expensa) {
        this.expensa = expensa;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public boolean isDisableButton() {
        return disableButton;
    }

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }
    
    
    
}
