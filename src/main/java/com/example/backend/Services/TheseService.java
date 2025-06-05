package com.example.backend.Services;

import com.example.backend.Models.Doctorants;
import com.example.backend.Models.These;
import com.example.backend.Models.Chercheur;
import com.example.backend.Dto.TheseDTO;
import com.example.backend.Repositories.TheseRepository;
import com.example.backend.Repositories.DoctorantRepository;
import com.example.backend.Repositories.ChercheurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TheseService {
    @Autowired
    private TheseRepository theseRepository;

    @Autowired
    private DoctorantRepository doctorantRepository;

    @Autowired
    private ChercheurRepository chercheurRepository;


    public List<These> getAllTheses() {
        return theseRepository.findAll();
    }

    public Optional<These> getThesisById(Long id) {
        return theseRepository.findById(id);
    }

    public These getThesisByDoctorantId(Long doctorantId) {
        return theseRepository.findByDoctorantId(doctorantId);
    }

public These updateThesis(Long id, TheseDTO updateDTO) {
    Optional<These> optionalThesis = theseRepository.findById(id);

    if (optionalThesis.isEmpty()) {
        throw new RuntimeException("Thesis not found with id: " + id);
    }

    These thesis = optionalThesis.get();

    // Update basic fields
    if (updateDTO.getTitre() != null) {
        thesis.setTitre(updateDTO.getTitre());
    }
    if (updateDTO.getResume() != null) {
        thesis.setResume(updateDTO.getResume());
    }
    if (updateDTO.getDateSoumission() != null) {
        thesis.setDateSoumission(updateDTO.getDateSoumission());
    }
    if (updateDTO.getDateInscription() != null) {
        thesis.setDateInscription(updateDTO.getDateInscription());
    }
    if (updateDTO.getDateFinVisee() != null) {
        thesis.setDateFinVisee(updateDTO.getDateFinVisee());
    }
    if (updateDTO.getDateSoutenance() != null) {
        thesis.setDateSoutenance(updateDTO.getDateSoutenance());
    }
    if (updateDTO.getLangue() != null) {
        thesis.setLangue(updateDTO.getLangue());
    }
    if (updateDTO.getUrlPdf() != null) {
        thesis.setUrlPdf(updateDTO.getUrlPdf());
    }
    if (updateDTO.getMotCles() != null) {
        thesis.setMotCles(updateDTO.getMotCles());
    }
    if (updateDTO.getFichierThese() != null) {
        thesis.setFichierThese(updateDTO.getFichierThese());
    }
    if (updateDTO.getEtatThese() != null) {
        thesis.setEtatThese(updateDTO.getEtatThese());
    }
    if (updateDTO.getStatut() != null) {
        thesis.setStatut(updateDTO.getStatut());
    }

    // Update relations safely using IDs
    if (updateDTO.getDoctorantId() != null) {
        Optional<Doctorants> doctorant = doctorantRepository.findById(updateDTO.getDoctorantId());
        if (doctorant.isPresent()) {
            thesis.setDoctorant(doctorant.get());
        } else {
            throw new RuntimeException("Doctorant not found with id: " + updateDTO.getDoctorantId());
        }
    }

    if (updateDTO.getChercheurId() != null) {
        Optional<Chercheur> chercheur = chercheurRepository.findById(updateDTO.getChercheurId());
        if (chercheur.isPresent()) {
            thesis.setChercheur(chercheur.get());
        } else {
            throw new RuntimeException("Chercheur not found with id: " + updateDTO.getChercheurId());
        }
    }

    return theseRepository.save(thesis);
}

// Keep the old method for backward compatibility but mark as deprecated
@Deprecated
public These updateThesis(Long id, These updatedThesis) {
    Optional<These> optionalThesis = theseRepository.findById(id);

    if (optionalThesis.isEmpty()) {
        throw new RuntimeException("Thesis not found with id: " + id);
    }

    These thesis = optionalThesis.get();

    // Update only non-null fields to avoid overwriting with null values
    if (updatedThesis.getTitre() != null) {
        thesis.setTitre(updatedThesis.getTitre());
    }
    if (updatedThesis.getResume() != null) {
        thesis.setResume(updatedThesis.getResume());
    }
    if (updatedThesis.getDateSoumission() != null) {
        thesis.setDateSoumission(updatedThesis.getDateSoumission());
    }
    if (updatedThesis.getDateInscription() != null) {
        thesis.setDateInscription(updatedThesis.getDateInscription());
    }
    if (updatedThesis.getDateFinVisee() != null) {
        thesis.setDateFinVisee(updatedThesis.getDateFinVisee());
    }
    if (updatedThesis.getDateSoutenance() != null) {
        thesis.setDateSoutenance(updatedThesis.getDateSoutenance());
    }
    if (updatedThesis.getLangue() != null) {
        thesis.setLangue(updatedThesis.getLangue());
    }
    if (updatedThesis.getUrlPdf() != null) {
        thesis.setUrlPdf(updatedThesis.getUrlPdf());
    }
    if (updatedThesis.getMotCles() != null) {
        thesis.setMotCles(updatedThesis.getMotCles());
    }
    if (updatedThesis.getFichierThese() != null) {
        thesis.setFichierThese(updatedThesis.getFichierThese());
    }
    if (updatedThesis.getEtatThese() != null) {
        thesis.setEtatThese(updatedThesis.getEtatThese());
    }
    if (updatedThesis.getStatut() != null) {
        thesis.setStatut(updatedThesis.getStatut());
    }

    // Don't update relations from the old method to avoid circular reference issues

    return theseRepository.save(thesis);
}


    public List<These> getThesisByStatut(These.Statut statut) {
        return theseRepository.findByStatut(statut);
    }

    public List<Doctorants> getDoctorantsByChercheurId(Long chercheurId) {
        List<These> theses = theseRepository.findByChercheurId(chercheurId);
        return theses.stream()
                .map(These::getDoctorant)
                .collect(Collectors.toList());
    }
}