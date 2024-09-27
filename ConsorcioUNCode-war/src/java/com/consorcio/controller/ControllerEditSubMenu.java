/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.MenuServiceBean;
import com.consorcio.business.SubMenuServiceBean;
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
@ManagedBean(name = "ControllerEditSubMenu")
@ViewScoped
public class ControllerEditSubMenu {
    private @EJB
    SubMenuServiceBean subMenuService;
    private @EJB
    MenuServiceBean menuService;
    
    private SubMenu subMenu;
    private Menu selectedMenu;
    private String nombreMenu;
    private String nombre;
    private String idMenuSeleccionado;
    private String url;
    private int orden;
    private String casoDeUso;
    private boolean disableButton;
    private Collection<SubMenu> subMenus;
    private Collection<Menu> menus;
    
    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            subMenu = (SubMenu) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SUBMENU");
            menus = menuService.listarMenues();
            if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {
                nombre = subMenu.getNombre();
                orden = subMenu.getOrden();
                url = subMenu.getUrl();
                nombreMenu = subMenuService.buscarMenuPorSubMenu(subMenu.getId()).getNombre();
                
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
                try {
                    selectedMenu = menuService.buscarMenuPorNombre(nombreMenu);
                    if (selectedMenu == null){
                        throw new IllegalArgumentException("No existe ese menú");
                    }
                } catch (Exception e) {
                }
                subMenuService.crearSubMenu(selectedMenu.getId(), nombre, url, orden);
            } else if (casoDeUso.equals("MODIFICAR")) {
                selectedMenu = subMenuService.buscarMenuPorSubMenu(subMenu.getId());
                System.out.println("MODIFICO");
                System.out.println(selectedMenu.getId() + subMenu.getId() + nombre + url + orden);
                subMenuService.modificarSubMenu(selectedMenu.getId(), subMenu.getId(), nombre, url, orden);
            }
            limpiarSession();
            return "listarSubMenu";
        } catch (Exception e) {
            return null;
        }
    }
    
    public String cancelar() {
        return "listarSubMenu";
    }

    public void limpiarSession() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.removeAttribute("ACCION");
        session.removeAttribute("SUBMENU");
    }

    public SubMenu getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(SubMenu subMenu) {
        this.subMenu = subMenu;
    }

    public Menu getSelectedMenu() {
        return selectedMenu;
    }

    public void setSelectedMenu(Menu selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
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


    public Collection<SubMenu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(Collection<SubMenu> subMenus) {
        this.subMenus = subMenus;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public Collection<Menu> getMenus() {
        return menus;
    }

    public String getIdMenuSeleccionado() {
        return idMenuSeleccionado;
    }

    public void setIdMenuSeleccionado(String idMenuSeleccionado) {
        this.idMenuSeleccionado = idMenuSeleccionado;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMenus(Collection<Menu> menus) {
        this.menus = menus;
    }
    
    
    
}
