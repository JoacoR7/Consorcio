package com.consorcio.controller;

import com.consorcio.business.InmuebleServiceBean;
import com.consorcio.business.PropietarioServiceBean;
import com.consorcio.business.InquilinoServiceBean;
import com.consorcio.entity.Inmueble;
import com.consorcio.entity.Propietario;
import com.consorcio.entity.Inquilino;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Collection;

@ManagedBean
@ViewScoped
public class ControllerEditInmueble {

    @EJB
    private InmuebleServiceBean inmuebleService;
    @EJB
    private PropietarioServiceBean propietarioService;
    @EJB
    private InquilinoServiceBean inquilinoService;

    private String casoDeUso;
    private Inmueble inmueble;
    private boolean disableButton;
    private String idPropietario;
    private String idInquilino;
    private String piso;
    private String puerta;
    private Collection<SelectItem> propietarios = new ArrayList<>();
    private Collection<SelectItem> inquilinos = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            inmueble = (Inmueble) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("INMUEBLE");

            if ("ALTA".equals(casoDeUso)) {
                loadPropietarios();
                loadInquilinos();
            } else if ("MODIFICAR".equals(casoDeUso) || "CONSULTAR".equals(casoDeUso)) {
                piso = inmueble.getPiso();
                puerta = inmueble.getPuerta();
                idPropietario = inmueble.getPropietario() != null ? inmueble.getPropietario().getId() : null;
                idInquilino = inmueble.getInquilino() != null ? inmueble.getInquilino().getId() : null;
                if ("CONSULTAR".equals(casoDeUso)) {
                    disableButton = true;
                }
                loadPropietarios();
                loadInquilinos();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPropietarios() {
        try {
            propietarios.clear();
            propietarios.add(new SelectItem(null, "Seleccione..."));
            for (Propietario propietario : propietarioService.listarPropietarios()) {
                propietarios.add(new SelectItem(propietario.getId(), propietario.getNombre()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInquilinos() {
        try {
            inquilinos.clear();
            inquilinos.add(new SelectItem(null, "Seleccione..."));
            for (Inquilino inquilino : inquilinoService.listarInquilinos()) {
                inquilinos.add(new SelectItem(inquilino.getId(), inquilino.getNombre()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String aceptar() {
        try {
            if ("ALTA".equals(casoDeUso)) {
                inmuebleService.crearInmueble(idPropietario, idInquilino, piso, puerta);
            } else if ("MODIFICAR".equals(casoDeUso)) {
                inmuebleService.modificarInmueble(inmueble.getId(), idPropietario, idInquilino, piso, puerta);
            }
            return "listarInmueble";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String cancelar() {
        return "listarInmueble";
    }

    // Getters and Setters
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

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public String getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(String idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(String idInquilino) {
        this.idInquilino = idInquilino;
    }

    public Collection<SelectItem> getPropietarios() {
        return propietarios;
    }

    public Collection<SelectItem> getInquilinos() {
        return inquilinos;
    }
}
