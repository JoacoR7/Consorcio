package com.consorcio.persist;

import com.consorcio.entity.Provincia;
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
public class DAOProvincia {

    @PersistenceContext
    private EntityManager em;

    public void guardarProvincia(Provincia provincia) {
        em.persist(provincia);
    }

    public void actualizarProvincia(Provincia provincia) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(provincia);
        em.flush();
    }

    public Provincia buscarProvinciaId(String id) throws NoResultException {
        return em.find(Provincia.class, id);
    }

    public Provincia buscarProvinciaPorNombre(String nombre) throws NoResultDAOException, Exception {
        try {
            TypedQuery<Provincia> query = em.createQuery("SELECT p FROM Provincia p WHERE p.nombre = :nombre AND p.eliminado = FALSE", Provincia.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró ningúna provincia con ese nombre.");
        } catch (Exception e){
            throw new Exception("Error de sistemas");
        }
    }

    public Collection<Provincia> listarProvinciaActivo() {
        TypedQuery<Provincia> query = em.createQuery("SELECT p FROM Provincia p WHERE p.eliminado = FALSE", Provincia.class);
        return query.getResultList();
    }

    public Provincia buscarProvinciaPorPaisYNombre(String idPais, String nombre) throws NoResultDAOException, Exception {

        try {

            return (Provincia) em.createQuery("SELECT p "
                    + "  FROM Provincia p"
                    + " WHERE p.pais.id = :idPais"
                    + "   AND p.nombre = :nombre"
                    + "   AND p.eliminado = FALSE").
                    setParameter("idPais", idPais).
                    setParameter("nombre", nombre).
                    getSingleResult();

        } catch (NoResultException e) {
            throw new NoResultDAOException("No se encontró ningúna provincia.");
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("Error de sistemas");
        }
    }

    public Collection<Provincia> listarProvinciasPorPais(String idPais) {
        try {
            TypedQuery<Provincia> query;
            query = em.createQuery("SELECT p FROM Provincia p"
                    + " WHERE p.eliminado = FALSE AND p.pais.id = :idPais", Provincia.class);
            query.setParameter("idPais", idPais);
            return query.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
