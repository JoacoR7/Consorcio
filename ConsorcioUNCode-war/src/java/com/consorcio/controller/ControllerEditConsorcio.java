/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.ConsorcioServiceBean;
import com.consorcio.business.DepartamentoServiceBean;
import com.consorcio.business.DireccionServiceBean;
import com.consorcio.business.LocalidadServiceBean;
import com.consorcio.business.PaisServiceBean;
import com.consorcio.business.ProvinciaServiceBean;
import com.consorcio.entity.Consorcio;
import com.consorcio.entity.Departamento;
import com.consorcio.entity.Direccion;
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
public class ControllerEditConsorcio {

    /**
     * Creates a new instance of ControllerEditConsorcio
     */
    private @EJB
    ConsorcioServiceBean consorcioService;
    private @EJB
    LocalidadServiceBean localidadService;
    private @EJB
    DepartamentoServiceBean departamentoService;
    private @EJB
    ProvinciaServiceBean provinciaService;
    private @EJB
    PaisServiceBean paisService;
    private @EJB
    DireccionServiceBean direccionService;

    private String casoDeUso;
    private Consorcio consorcio;
    private boolean disableButton;
    private String idLocalidad;
    private String idProvincia;
    private String idPais;
    private String idDepartamento;
    private String nombre;
    private String calle;
    private String numeracion;
    private String barrio;
    private String pisoCasa;
    private String puertaManzana;
    private String coordenadaX;
    private String coordenadaY;
    private String observacion;
    private Collection<SelectItem> localidades = new ArrayList();
    private Collection<SelectItem> departamentos = new ArrayList();
    private Collection<SelectItem> provincias = new ArrayList();
    private Collection<SelectItem> paises = new ArrayList();

    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            consorcio = (Consorcio) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONSORCIO");
            
            if (casoDeUso.equals("ALTA")) {
                comboPaises();
                comboProvincias("");
                comboDepartamentos("");
                comboLocalidades("");
            } else if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {
                nombre = consorcio.getNombre();
                calle = consorcio.getDireccion().getCalle();
                numeracion = consorcio.getDireccion().getNumeracion();
                barrio = consorcio.getDireccion().getBarrio();
                pisoCasa = consorcio.getDireccion().getPisoCasa();
                puertaManzana = consorcio.getDireccion().getPuertaManzana();
                coordenadaX = consorcio.getDireccion().getCoordenadaX();
                coordenadaY = consorcio.getDireccion().getCoordenadaY();
                observacion = consorcio.getDireccion().getObservacion();
                idLocalidad = consorcio.getDireccion().getLocalidad().getId();
                idDepartamento = consorcio.getDireccion().getLocalidad().getDepartamento().getId();
                idProvincia = consorcio.getDireccion().getLocalidad().getDepartamento().getProvincia().getId();
                idPais = consorcio.getDireccion().getLocalidad().getDepartamento().getProvincia().getPais().getId();
                comboPaises();
                comboProvincias(idPais);
                comboDepartamentos(idProvincia);
                comboLocalidades(idDepartamento);
                if (casoDeUso.equals("CONSULTAR")) {
                    disableButton = true;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    public String search() throws Exception{
        Localidad localidad = localidadService.buscarLocalidad(idLocalidad);
        Provincia provincia = provinciaService.buscarProvincia(idProvincia);
        return direccionService.search(calle, numeracion, localidad.getNombre(),localidad.getCodigoPostal(), provincia.getNombre());
    }
    
    public void comboPaisProvinciaDepartamentoLocalidad() {
        provincias.clear();
        departamentos.clear();
        localidades.clear();
        comboProvincias(idPais);
        comboDepartamentos(idProvincia);
        comboLocalidades(idDepartamento);
        RequestContext.getCurrentInstance().update("provincia");
        RequestContext.getCurrentInstance().update("departamento");
    }

    public void comboProvinciaDepartamentoLocalidad() {
        departamentos.clear();
        localidades.clear();
        comboDepartamentos(idProvincia);
        comboLocalidades(idDepartamento);
        RequestContext.getCurrentInstance().update("departamento");
    }

    public void comboDepartamentoLocalidad() {
        localidades.clear();
        comboLocalidades(idDepartamento);
        RequestContext.getCurrentInstance().update("departamento");
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

    public void comboLocalidades(String idDepartamento) {
        try {
            localidades = new ArrayList<SelectItem>();
            localidades.add(new SelectItem(null, "Seleccione..."));
            for (Localidad localidad : localidadService.listarLocalidadesPorDpto(idDepartamento)) {
                localidades.add(new SelectItem(localidad.getId(), localidad.getNombre()));
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String aceptar() {
        try {
            if (casoDeUso.equals("ALTA")) {
                consorcioService.crearConsorcio(nombre,
                        direccionService.crearDireccion(idLocalidad, calle, numeracion, barrio, pisoCasa,
                                puertaManzana, coordenadaX,
                                coordenadaY, observacion));

            } else if (casoDeUso.equals("MODIFICAR")) {
                consorcioService.modificarConsorcio(consorcio.getId(), nombre,
                        direccionService.crearDireccion(idLocalidad, calle, numeracion,
                                barrio, pisoCasa, puertaManzana, coordenadaX,
                                coordenadaY, observacion));
            }

            return "listarConsorcio";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String cancelar(){
        return "listarConsorcio";
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public boolean isDisableButton() {
        return disableButton;
    }

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }

    public String getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(String idLocalidad) {
        this.idLocalidad = idLocalidad;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getPisoCasa() {
        return pisoCasa;
    }

    public void setPisoCasa(String pisoCasa) {
        this.pisoCasa = pisoCasa;
    }

    public String getPuertaManzana() {
        return puertaManzana;
    }

    public void setPuertaManzana(String puertaManzana) {
        this.puertaManzana = puertaManzana;
    }

    public String getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(String coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public String getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(String coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Collection<SelectItem> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(Collection<SelectItem> localidades) {
        this.localidades = localidades;
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

    
    
}
