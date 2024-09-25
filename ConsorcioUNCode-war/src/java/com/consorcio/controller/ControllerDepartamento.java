/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.DepartamentoServiceBean;
import com.consorcio.entity.Departamento;
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
 * @author victo
 */
@ManagedBean
@ViewScoped
public class ControllerDepartamento implements Serializable{

    /**
     * Creates a new instance of ControllerDepartamento
     */
    private @EJB DepartamentoServiceBean departamentoService;
    private List<Departamento> departamentos = new ArrayList<>();
    private Departamento departamentoEliminar;

    @PostConstruct
    public void init() {
        listarDepartamentos();
    }

    public Collection<Departamento> getDepartamentos() {
        return departamentos;
    }

    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarDepartamento";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void prepararBaja(Departamento departamento) {
        this.departamentoEliminar = departamento;
    }

    public void baja() {
        
        if (departamentoEliminar != null) {
            try {
                departamentoService.eliminarDepartamento(departamentoEliminar.getId());
                listarDepartamentos();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public String modificar(Departamento departamento) {
        try {
            guardarSession("MODIFICAR", departamento);
            return "modificarDepartamento";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String consultar(Departamento departamento) {
        try {
            guardarSession("CONSULTAR", departamento);
            return "modificarDepartamento";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarSession(String casoDeUso, Departamento departamento) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("DEPARTAMENTO", departamento);
    }

    public void listarDepartamentos() {
        try {
            departamentos.clear();
            departamentos.addAll(departamentoService.listarDepartamentos());

            // Ordenar alfabéticamente por el nombre de la provincia usando un Comparator
            Collections.sort(departamentos, new Comparator<Departamento>() {
                @Override
                public int compare(Departamento d1, Departamento d2) {
                    return d1.getNombre().compareToIgnoreCase(d2.getNombre());
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }
    
}
