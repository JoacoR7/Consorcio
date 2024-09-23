/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Direccion;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victo
 */
@Stateless
@LocalBean
public class DAODireccion {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;
    
    public void guardarDireccion(Direccion direccion) {
        em.persist(direccion);
    }

    public void actualizarDireccion(Direccion direccion) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(direccion);
        em.flush();
    }

    public Direccion buscarDireccionId(String id) throws NoResultException {
        return em.find(Direccion.class, id);
    }


}
