/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.MenuServiceBean;
import com.consorcio.entity.Menu;
import com.consorcio.entity.SubMenu;
import java.util.Collection;
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
@ManagedBean(name = "ControllerEditMenu")
@ViewScoped
public class ControllerEditMenu {

    private @EJB
    MenuServiceBean menuService;
    private Menu menu;
    private String nombre;
    private int orden;
    private String casoDeUso;
    private boolean disableButton;
    private Collection<SubMenu> subMenus;

    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            menu = (Menu) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("MENU");
            if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {
                nombre = menu.getNombre();
                orden = menu.getOrden();
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

    public String aceptar() throws Exception {
        try {
            if (casoDeUso.equals("ALTA")) {
                menuService.crearMenu(nombre, orden);
            } else if (casoDeUso.equals("MODIFICAR")) {
                menuService.modificarMenu(menu.getId(), nombre, orden, null);
            }
            limpiarSession();
            return "listarMenu";
        } catch (Exception e) {
            return null;
        }
    }

    public void limpiarSession() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.removeAttribute("ACCION");
        session.removeAttribute("INQUILINO");
    }

    public String cancelar() {
        return "listMenu";
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
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

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public Collection<SubMenu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(Collection<SubMenu> subMenus) {
        this.subMenus = subMenus;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
}
