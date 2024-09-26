/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.SubMenuServiceBean;
import com.consorcio.entity.Menu;
import com.consorcio.entity.SubMenu;
import java.util.ArrayList;
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
@ManagedBean(name = "ControllerSubMenu")
@ViewScoped
public class ControllerSubMenu {

    private @EJB
    SubMenuServiceBean subMenuService;
    private String nombre;
    private String orden;
    private Menu menu;
    private List<SubMenu> submenues = new ArrayList<SubMenu>();
    private boolean eliminado;
    private SubMenu subMenuAEliminar;

    @PostConstruct
    public void init() {
        listarSubMenu();
    }

    public void listarSubMenu() {
        try {
            submenues.clear();
            submenues.addAll(subMenuService.listarSubMenu());

            Collections.sort(submenues, new Comparator<SubMenu>() {
                @Override
                public int compare(SubMenu o1, SubMenu o2) {
                    return Integer.compare(o1.getOrden(), o2.getOrden());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String alta() {
        try {
            guardarSession("ALTA", null);
            return "modificarSubMenu";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void prepararBaja(SubMenu subMenu) {
        this.subMenuAEliminar = subMenu;
    }

    public void baja() {
        if (subMenuAEliminar != null) {
            try {
                subMenuService.eliminarSubMenu(subMenuAEliminar.getId());
                listarSubMenu();
                subMenuAEliminar = null;
            } catch (Exception e) {
                throw new IllegalArgumentException("El inquilino no fue encontrado.");
            }
        }
    }

    public String modificar(SubMenu subMenu) {
        try {
            guardarSession("MODIFICAR", subMenu);
            return "modificarSubMenu";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String consultar(SubMenu subMenu) {
        try {
            guardarSession("CONSULTAR", subMenu);
            return "modificarSubMenu";
        } catch (Exception e) {
            return null;
        }
    }
    
    private void guardarSession(String casoDeUso, SubMenu subMenu) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("ACCION", casoDeUso.toUpperCase());
        session.setAttribute("SUBMENU", subMenu);
    }

    public SubMenuServiceBean getSubMenuService() {
        return subMenuService;
    }

    public String getNombre() {
        return nombre;
    }

    public String getOrden() {
        return orden;
    }

    public void setSubMenuAEliminar(SubMenu subMenuAEliminar) {
        this.subMenuAEliminar = subMenuAEliminar;
    }

    public Menu getMenu() {
        return menu;
    }

    public List<SubMenu> getSubmenues() {
        return submenues;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public SubMenu getSubMenuAEliminar() {
        return subMenuAEliminar;
    }

}
