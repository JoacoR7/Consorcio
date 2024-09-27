/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Pais;
import com.consorcio.persist.error.NoResultDAOException;
import java.util.Collection;
import java.util.List;
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
public class DAOPais {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    public void guardarPais(Pais pais) {
        em.persist(pais);
    }

    public void actualizarPais(Pais pais) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(pais);
        em.flush();
    }

    public Pais buscarPaisId(String id) throws NoResultException {
        return em.find(Pais.class, id);
    }

    public Pais buscarPaisPorNombre(String nombre) throws NoResultDAOException, Exception {

        try {
            TypedQuery<Pais> query = em.createQuery("SELECT p FROM Pais p WHERE p.nombre = :nombre AND p.eliminado = FALSE", Pais.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró ningún país con ese nombre.");
        } catch (Exception e){
            throw new Exception("Error de sistemas");
        }
    }

    public Collection<Pais> listarPaisActivo() {
        TypedQuery<Pais> query = em.createQuery("SELECT p FROM Pais p WHERE p.eliminado = FALSE", Pais.class
        );
        return query.getResultList();
    }
}
