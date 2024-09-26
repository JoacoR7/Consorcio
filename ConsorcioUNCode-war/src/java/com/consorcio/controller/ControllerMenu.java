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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author joaqu
 */
@ManagedBean(name = "ControllerMenu")
@ViewScoped
public class ControllerMenu {

    private @EJB
    MenuServiceBean menuService;
    private @EJB
    SubMenuServiceBean subMenuService;

    private List<Menu> menues = new ArrayList();
    private String nombre;
    private int orden;
    private Menu menuAEliminar;
    private Set<Integer> visibleSubMenus = new HashSet<>();

    @PostConstruct
    public void init() {
        try {
            listarMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarMenu() throws Exception {
        try {
            menues.clear();
            menues.addAll(menuService.listarMenues());
        } catch (Exception e) {
            throw e;
        }
    }

    public String alta() {

        try {

            guardarMenuSession("ALTA", null);
            return "modificarMenu";

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String consultar(Menu menu) {

        try {

            guardarMenuSession("CONSULTAR", menu);
            return "modificarMenu";

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String modificar(Menu menu) {

        try {

            guardarMenuSession("MODIFICAR", menu);
            return "modificarMenu";

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void baja(Menu menu) throws Exception {

        try {

            menuService.eliminarMenu(menu.getId());
            listarMenu();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void prepararBaja(Menu menu) {
        this.menuAEliminar = menu;
    }

    public void baja() {
        if (menuAEliminar != null) {
            try {
                menuService.eliminarMenu(menuAEliminar.getId());
                listarMenu();
                menuAEliminar = null;
            } catch (Exception e) {
                throw new IllegalArgumentException("El inquilino no fue encontrado.");
            }
        }
    }

    public Collection<SubMenu> listarSubMenus(String idMenu) {
        try {
            return subMenuService.buscarSubMenuPorMenu(idMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void redirectToUrl(String url) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            // Redirigir usando una URL absoluta o bien formada
            externalContext.redirect(externalContext.getRequestContextPath() + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarMenuSession(String casoDeUso, Menu menu) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("MENU", menu);
    }

    public void toggleSubMenu(int menuId) {
        if (visibleSubMenus.contains(menuId)) {
            visibleSubMenus.remove(menuId);
        } else {
            visibleSubMenus.add(menuId);
        }
    }

    public boolean isSubMenuVisible(int menuId) {
        return visibleSubMenus.contains(menuId);
    }

    public MenuServiceBean getMenuService() {
        return menuService;
    }

    public int getOrden() {
        return orden;
    }

    public List<Menu> getMenues() {
        return menues;
    }

    public String getNombre() {
        return nombre;
    }

    public Menu getMenuAEliminar() {
        return menuAEliminar;
    }

    public void setMenuAEliminar(Menu menuAEliminar) {
        this.menuAEliminar = menuAEliminar;
    }

}
