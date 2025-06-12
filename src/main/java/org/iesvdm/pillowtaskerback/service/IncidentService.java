package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Incident;
import org.iesvdm.pillowtaskerback.exception.ApiException;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    @Autowired
    IncidentRepository incidentRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    CredentialRepository credentialService;

    @Autowired
    CredentialRepository credentialRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private HotelService hotelService;

    /**
     * Devuelve la lista completa de incidencias.
     *
     * @return lista de todas las incidencias
     */
    public List<Incident> all() {
        return this.incidentRepository.findAll();
    }

    /**
     * Guarda una nueva incidencia en la base de datos y actualiza su estado.
     *
     * @param incident incidencia a guardar
     * @return incidencia guardada
     */
    @Transactional
    public Incident save(Incident incident) {
        incidentRepository.save(incident);
        entityManager.refresh(incident);
        return incident;
    }

    /**
     * Busca y devuelve una incidencia por su id.
     *
     * @param id identificador de la incidencia
     * @return incidencia encontrada
     * @throws ApiException si no se encuentra la incidencia
     */
    public Incident one(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Incidencia con id " + id + " no encontrada", HttpStatus.NOT_FOUND));
    }

    /**
     * Reemplaza los datos de una incidencia existente por los nuevos datos proporcionados.
     *
     * @param id identificador de la incidencia a modificar
     * @param incidentDetails datos nuevos de la incidencia
     * @return incidencia actualizada
     * @throws ApiException si no se encuentra la incidencia
     */
    public Incident replace(Long id, Incident incidentDetails) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Incidencia con id " + id + " no encontrada", HttpStatus.NOT_FOUND));
        incident.setTitle(incidentDetails.getTitle());
        incident.setConcept(incidentDetails.getConcept());
        incident.setDate(incidentDetails.getDate());

        return incidentRepository.save(incident);
    }

    /**
     * Elimina una incidencia por su id.
     *
     * @param id identificador de la incidencia a eliminar
     * @throws ApiException si no se encuentra la incidencia
     */
    public void delete(Long id) {
        this.incidentRepository.findById(id).map(h -> {
                    this.incidentRepository.delete(h);
                    return h;
                })
                .orElseThrow(() -> new ApiException("Incidencia con id " + id + " no encontrada", HttpStatus.NOT_FOUND));
    }

    /**
     * Obtiene la lista de incidencias asociadas a un hotel específico.
     *
     * @param hotelId identificador del hotel
     * @return lista de incidencias del hotel
     * @throws ApiException si no se encuentra el hotel
     */
    public List<Incident> getIncidentsByHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel con id " + hotelId + " no encontrado", HttpStatus.NOT_FOUND));

        return this.incidentRepository.findAllByHotel_id(hotelId);
    }

    /**
     * Crea una nueva incidencia y la asocia a un hotel.
     *
     * @param hotelId identificador del hotel
     * @param incident incidencia a crear
     * @return incidencia creada y asociada al hotel
     * @throws ApiException si no se encuentra el hotel
     */
    public Incident createIncidentForHotel(Long hotelId, Incident incident) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel con id " + hotelId + " no encontrado", HttpStatus.NOT_FOUND));

        incident.setHotel(hotel);

        hotelService.save(hotel);

        return incidentRepository.save(incident);
    }

}