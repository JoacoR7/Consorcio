package com.consorcio.business;

import com.consorcio.entity.EstadoInmueble;
import com.consorcio.entity.Inmueble;
import com.consorcio.entity.Inquilino;
import com.consorcio.entity.Propietario;
import com.consorcio.persist.DAOException;
import com.consorcio.persist.DAOImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author adrzanbar
 */
@Stateless
@LocalBean
public class InmuebleServiceBean {

    private @EJB
    InquilinoServiceBean inquilinoService;
    private @EJB
    PropietarioServiceBean propietarioService;
    private @EJB
    DAOImpl<Inmueble> dao;

    private void validar(String piso, String puerta) throws ErrorServiceException {
        if (piso == null || piso.trim().isEmpty()) {
            throw new ErrorServiceException("Debe indicar el piso");
        }
        if (puerta == null || puerta.trim().isEmpty()) {
            throw new ErrorServiceException("Debe indicar la puerta");
        }
    }

    public void crearInmueble(String idPropietario, String idInquilino, String piso, String puerta) throws ErrorServiceException, DAOException {
        try {
            validar(piso, puerta);
            Propietario propietario = propietarioService.buscarPropietario(idPropietario);

            if (propietario == null) {
                throw new ErrorServiceException("El propietario indicado no existe");
            }

            Inquilino inquilino = inquilinoService.buscarInquilino(idInquilino);

            Map<String, Object> searchCriteria = new HashMap<>();
            searchCriteria.put("eliminado", Boolean.FALSE);
            searchCriteria.put("piso", piso);
            searchCriteria.put("puerta", puerta);

            Collection<Map<String, Object>> searchCriteriaCollection = new ArrayList<>();
            searchCriteriaCollection.add(searchCriteria);

            if (!dao.listar(searchCriteriaCollection).isEmpty()) {
                throw new ErrorServiceException("Existe un inmueble con el piso y puerta indicado");
            }

            Inmueble inmueble = new Inmueble();
            inmueble.setId(UUID.randomUUID().toString());
            inmueble.setEliminado(false);
            inmueble.setPropietario(propietario);
            inmueble.setInquilino(inquilino);
            inmueble.setEstado(inquilino == null
                    ? (propietario.isHabitaConsorcio() ? EstadoInmueble.HABITADO : EstadoInmueble.DESOCUPADO)
                    : EstadoInmueble.HABITADO);
            inmueble.setPiso(piso);
            inmueble.setPuerta(puerta);

            dao.guardar(inmueble);

        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Inmueble buscarInmueble(String idInmueble) throws ErrorServiceException {
        try {
            if (idInmueble == null || idInmueble.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el inmueble");
            }

            Map<String, Object> searchCriteria = new HashMap<>();
            searchCriteria.put("id", idInmueble);
            searchCriteria.put("eliminado", Boolean.FALSE);

            Collection<Map<String, Object>> searchCriteriaCollection = new ArrayList<>();
            searchCriteriaCollection.add(searchCriteria);

            Inmueble inmueble = dao.buscarUnico(searchCriteriaCollection);
            if (inmueble == null) {
                throw new ErrorServiceException("No se encuentra el inmueble indicado");
            }
            return inmueble;
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public void modificarInmueble(String idInmueble, String idPropietario, String idInquilino, String piso, String puerta) throws ErrorServiceException, DAOException {
        Inmueble inmueble = buscarInmueble(idInmueble);
        Propietario propietario = propietarioService.buscarPropietario(idPropietario);
        Inquilino inquilino = inquilinoService.buscarInquilino(idInquilino);

        validar(piso, puerta);

        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("piso", piso);
        searchCriteria.put("puerta", puerta);
        searchCriteria.put("eliminado", Boolean.FALSE);

        Collection<Map<String, Object>> searchCriteriaCollection = new ArrayList<>();
        searchCriteriaCollection.add(searchCriteria);

        try {
            Inmueble inmuebleAux = dao.buscarUnico(searchCriteriaCollection);
            if (inmuebleAux != null && !inmuebleAux.getId().equals(idInmueble)) {
                throw new ErrorServiceException("Existe un inmueble con el piso y puerta indicado");
            }
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new ErrorServiceException("Error de sistemas");
        }

        inmueble.setPropietario(propietario);
        inmueble.setInquilino(inquilino);
        inmueble.setEstado((inquilino == null ? (propietario.isHabitaConsorcio() ? EstadoInmueble.HABITADO : EstadoInmueble.DESOCUPADO) : EstadoInmueble.HABITADO));
        inmueble.setPiso(piso);
        inmueble.setPuerta(puerta);

        try {
            dao.actualizar(inmueble);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    public void eliminarInmueble(String idInmueble) throws ErrorServiceException {
        Inmueble inmueble = buscarInmueble(idInmueble);
        inmueble.setEliminado(true);
        try {
            dao.actualizar(inmueble);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Collection<Inmueble> listar() throws ErrorServiceException {
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("eliminado", Boolean.FALSE);
        Collection<Map<String, Object>> searchCriteriaCollection = new ArrayList<>();
        searchCriteriaCollection.add(searchCriteria);

        try {
            return dao.listar(searchCriteriaCollection);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Inmueble buscarInmueblePorInquilino(String idInquilino) throws ErrorServiceException {
        if (idInquilino == null || idInquilino.isEmpty()) {
            throw new ErrorServiceException("Debe indicar el inquilino");
        }
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("eliminado", Boolean.FALSE);
        searchCriteria.put("inquilino", inquilinoService.buscarInquilino(idInquilino));

        Collection<Map<String, Object>> searchCriteriaCollection = new ArrayList<>();
        searchCriteriaCollection.add(searchCriteria);

        try {
            return dao.buscarUnico(searchCriteriaCollection);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Inmueble buscarInmueblePorPropietario(String idPropietario) throws ErrorServiceException {
        if (idPropietario == null || idPropietario.isEmpty()) {
            throw new ErrorServiceException("Debe indicar el propietario");
        }
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("eliminado", Boolean.FALSE);
        searchCriteria.put("propietario", propietarioService.buscarPropietario(idPropietario));

        Collection<Map<String, Object>> searchCriteriaCollection = new ArrayList<>();
        searchCriteriaCollection.add(searchCriteria);

        try {
            return dao.buscarUnico(searchCriteriaCollection);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Collection<Inmueble> listarInmueblePorPropietario(String idPropietario) throws ErrorServiceException {
        if (idPropietario == null || idPropietario.isEmpty()) {
            throw new ErrorServiceException("Debe indicar el propietario");
        }
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("eliminado", Boolean.FALSE);
        searchCriteria.put("propietario", propietarioService.buscarPropietario(idPropietario));

        Collection<Map<String, Object>> searchCriteriaCollection = new ArrayList<>();
        searchCriteriaCollection.add(searchCriteria);

        try {
            return dao.listar(searchCriteriaCollection);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Collection<Inmueble> listarInmueblePorApellidoDni(String filtro) throws ErrorServiceException {
        try {
            if (filtro == null || filtro.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el criterio de búsqueda");
            }

            // Create search criteria
            Map<String, Object> propietarioCriteria = new HashMap<>();
            propietarioCriteria.put("eliminado", Boolean.FALSE);
            propietarioCriteria.put("propietario.apellido", filtro);

            Map<String, Object> inquilinoCriteria = new HashMap<>();
            inquilinoCriteria.put("eliminado", Boolean.FALSE);
            inquilinoCriteria.put("inquilino.apellido", filtro);
            inquilinoCriteria.put("inquilino.documento", filtro);

            Collection<Map<String, Object>> searchCriteria = new ArrayList<>();
            searchCriteria.add(propietarioCriteria);
            searchCriteria.add(inquilinoCriteria);

            return dao.listar(searchCriteria);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            throw new ErrorServiceException("Error de sistema");
        }
    }

}
