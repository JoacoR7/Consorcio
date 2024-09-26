/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Menu;
import com.consorcio.entity.SubMenu;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author IS2
 */
@Stateless
@LocalBean
public class DAOSubMenu {

    @PersistenceContext
    private EntityManager em;

    public void guardarSubMenu(SubMenu subMenu) {
        em.persist(subMenu);
    }

    public void actualizarSubMenu(SubMenu subMenu) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(subMenu);
        em.flush();
    }

    public SubMenu buscarSubMenu(String id) throws NoResultException {
        return em.find(SubMenu.class, id);
    }

    public SubMenu buscarSubMenuPorNombre(String nombre) {
        try {
            return (SubMenu) em.createQuery("SELECT s FROM SubMenu s WHERE s.nombre = :nombre")
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public SubMenu buscarSubMenuPorMenuYOrden(String idMenu, int orden) {

        try {

            return (SubMenu) em.createQuery("SELECT sub "
                    + "  FROM Menu m, IN (m.submenu) sub "
                    + " WHERE m.id = :idMenu "
                    + "   AND sub.orden = :orden "
                    + " ORDER BY sub.orden").
                    setParameter("idMenu", idMenu).
                    setParameter("orden", orden).
                    getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public SubMenu buscarSubMenuPorOrden(String idSubMenu, String nombre) {

        try {

            return (SubMenu) em.createQuery("SELECT m "
                    + "  FROM SubMenu m"
                    + " WHERE m.nombre = :nombre"
                    + "   AND m.id = :id").
                    setParameter("nombre", nombre).
                    setParameter("id", idSubMenu).
                    getSingleResult();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public Menu buscarMenuPorSubMenuId(String subMenuId) {
        try {
            return em.createQuery("SELECT m FROM Menu m JOIN m.submenu sub WHERE sub.id = :subMenuId", Menu.class)
                    .setParameter("subMenuId", subMenuId)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Maneja el caso en que no se encuentra ningún resultado
            return null; // o lanzar una excepción, según tu lógica
        } catch (NonUniqueResultException e) {
            // Maneja el caso en que hay más de un resultado
            throw new IllegalStateException("Se encontraron múltiples menús para el submenú dado.");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public Collection<SubMenu> listarSubMenu() {
        try {
            return em.createQuery("SELECT sub FROM Menu m JOIN m.submenu sub "
                    + "WHERE sub.eliminado = false "
                    + "ORDER BY sub.orden DESC", SubMenu.class)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public Collection<SubMenu> listarSubMenuActivo() {

        try {

            return em.createQuery("SELECT sub "
                    + "  FROM Menu m, IN (m.submenu) sub "
                    + " WHERE sub.eliminado = FALSE"
                    + "   AND m.eliminado = FALSE"
                    + " ORDER BY sub.orden DESC").
                    getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public Collection<SubMenu> listarSubMenuPorMenu(String idMenu) {
        try {
            return em.createQuery("SELECT sub "
                    + " FROM Menu m, IN (m.submenu) sub "
                    + " WHERE m.id = :idMenu "
                    + " AND sub.eliminado = false " // Excluir los eliminados
                    + " ORDER BY sub.orden DESC")
                    .setParameter("idMenu", idMenu)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}
