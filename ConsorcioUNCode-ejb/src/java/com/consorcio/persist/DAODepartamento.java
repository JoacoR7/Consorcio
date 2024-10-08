/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Departamento;
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
public class DAODepartamento {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    public void guardarDepartamento(Departamento departamento) throws Exception {
        try {
            em.persist(departamento);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error de sistema");
        }
    }

    public void actualizarDepartamento(Departamento departamento) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(departamento);
        em.flush();
    }

    public Departamento buscarDepartamentoId(String id) throws NoResultException {
        return em.find(Departamento.class, id);
    }

    public Departamento buscarDepartamentoPorNombre(String nombre) throws NoResultDAOException, Exception {
        try {
            TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d "
                    + "WHERE d.nombre = :nombre AND d.eliminado = FALSE", Departamento.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult();

        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró ningún .");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error de sistemas");
        }
    }

    public Collection<Departamento> listarDepartamentoActivo() {
        try {
            TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d "
                    + "WHERE d.eliminado = FALSE", Departamento.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Collection<Departamento> listarDepartamentoPorProvincia(String idProvincia) {
        try {
            TypedQuery<Departamento> query;
            query = em.createQuery("SELECT d FROM Departamento d"
                    + " WHERE d.eliminado = FALSE AND d.provincia.id = :idProvincia", Departamento.class);
            query.setParameter("idProvincia", idProvincia);
            return query.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Departamento buscarDepartamentoPorProvinciaYNombre(String nombreDpto, String idProvincia) throws NoResultDAOException, Exception {
        try {
            TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d "
                    + "WHERE d.provincia.id = :idProvincia AND d.nombre = :nombreDpto "
                    + "AND d.eliminado = FALSE", Departamento.class);
            query.setParameter("nombreDpto", nombreDpto);
            query.setParameter("idProvincia", idProvincia);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró ningúna provincia.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error de sistemas");
        }
    }

}
