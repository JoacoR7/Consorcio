/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Menu;
import com.consorcio.entity.SubMenu;
import com.consorcio.persist.DAOSubMenu;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.jboss.weld.bootstrap.api.Environments;

/**
 *
 * @author joaqu
 */
@Stateless
@LocalBean
public class SubMenuServiceBean {

    private @EJB MenuServiceBean menuService;
    private @EJB DAOSubMenu dao;

    public void validar(Menu menu, String nombre, String url, int orden)throws Exception {
        
        try{
            
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new Exception("Debe indicar el nombre del sub menú");
            }
           
            if (url == null || url.trim().isEmpty()) {
                throw new Exception("Debe indicar la url");
            }

            if (orden <= 0) {
                throw new Exception("Debe indicar el orden");
            }
            
        } catch (Exception ex){
            ex.printStackTrace();
            throw new Exception("Error de Sistemas");
        }
    }
    
    public void crearSubMenu(String idMenu, String nombre, String url, int orden) throws Exception {

        try {
            Menu menu = menuService.buscarMenu(idMenu);
            
            validar(menu, nombre, url, orden);

            try {
                dao.buscarSubMenuPorNombre(nombre);
                throw new Exception("Existe un sub menú con el nombre indicado");
            } catch (Exception ex) {}

            try {
                dao.buscarSubMenuPorMenuYOrden(menu.getId(), orden);
                throw new Exception("Existe un sub menú para el menú y orden indicado");
            } catch (Exception ex) {}
            
            //Se crea el submenu
            SubMenu subMenu = new SubMenu();
            subMenu.setId(UUID.randomUUID().toString());
            subMenu.setNombre(nombre);
            subMenu.setUrl(url);
            subMenu.setOrden(orden);

            //Se agrega el submenu a la lista existente
            Collection<SubMenu> subMenus = new ArrayList<SubMenu>();
            if (menu.getSubmenu() != null) {
                subMenus.addAll(menu.getSubmenu());
            }
            subMenus.add(subMenu);

            menuService.modificarMenu(menu.getId(), menu.getNombre(), menu.getOrden(), subMenus);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error de sistema");
        }
    }

    public void modificarSubMenu(String idMenu, String idSubMenu, String nombre, String url, int orden) throws Exception {

        try {
            System.out.println("PRE VALIDO");
            Menu menu = menuService.buscarMenu(idMenu);

            SubMenu subMenu = buscarSubMenu(idSubMenu);
            
            validar(menu, nombre, url, orden);
            System.out.println("VALIDADO");
            try {
                SubMenu subMenuAux = dao.buscarSubMenuPorOrden(subMenu.getId(), nombre);
                if (!subMenuAux.getId().equals(subMenu.getId())){
                   throw new Exception("Existe un sub menu para el menú y nombre indicado");
                }
            } catch (Exception ex) {}
            
            try {
                SubMenu subMenuAux = dao.buscarSubMenuPorMenuYOrden(menu.getId(), orden);
                if (!subMenuAux.getId().equals(subMenu.getId())){
                  throw new Exception("Existe un sub menu para el menú y nombre indicado");
                } 
            } catch (Exception nrx) {}

            subMenu.setNombre(nombre);
            subMenu.setUrl(url);
            subMenu.setOrden(orden);
            
            dao.actualizarSubMenu(subMenu);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error de sistema");
        }
    }

    public void eliminarSubMenu(String idSubMenu) throws Exception {

        try {

            SubMenu sub = buscarSubMenu(idSubMenu);
            sub.setEliminado(true);
            dao.actualizarSubMenu(sub);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error de sistema");
        }
    }

    public SubMenu buscarSubMenu(String id) throws Exception {

        try {

            if (id == null || id.trim().isEmpty()) {
                throw new Exception("Debe indicar un sub menú");
            }

            SubMenu sub= dao.buscarSubMenu(id);
            
            if (sub.isEliminado()){
               throw new Exception("No se encuentra el sub menú indicado"); 
            }
            
            return sub;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error de sistema");
        }
    }
    
    public Menu buscarMenuPorSubMenu(String idSubMenu){
        try {
            Menu menu = dao.buscarMenuPorSubMenuId(idSubMenu);
            return menu;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SubMenu buscarSubMenuPorNombre(String nombre) throws Exception{

        try {

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new Exception("Debe indicar el nombre");
            }

            return dao.buscarSubMenuPorNombre(nombre);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public Collection<SubMenu> buscarSubMenuPorMenu(String idMenu){
        try {
            if(idMenu == null || idMenu.isEmpty()){
                throw new Exception("Debe indicar el menú");
            }
            return dao.listarSubMenuPorMenu(idMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Collection<SubMenu> listarSubMenu() throws Exception {

        try {
            return dao.listarSubMenu();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error de sistema");
        }
    }

}
