/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import com.consorcio.entity.Menu;
import com.consorcio.entity.SubMenu;
import com.consorcio.persist.DAOMenu;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author joaqu
 */
@Stateless
@LocalBean
public class MenuServiceBean {

    private @EJB DAOMenu dao;
    
    public void crearMenu(String nombre, int orden) throws Exception {

        try {

            if (nombre == null || nombre.isEmpty()) {
                throw new IllegalArgumentException("Debe indicar el nombre del menú");
            }

            Menu menuAux = buscarMenuPorNombre(nombre);
            
            if(menuAux != null){
                throw new Exception("Ya existe un menú con ese nombre");
            }

            Menu menu = new Menu();
            menu.setId(UUID.randomUUID().toString());
            menu.setNombre(nombre);
            menu.setOrden(orden);
            menu.setEliminado(false);

            dao.guardarMenu(menu);
            

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error de sistema");
        }
    }
    
    public Menu buscarMenu(String id) throws Exception {

        try {

            if (id == null || id.trim().isEmpty()) {
                throw new Exception("Debe indicar el menú");
            }

            return dao.buscarPorId(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }
    
    public Menu buscarMenuPorNombre(String nombre) throws Exception{

        try {

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("Debe indicar el nombre");
            }
            Menu menu = dao.buscarPorNombre(nombre);

            if(!menu.isEliminado()){
                return menu;
            }
            
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
    
     public void modificarMenu(String idMenu, String nombre, int orden, Collection<SubMenu> subMenus) throws Exception {

        try {

            Menu menu = buscarMenu(idMenu);
            
            if (nombre != null || !nombre.isEmpty()) {
                menu.setNombre(nombre);
            }
            
            menu.setOrden(orden);
            
            /*if (subMenus.isEmpty()){
              throw new Exception("Debe indicar los sub menú");   
            }*/
            
            menu.setSubmenu(subMenus);
            
            dao.actualizarMenu(menu);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error de sistema");
        }
    }

    public void eliminarMenu(String idMenu) throws Exception {

        try {
            Menu menu = buscarMenu(idMenu);
            menu.setEliminado(true);
            dao.actualizarMenu(menu);
        } catch (Exception e) {
            throw e;
        }

    }
    
    public Collection<Menu> listarMenues() {
        try {
            return dao.listarMenues();
        } catch (Exception e) {
            throw e;
        }
    }
    
}
