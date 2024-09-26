/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.ErrorServiceException;
import com.consorcio.business.PaisServiceBean;
import com.consorcio.controller.messages.Messages;
import com.consorcio.controller.messages.TypeMessages;
import com.consorcio.entity.Pais;
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
public class ControllerEditPais {

    /**
     * Creates a new instance of ControllerEditPais
     */
    private @EJB PaisServiceBean paisService;
    
    // Atributos que voy a recuperar con el FacesContext
    private String casoDeUso;
    private Pais pais;
    private String nombrePais;
    private boolean disableButton;
    
    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            pais = (Pais) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PAIS");

            if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")){
                nombrePais = pais.getNombre();
                if (casoDeUso.equals("CONSULTAR")){
                    disableButton = true;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    public String aceptar(){
    
        try {
            if (casoDeUso.equals("ALTA")){
                paisService.crearPais(nombrePais);
            } else {
                paisService.modificarPais(pais.getId(), nombrePais);
            }
            return "listarPais";
            
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }

    public String cancelar(){
        return "listarPais";
    }
    public boolean isDisableButton() {
        return disableButton;
    }

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }
    
    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
    
    
}
