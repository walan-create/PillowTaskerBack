package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.*;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.exception.IncidentNotFoundException;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Incident> all(){return this.incidentRepository.findAll();}

    @Transactional
    public Incident save (Incident incident){
        incidentRepository.save(incident);
        entityManager.refresh(incident);
        return incident;
    }

    public Incident one (Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(()->new IncidentNotFoundException(id));
    }

    public Incident replace(Long id, Incident incidentDetails) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentNotFoundException(id));
        incident.setTitle(incidentDetails.getTitle());
        incident.setConcept(incidentDetails.getConcept());
        incident.setDate(incidentDetails.getDate());

        return incidentRepository.save(incident);  // Si no se encuentra el incident con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.incidentRepository.findById(id).map(h->{
                    this.incidentRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new IncidentNotFoundException(id));
    }

    public List<Incident> getIncidentsByHotel(Long hotelId){
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        return this.incidentRepository.findAllByHotel_id(hotelId);
    }

    public Incident createIncidentForHotel(Long hotelId, Incident incident) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        incident.setHotel(hotel);

        hotelService.save(hotel);

        return incidentRepository.save(incident);
    }

}
