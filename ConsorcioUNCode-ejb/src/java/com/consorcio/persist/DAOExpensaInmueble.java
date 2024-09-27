/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.ExpensaInmueble;
import com.consorcio.enums.EstadoExpensaInmueble;
import com.consorcio.enums.Mes;
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
public class DAOExpensaInmueble {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    public void guardarExpensaInmueble(ExpensaInmueble expensaInmueble) {
        em.persist(expensaInmueble);
    }

    public void actualizarExpensaInmueble(ExpensaInmueble expensaInmueble) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(expensaInmueble);
        em.flush();
    }

    public ExpensaInmueble buscarExpensaInmuebleId(String id) throws NoResultException, NoResultDAOException {
        try {
            return em.find(ExpensaInmueble.class, id);
        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró esa expensa con ese inmueble");
        } catch (Exception ex) {
            throw ex;
        }

    }

    public Collection<ExpensaInmueble> listarExpensaInmuebleActivo() throws NoResultDAOException {
        try {
            TypedQuery<ExpensaInmueble> query = em.createQuery("SELECT ei FROM ExpensaInmueble ei WHERE"
                    + " ei.eliminado = FALSE", ExpensaInmueble.class);
            return query.getResultList();
        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontraron expensas activas");
        } catch (Exception ex) {
            throw ex;
        }

    }

    public ExpensaInmueble buscarExpensaInmueblePorFecha(String idExpensa, String idInmueble,
            Mes mes, long anio) throws NoResultDAOException {

        try {
            TypedQuery<ExpensaInmueble> query = em.createQuery("SELECT ei FROM ExpensaInmueble ei WHERE"
                    + " ei.eliminado = FALSE AND ei.expensa.id = :idExpensa"
                    + " AND ei.inmueble.id = :idInmueble AND ei.mes = :mes AND"
                    + " ei.anio = :anio AND ei.estado <> :estado", ExpensaInmueble.class);

            query.setParameter("idExpensa", idExpensa);
            query.setParameter("idInmueble", idInmueble);
            query.setParameter("mes", mes);
            query.setParameter("anio", anio);
            query.setParameter("estado", EstadoExpensaInmueble.ANULADO);
            return query.getSingleResult();

        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró esa información");
        } catch (Exception ex) {
            throw ex;
        }
    }

    public ExpensaInmueble buscarExpensaInmuebleExistentePorFecha(String idExpensaInmueble, Mes mes, long anio) throws NoResultDAOException {

        try {
            TypedQuery<ExpensaInmueble> query = em.createQuery("SELECT ei FROM ExpensaInmueble ei WHERE"
                    + " ei.eliminado = FALSE AND ei.id = :idExpensaInmueble"
                    + " AND ei.mes = :mes AND ei.anio = :anio"
                    + " AND ei.estado <> :estado", ExpensaInmueble.class);

            query.setParameter("idExpensaInmueble", idExpensaInmueble);
            query.setParameter("mes", mes);
            query.setParameter("anio", anio);
            query.setParameter("estado", EstadoExpensaInmueble.ANULADO);
            return query.getSingleResult();

        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró esa información");
        }
    }

}
