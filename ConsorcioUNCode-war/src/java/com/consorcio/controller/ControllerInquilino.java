/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.InquilinoServiceBean;
import com.consorcio.entity.Inquilino;
import com.consorcio.enums.SexoEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
 * @author joaqu
 */
@ManagedBean(name = "ControllerInquilino")
@ViewScoped
public class ControllerInquilino implements Serializable {

    private @EJB
    InquilinoServiceBean inquilinoService;
    private String documento;
    private String tipoDocumento;
    private String nombre;
    private String apellido;
    private SexoEnum sexo;
    private String fechaNacimiento;
    private String telefono;
    private String correoElectronico;
    private List<Inquilino> inquilinos = new ArrayList<Inquilino>();
    private boolean eliminado;
    private Inquilino inquilinoAEliminar;

    @PostConstruct
    public void init() {
        listarInquilino();
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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

    public SexoEnum getSexo() {
        return sexo;
    }

    public void setSexo(SexoEnum sexo) {
        this.sexo = sexo;
    }

    public SexoEnum[] getSexos() {
        return SexoEnum.values();
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public Collection<Inquilino> getInquilinos() {
        return inquilinos;
    }

    public void listarInquilino() {
        try {
            inquilinos.clear();
            inquilinos.addAll(inquilinoService.listarInquilinos());

            Collections.sort(inquilinos, new Comparator<Inquilino>() {

                @Override
                public int compare(Inquilino o1, Inquilino o2) {
                    return o1.getDocumento().compareToIgnoreCase(o2.getDocumento());
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarInquilino";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void prepararBaja(Inquilino inquilino) {
        this.inquilinoAEliminar = inquilino;
    }

    public void baja() {
        if (inquilinoAEliminar != null) {
            try {
                inquilinoService.eliminarInquilino(inquilinoAEliminar.getId());
                listarInquilino();
                inquilinoAEliminar = null;
            } catch (Exception e) {
                throw new IllegalArgumentException("El inquilino no fue encontrado.");
            }
        }
    }

    public String modificar(Inquilino inquilino) {
        try {
            guardarSession("MODIFICAR", inquilino);
            return "modificarInquilino";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String consultar(Inquilino inquilino) {
        try {
            guardarSession("CONSULTAR", inquilino);
            return "modificarInquilino";
        } catch (Exception e) {
            return null;
        }
    }

    private void guardarSession(String casoDeUso, Inquilino inquilino) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("INQUILINO", inquilino);
    }

}