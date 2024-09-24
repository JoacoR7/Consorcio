/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Consorcio;
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
public class DAOConsorcio {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    public void guardarConsorcio(Consorcio consorcio) throws Exception {
        try {
            em.persist(consorcio);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error de sistema");
        }
    }

    public void actualizarConsorcio(Consorcio consorcio) throws Exception {
        try {
            em.setFlushMode(FlushModeType.COMMIT);
            em.merge(consorcio);
            em.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error de sistema");
        }

    }

    public Consorcio buscarConsorcioId(String id) throws NoResultException, Exception {
        try {
            return em.find(Consorcio.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error de sistema");
        }

    }
}
