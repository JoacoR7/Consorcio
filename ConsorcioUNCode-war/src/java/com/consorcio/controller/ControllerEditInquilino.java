/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.InquilinoServiceBean;
import com.consorcio.controller.messages.Messages;
import com.consorcio.controller.messages.TypeMessages;
import com.consorcio.entity.Inquilino;
import com.consorcio.enums.SexoEnum;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author joaqu
 */
@ManagedBean
@ViewScoped
public class ControllerEditInquilino {

    /**
     * Creates a new instance of ControllerEditInquilino
     */
    private @EJB
    InquilinoServiceBean inquilinoService;

    private String casoDeUso;
    private Inquilino inquilino;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correoElectronico;
    private String documento;
    private String tipoDocumento;
    private SexoEnum sexo;
    private String fechaNacimiento;
    private boolean disableButton;

    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            inquilino = (Inquilino) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("INQUILINO");
            if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {
                nombre = inquilino.getNombre();
                apellido = inquilino.getApellido();
                telefono = inquilino.getTelefono();
                correoElectronico = inquilino.getCorreoElectronico();
                documento = inquilino.getDocumento();
                tipoDocumento = inquilino.getTipoDocumento();
                sexo = inquilino.getSexo();
                fechaNacimiento = inquilino.getFechaNacimiento();
            }
            if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("ALTA")) {
                setDisableButton(false);
            } else {
                setDisableButton(true);
            }

        } catch (Exception e) {
            throw e;
        }
    }

    public String aceptar() {
        try {
            if (casoDeUso.equals("ALTA")) {
                inquilinoService.crearInquilino(nombre, apellido, telefono, correoElectronico, documento, tipoDocumento, sexo, fechaNacimiento);
            } else {
                inquilinoService.modificarInquilino(inquilino.getId(), nombre, apellido, telefono, correoElectronico, documento, tipoDocumento, sexo, fechaNacimiento);
            }
            limpiarSession();
            return "listarInquilino";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            return null;
        }
    }

    public void limpiarSession() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.removeAttribute("ACCION");
        session.removeAttribute("INQUILINO");
    }

    public InquilinoServiceBean getInquilinoService() {
        return inquilinoService;
    }

    public void setInquilinoService(InquilinoServiceBean inquilinoService) {
        this.inquilinoService = inquilinoService;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public SexoEnum getSexo() {
        return sexo;
    }

    public void setSexo(SexoEnum sexo) {
        this.sexo = sexo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isDisableButton() {
        return disableButton;
    }

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }

}
