/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.DepartamentoServiceBean;
import com.consorcio.business.LocalidadServiceBean;
import com.consorcio.business.PaisServiceBean;
import com.consorcio.business.ProvinciaServiceBean;
import com.consorcio.entity.Departamento;
import com.consorcio.entity.Localidad;
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
public class ControllerEditLocalidad {

    /**
     * Creates a new instance of ControllerEditLocalidad
     */
    private @EJB
    LocalidadServiceBean localidadService;
    private @EJB
    DepartamentoServiceBean departamentoService;
    private @EJB
    ProvinciaServiceBean provinciaService;
    private @EJB
    PaisServiceBean paisService;

    private String casoDeUso;
    private Localidad localidad;
    private String nombre;
    private String codigoPostal;
    private boolean disableButton;
    private String idProvincia;
    private String idPais;
    private String idDepartamento;
    private Collection<SelectItem> departamentos = new ArrayList();
    private Collection<SelectItem> provincias = new ArrayList();
    private Collection<SelectItem> paises = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            localidad = (Localidad) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LOCALIDAD");

            if (casoDeUso.equals("ALTA")) {
                comboPaises();
                comboProvincias("");
                comboDepartamentos("");
            } else if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {
                nombre = localidad.getNombre();
                codigoPostal = localidad.getCodigoPostal();
                idDepartamento = localidad.getDepartamento().getId();
                idProvincia = localidad.getDepartamento().getProvincia().getId();
                idPais = localidad.getDepartamento().getProvincia().getPais().getId();

                comboPaises();
                comboProvincias(idPais);
                comboDepartamentos(idProvincia);
                if (casoDeUso.equals("CONSULTAR")) {
                    disableButton = true;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void comboPaisProvinciaDepartamento() {
        provincias.clear();
        departamentos.clear();
        comboProvincias(idPais);
        comboDepartamentos("");
        RequestContext.getCurrentInstance().update("@form:provincia");
        RequestContext.getCurrentInstance().update("@form:departamento");
    }

    public void comboProvinciaDepartamento() {
        provincias.clear();
        departamentos.clear();
        comboDepartamentos(idProvincia);
        RequestContext.getCurrentInstance().update("@form:provincia");
        RequestContext.getCurrentInstance().update("@form:departamento");
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
            provincias = new ArrayList<SelectItem>();
            provincias.add(new SelectItem(null, "Seleccione..."));
            for (Provincia provincia : provinciaService.listarProvinciasPorPais(idPais)) {
                provincias.add(new SelectItem(provincia.getId(), provincia.getNombre()));
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void comboDepartamentos(String idProvincia) {
        try {
            departamentos = new ArrayList<SelectItem>();
            departamentos.add(new SelectItem(null, "Seleccione..."));
            for (Departamento departamento : departamentoService.listarDepartamentosPorProvincia(idProvincia)) {
                departamentos.add(new SelectItem(departamento.getId(), departamento.getNombre()));
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String aceptar() {
        try {
            if (casoDeUso.equals("ALTA")) {
                localidadService.crearLocalidad(nombre, codigoPostal, idDepartamento);
            } else if (casoDeUso.equals("MODIFICAR")) {
                localidadService.modificarLocalidad(localidad.getId(), nombre, codigoPostal, idDepartamento);
            }

            return "listarLocalidad";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String cancelar() {
        return "listarLocalidad";
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

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public boolean isDisableButton() {
        return disableButton;
    }

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }

    public Collection<SelectItem> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(Collection<SelectItem> departamentos) {
        this.departamentos = departamentos;
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

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    
}
