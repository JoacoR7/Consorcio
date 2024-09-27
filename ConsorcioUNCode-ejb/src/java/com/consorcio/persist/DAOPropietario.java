/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Propietario;
import com.consorcio.persist.error.NoResultDAOException;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author joaqu
 */
@Stateless
@LocalBean
public class DAOPropietario {

    @PersistenceContext
    private EntityManager em;

    public void guardarPropietario(Propietario propietario) {
        em.persist(propietario);
    }

    public void actualizarPropietario(Propietario propietario) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(propietario);
        em.flush();
    }

    public Propietario buscarPorId(String id) throws NoResultException, NoResultDAOException {
        try {
            return em.find(Propietario.class, id);
        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró ningún propietario con ese id.");
        } catch (Exception e){
            throw e;
        }
    }

    public Propietario buscarPorNombre(String nombre) throws NoResultDAOException {
        try {
            TypedQuery<Propietario> query = em.createQuery("SELECT p FROM Propietario p WHERE p.nombre = :nombre AND p.eliminado = FALSE", Propietario.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró ningún propietario con ese nombre.");
        } catch (Exception e){
            throw e;
        }
    }

    public Collection<Propietario> listarPropietarios() {
        TypedQuery<Propietario> query = em.createQuery("SELECT p from Propietario p WHERE p.eliminado = FALSE", Propietario.class);
        return query.getResultList();
    }
}
