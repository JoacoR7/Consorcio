/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Consorcio;
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

    public Consorcio buscarConsorcioPorNombre(String nombre) {
        try {
            TypedQuery<Consorcio> query = em.createQuery("SELECT c FROM Consorcio c "
                    + " WHERE c.nombre = :nombre AND c.eliminado = FALSE", Consorcio.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Collection<Consorcio> listarConsorcioActivo() {
        TypedQuery<Consorcio> query = em.createQuery("SELECT c FROM Consorcio c WHERE c.eliminado = FALSE", Consorcio.class);
        return query.getResultList();
    }
}
