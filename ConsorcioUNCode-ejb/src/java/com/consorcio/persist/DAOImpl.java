/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author adrzanbar
 */
@Stateless
@LocalBean
public class DAOImpl<T> implements DAO<T> {

    protected @PersistenceContext
    EntityManager em;
    public Class type;

    public DAOImpl() {
    }

    public DAOImpl(T entidad) {
        type = entidad.getClass();
    }

    @Override
    public T guardar(T entidad) {
        em.persist(entidad);
        return entidad;
    }

    @Override
    public void eliminar(T entidad) {
        em.remove(em.merge(entidad));
    }

    @Override
    public T buscarPorId(Object id) {
        return (T) em.find(type, id);
    }

    @Override
    public T actualizar(T t) {
        return em.merge(t);
    }

    @Override
    public Collection<T> listar() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(type);
        Root<T> root = criteriaQuery.from(type);

        criteriaQuery.select(root).where(cb.equal(root.get("eliminado"), false));

        TypedQuery<T> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Collection<T> listar(Collection<Map<String, Object>> searchCriteria) throws DAOException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(type);
        Root<T> root = criteriaQuery.from(type);

        List<Predicate> andPredicates = new ArrayList<>();

        for (Map<String, Object> criteria : searchCriteria) {
            List<Predicate> orPredicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                String attribute = entry.getKey();
                Object value = entry.getValue();

                Predicate predicate = createPredicate(cb, root, attribute, value);
                orPredicates.add(predicate);
            }

            andPredicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
        }

        criteriaQuery.select(root).where(cb.and(andPredicates.toArray(new Predicate[0])));

        return em.createQuery(criteriaQuery).getResultList();
    }

    private Predicate createPredicate(CriteriaBuilder cb, Root<T> root, String attribute, Object value) throws DAOException {
        String[] parts = attribute.split("\\.");
        Path<?> path = root;

        for (String part : parts) {
            path = path.get(part);
        }

        if (value instanceof String) {
            return cb.equal(cb.lower(path.as(String.class)), value.toString().toLowerCase());
        } else if (value instanceof Integer) {
            return cb.equal(path.as(Integer.class), value);
        } else if (value instanceof Boolean) {
            return cb.equal(path.as(Boolean.class), value);
        } else if (value instanceof Double) {
            return cb.equal(path.as(Double.class), value);
        } else {
            return cb.equal(path, value);
        }
    }

    private Class<?> getAttributeType(String attribute) throws DAOException {
        try {
            return type.getDeclaredField(attribute).getType();
        } catch (NoSuchFieldException e) {
            throw new DAOException("Atributo '" + attribute + "' no encontrado en la entidad " + type.getSimpleName());
        }
    }

    private boolean isValidAttribute(String attribute) {
        try {
            type.getDeclaredField(attribute);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    @Override
    public T buscarUnico(Collection<Map<String, Object>> searchCriteria) throws DAOException {
        Collection<T> result = listar(searchCriteria);

        if (result.isEmpty()) {
            return null;
        } else if (result.size() > 1) {
            throw new DAOException("Se encontraron múltiples entidades para los criterios proporcionados.");
        }

        return result.iterator().next();
    }

}
