package com.example.backend.Controllers;

import com.example.backend.Dto.DoctorantProfileRequestDto;
import com.example.backend.Models.*;
import com.example.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProfileController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DoctorantRepository doctorantRepository;
    
    @Autowired
    private TheseRepository theseRepository;

    @Autowired
    private EtablissementRepository etablissementRepository;

    @Autowired  
    private UniteRechercheRepository uniteRechercheRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @PutMapping("/api/profile")
    public ResponseEntity<?> updateProfile(@RequestBody DoctorantProfileRequestDto request) {
        try {
            // 1. Update User
            Optional<Users> userOptional = userRepository.findById(request.getUser().getId());
            if(userOptional.isPresent()) {
                Users user = userOptional.get();
                user.setFirstName(request.getUser().getFirstName());
                user.setLastName(request.getUser().getLastName());
                user.setUsername(request.getUser().getUsername());
                user.setEmail(request.getUser().getEmail());
                
                // Handle phone number (convert String to int)
                if(request.getUser().getPhoneNumber() != null) {
                    user.setPhoneNumber(Integer.parseInt(request.getUser().getPhoneNumber()));
                }
                
                // Handle birth date (convert String to LocalDate)
                if(request.getUser().getBirthDate() != null) {
                    user.setBirthDate(LocalDate.parse(request.getUser().getBirthDate(), dateFormatter));
                }
                
                user.setNationality(request.getUser().getNationality());
                
                // Handle Etablissement
                if(request.getUser().getEtablissementId() != null) {
                    Optional<Etablissement> etablissementOptional = etablissementRepository.findById(request.getUser().getEtablissementId());
                    if(etablissementOptional.isPresent()) {
                        Etablissement etablissement = etablissementOptional.get();
                        etablissement.setNom(request.getUser().getEtablissementNom());
                        etablissementRepository.save(etablissement);
                        user.setEtablissement(etablissement);
                    }
                }
                
                userRepository.save(user);
            } else {
                return ResponseEntity.badRequest().body("User not found");
            }

            // 2. Update Doctorant
            Optional<Doctorants> doctorantOptional = doctorantRepository.findById(request.getDoctorant().getId());
            if(doctorantOptional.isPresent()) {
                Doctorants doctorant = doctorantOptional.get();
                doctorant.setNumeroInscription(request.getDoctorant().getNumeroInscription());
                
                // Handle UniteRecherche
                if(request.getDoctorant().getUniteRechercheId() != null) {
                    Optional<UniteRecherche> uniteRechercheOptional = uniteRechercheRepository.findById(request.getDoctorant().getUniteRechercheId());
                    if(uniteRechercheOptional.isPresent()) {
                        UniteRecherche uniteRecherche = uniteRechercheOptional.get();
                        uniteRecherche.setNom(request.getDoctorant().getUniteRechercheNom());
                        uniteRechercheRepository.save(uniteRecherche);
                        doctorant.setUniteRecherche(uniteRecherche);
                    }
                }
                
                doctorantRepository.save(doctorant);
            } else {
                return ResponseEntity.badRequest().body("Doctorant not found");
            }

            // 3. Update These
            Optional<These> theseOptional = theseRepository.findById(request.getThese().getId());
            if(theseOptional.isPresent()) {
                These these = theseOptional.get();
                these.setTitre(request.getThese().getTitre());
                these.setResume(request.getThese().getResume());
                
                // Handle dates (convert String to LocalDate)
                if(request.getThese().getDateSoumission() != null) {
                    these.setDateSoumission(LocalDate.parse(request.getThese().getDateSoumission(), dateFormatter));
                }
                if(request.getThese().getDateInscription() != null) {
                    these.setDateInscription(LocalDate.parse(request.getThese().getDateInscription(), dateFormatter));
                }
                if(request.getThese().getDateFinVisee() != null) {
                    these.setDateFinVisee(LocalDate.parse(request.getThese().getDateFinVisee(), dateFormatter));
                }
                if(request.getThese().getDateSoutenance() != null) {
                    these.setDateSoutenance(LocalDate.parse(request.getThese().getDateSoutenance(), dateFormatter));
                }
                
                
                if(request.getThese().getEtatThese() != null) {
                    these.setEtatThese(request.getThese().getEtatThese());
                }
                if(request.getThese().getStatut() != null) {
                    these.setStatut(These.Statut.valueOf(request.getThese().getStatut()));
                }
                
                theseRepository.save(these);
            } else {
                return ResponseEntity.badRequest().body("These not found");
            }

            // 4. Prepare response
            Map<String, Object> response = new HashMap<>();
            
            // User response
            Users user = userRepository.findById(request.getUser().getId()).get();
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("firstName", user.getFirstName());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("phoneNumber", user.getPhoneNumber());
            userMap.put("birthdate", user.getBirthDate() != null ? user.getBirthDate().format(dateFormatter) : null);
            userMap.put("Nationality", user.getNationality());
            if(user.getEtablissement() != null) {
                userMap.put("etablissementNom", user.getEtablissement().getNom());
                userMap.put("etablissementId", user.getEtablissement().getId());
            }
            response.put("user", userMap);
            
            // Doctorant response
            Doctorants doctorant = doctorantRepository.findById(request.getDoctorant().getId()).get();
            Map<String, Object> doctorantMap = new HashMap<>();
            doctorantMap.put("id", doctorant.getId());
            doctorantMap.put("numeroInscription", doctorant.getNumeroInscription());
            if(doctorant.getUniteRecherche() != null) {
                doctorantMap.put("UniteRechercheNom", doctorant.getUniteRecherche().getNom());
                doctorantMap.put("UniteRechercheId", doctorant.getUniteRecherche().getId());
            }
            response.put("doctorant", doctorantMap);
            
            // These response
            These these = theseRepository.findById(request.getThese().getId()).get();
            Map<String, Object> theseMap = new HashMap<>();
            theseMap.put("id", these.getId());
            theseMap.put("titre", these.getTitre());
            theseMap.put("resume", these.getResume());
            theseMap.put("dateSoumission", these.getDateSoumission() != null ? these.getDateSoumission().format(dateFormatter) : null);
            theseMap.put("dateInscription", these.getDateInscription() != null ? these.getDateInscription().format(dateFormatter) : null);
            theseMap.put("dateFinVisee", these.getDateFinVisee() != null ? these.getDateFinVisee().format(dateFormatter) : null);
            theseMap.put("dateSoutenance", these.getDateSoutenance() != null ? these.getDateSoutenance().format(dateFormatter) : null);
            theseMap.put("etatThese", these.getEtatThese());
            theseMap.put("statut", these.getStatut() != null ? these.getStatut().name() : null);
            response.put("these", theseMap);
            
            return ResponseEntity.ok(response);
            
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Error updating profile: " + e.getMessage());
        }
    }
}