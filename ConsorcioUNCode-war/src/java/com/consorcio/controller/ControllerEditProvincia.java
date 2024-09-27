/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.ErrorServiceException;
import com.consorcio.business.PaisServiceBean;
import com.consorcio.business.ProvinciaServiceBean;
import com.consorcio.controller.messages.Messages;
import com.consorcio.controller.messages.TypeMessages;
import com.consorcio.entity.Pais;
import com.consorcio.entity.Provincia;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author victo
 */
@ManagedBean
@ViewScoped
public class ControllerEditProvincia {

    /**
     * Creates a new instance of ControllerEditProvincia
     */
    
    private @EJB ProvinciaServiceBean provinciaService;
    private @EJB PaisServiceBean paisService;

    private String casoDeUso;
    private Provincia provincia;
    private String nombre;
    private boolean disableButton;
    private String idPais;
    private Collection<SelectItem> paises=new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            provincia = (Provincia) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PROVINCIA");

            if (casoDeUso.equals("ALTA")) {
                comboPaises();
            } else if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {
                nombre = provincia.getNombre();
                idPais = provincia.getPais().getId(); // Pais actual de la provincia seleccionada
                comboPaises(); 

                if (casoDeUso.equals("CONSULTAR")) {
                    disableButton = true;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void comboPaises(){
      try{  
        
            paises = new ArrayList<SelectItem>();
            paises.add(new SelectItem(null, "Seleccione..."));
            for(Pais pais: paisService.listarPais()){
              paises.add(new SelectItem(pais.getId(), pais.getNombre()));
            }
                
      }catch(Exception e){
        e.getMessage();
      }
    }


    public String aceptar() {
        try {
            if (casoDeUso.equals("ALTA")) {
                provinciaService.crearProvincia(nombre,idPais);
            } else if (casoDeUso.equals("MODIFICAR")) {
                provinciaService.modificarProvincia(provincia.getId(), nombre);
            }

            return "listarProvincia";
        
        }catch (ErrorServiceException e){
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }
    public String cancelar(){
        return "listarProvincia";
    }
    public boolean isDisableButton() {
        return disableButton;
    }

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public PaisServiceBean getPaisService() {
        return paisService;
    }

    public void setPaisService(PaisServiceBean paisService) {
        this.paisService = paisService;
    }

    public Collection<SelectItem> getPaises() {
        return paises;
    }

    public void setPaises(Collection<SelectItem> paises) {
        this.paises = paises;
    }


    
    
}
