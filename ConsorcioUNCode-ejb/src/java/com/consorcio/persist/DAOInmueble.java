/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Inmueble;
import com.consorcio.persist.error.ErrorDAOException;
import com.consorcio.persist.error.NoResultDAOException;
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
public class DAOInmueble {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    public void guardarInmueble(Inmueble inmueble) {
        em.persist(inmueble);
    }

    public void actualizarInmueble(Inmueble inmueble) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(inmueble);
        em.flush();
    }

    public Inmueble buscarInmuebleId(String id) throws DAOException {
        return em.find(Inmueble.class, id);
    }

    public Collection<Inmueble> listarInmuebleActivo() {
        TypedQuery<Inmueble> query = em.createQuery("SELECT i FROM Inmueble i WHERE "
                + " i.eliminado = FALSE", Inmueble.class);
        return query.getResultList();
    }

    public Inmueble buscarInmueblePorPisoYPuerta(String piso, String puerta) throws NoResultException,NoResultDAOException, ErrorDAOException {

        try {

            return (Inmueble) em.createQuery("SELECT i "
                    + "  FROM Inmueble i"
                    + " WHERE i.piso = :piso"
                    + "   AND i.puerta = :puerta"
                    + "   AND i.eliminado = FALSE").
                    setParameter("piso", piso).
                    setParameter("puerta", puerta).
                    getSingleResult();

        } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
        }
    }
}
