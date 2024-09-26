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
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author IS2
 */
@Stateless
@LocalBean
public class DAOMenu {

    @PersistenceContext
    private EntityManager em;

    public void guardarMenu(Menu menu) {
        em.persist(menu);
    }

    public void actualizarMenu(Menu menu) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(menu);
        em.flush();
    }

    public Menu buscarPorId(String id) throws NoResultException {
        return em.find(Menu.class, id);
    }

    public Menu buscarPorNombre(String nombre) {
        try {
            return (Menu) em.createQuery("SELECT m FROM Menu m WHERE m.nombre = :nombre")
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; 
        }
    }

    public Collection<Menu> listarMenues() {
        TypedQuery<Menu> query = em.createQuery("SELECT m from Menu m WHERE m.eliminado = FALSE", Menu.class);
        return query.getResultList();
    }

}
