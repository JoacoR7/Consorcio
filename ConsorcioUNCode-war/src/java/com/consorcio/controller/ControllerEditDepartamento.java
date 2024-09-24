/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.DepartamentoServiceBean;
import com.consorcio.business.PaisServiceBean;
import com.consorcio.business.ProvinciaServiceBean;
import com.consorcio.entity.Departamento;
import com.consorcio.entity.Pais;
import com.consorcio.entity.Provincia;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

/**
 *
 * @author victo
 */
@ManagedBean
@ViewScoped
public class ControllerEditDepartamento {

    /**
     * Creates a new instance of ControllerEditDepartamento
     */
    private @EJB
    DepartamentoServiceBean departamentoService;
    private @EJB
    ProvinciaServiceBean provinciaService;
    private @EJB PaisServiceBean paisService;

    private String casoDeUso;
    private Departamento departamento;
    private String nombre;
    private boolean disableButton;
    private String idProvincia;
    private String idPais;
    private Collection<SelectItem> provincias = new ArrayList();
    private Collection<SelectItem> paises = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            departamento = (Departamento) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("DEPARTAMENTO");

            if (casoDeUso.equals("ALTA")) {
                comboPaises();
                comboProvincias("");
            } else if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {
                nombre = departamento.getNombre();
                idProvincia = departamento.getProvincia().getId(); 
                idPais = departamento.getProvincia().getPais().getId();
                comboPaises();
                comboProvincias(idPais);
                if (casoDeUso.equals("CONSULTAR")) {
                    disableButton = true;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    public void comboPaisProvincia(){
        provincias.clear();
        comboProvincias(idPais);
        RequestContext.getCurrentInstance().update("provincia");

    }

    public void comboPaises() {
        try {

            paises = new ArrayList<SelectItem>();
            paises.add(new SelectItem(null, "Seleccione..."));
            for (Pais pais : paisService.listarPais()) {
                paises.add(new SelectItem(pais.getId(), pais.getNombre()));
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void comboProvincias(String idPais) {
        try {
            System.out.println("El id del pais es: "+ idPais);
            provincias = new ArrayList<SelectItem>();
            provincias.add(new SelectItem(null, "Seleccione..."));
            for (Provincia provincia : provinciaService.listarProvinciasPorPais(idPais)) {
                System.out.println("Provincia actual: "+ provincia);
                provincias.add(new SelectItem(provincia.getId(), provincia.getNombre()));
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String aceptar() {
        try {
            if (casoDeUso.equals("ALTA")) {
                departamentoService.crearDepartamento(nombre, idProvincia);
            } else if (casoDeUso.equals("MODIFICAR")) {
                departamentoService.modificarDepartamento(nombre, departamento.getId(), idProvincia);
            }

            return "listarDepartamento";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String cancelar() {
        return "listarDepartamento";
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isDisableButton() {
        return disableButton;
    }

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }

    public String getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public Collection<SelectItem> getProvincias() {
        return provincias;
    }

    public void setProvincias(Collection<SelectItem> provincias) {
        this.provincias = provincias;
    }

    public Collection<SelectItem> getPaises() {
        return paises;
    }

    public void setPaises(Collection<SelectItem> paises) {
        this.paises = paises;
    }

}
