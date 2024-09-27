/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Expensa;
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
 * @author victo
 */
@Stateless
@LocalBean
public class DAOExpensa {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    public void guardarExpensa(Expensa expensa) {
        em.persist(expensa);
    }

    public void actualizarExpensa(Expensa expensa) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(expensa);
        em.flush();
    }

    public Expensa buscarExpensaId(String id) throws NoResultException {
        return em.find(Expensa.class, id);
    }

    public Collection<Expensa> listarExpensas() {
        TypedQuery<Expensa> query = em.createQuery("SELECT e FROM Expensa e WHERE e.eliminado = FALSE", Expensa.class);
        return query.getResultList();
    }

    public Expensa buscarExpensaActual() {
        try {
            TypedQuery<Expensa> query = em.createQuery("SELECT e FROM Expensa e "
                    + " WHERE e.eliminado = FALSE AND e.fechaHasta IS NULL", Expensa.class);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Expensa buscarPenultimaExpensa() {
        try {
            TypedQuery<Expensa> query = em.createQuery(
                    "SELECT e FROM Expensa e WHERE e.eliminado = FALSE AND e.fechaHasta IS NOT NULL ORDER BY e.fechaHasta DESC",
                    Expensa.class);

            query.setMaxResults(1);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
