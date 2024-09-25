/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.DiaFeriado;
import com.consorcio.persist.error.ErrorDAOException;
import com.consorcio.persist.error.NoResultDAOException;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author IS2
 */
@Stateless
@LocalBean
public class DAODiaFeriadoImplBean {

   @PersistenceContext private EntityManager em;
   
   public void guardarDiaFeriado(DiaFeriado diaFeriado){
       em.persist(diaFeriado);
   }
   
   public void actualizarDiaFeriado(DiaFeriado diaFeriado){
       em.setFlushMode(FlushModeType.COMMIT);
       em.merge(diaFeriado);
       em.flush();
   }
   
   public DiaFeriado buscarDiaFeriado(String id) throws NoResultException{
       return em.find(DiaFeriado.class, id);
   }
   
   public DiaFeriado buscarDiaFeriadoPorFecha (Date fecha)throws NoResultDAOException, ErrorDAOException{
     
       try{
           
           return (DiaFeriado) em.createQuery("SELECT d "
                                             + "  FROM DiaFeriado d "
                                             + " WHERE d.eliminado = FALSE "
                                             + "   AND d.fecha = :fecha1").
                                             setParameter("fecha1", fecha).
                                             getSingleResult();
       } catch (NoResultException ex) {
            throw new NoResultDAOException("No se encontró información");
       } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
       } 
   }
   
   public Collection<DiaFeriado> listarFeriadoActivo(Date fechaDesde, Date fechaHasta) throws ErrorDAOException {
       
        try {
            
            return em.createQuery(
                    "SELECT d "
                    + "  FROM DiaFeriado d"
                    + " WHERE d.eliminado = FALSE"
                    + " AND d.fecha >= :fechaDesde"
                    + " AND d.fecha <= :fechaHasta"
                    + " ORDER BY d.fecha").
                    setParameter("fechaDesde", fechaDesde).
                    setParameter("fechaHasta", fechaHasta).
                    getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
        } 
    }
   
    public Collection<DiaFeriado> listarFeriadosDesdeFecha(Date fechaDesde) throws ErrorDAOException {
        
        try {
            
            return em.createQuery(
                    "SELECT d "
                    + " FROM DiaFeriado d"
                    + " WHERE d.eliminado = FALSE"
                    + " AND d.fecha >= :fechaDesde"
                    + " ORDER BY d.fecha").
                    setParameter("fechaDesde", fechaDesde).
                    getResultList();

        } catch (Exception ex) {
             ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
        }
    }
    
    public Collection<DiaFeriado> listarFeriadoActivo() throws ErrorDAOException {
        
        try {
            
            return em.createQuery(
                    "SELECT d "
                    + "  FROM DiaFeriado d"
                    + " WHERE d.eliminado = FALSE"
                    + " ORDER BY d.fecha").
                    getResultList();

        } catch (Exception ex) {
             ex.printStackTrace();
            throw new ErrorDAOException("Error de sistema");
        }
    }
}
