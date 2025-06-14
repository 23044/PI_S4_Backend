package com.example.backend.Controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Models.UniteRecherche;
import com.example.backend.Models.Etablissement;
import com.example.backend.Repositories.EtablissementRepository;
import com.example.backend.Repositories.UniteRechercheRepository;
import com.example.backend.Dto.RegisterRequest;
import com.example.backend.Models.Directeur;
import com.example.backend.Models.Doctorants;
import com.example.backend.Models.Encadrant;
import com.example.backend.Models.Users;
import com.example.backend.jwtModule.utils.JwtUtil;
import com.example.backend.Repositories.DirecteurRepository;
import com.example.backend.Repositories.DoctorantRepository;
import com.example.backend.Repositories.EncadrantRepository;
import com.example.backend.Repositories.UserRepository;
import com.example.backend.Models.These;
import com.example.backend.Repositories.TheseRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

  @Autowired
  private JwtUtil jwtUtil;
  private final UserRepository usersRepository;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailsender;
  private final EncadrantRepository encadrantRepository;
  private final DoctorantRepository doctorantRepository;
  private final TheseRepository theseRepository;
  private final DirecteurRepository directeurRepository;
  private final UniteRechercheRepository uniteRechercheRepository;
  private final EtablissementRepository etablissementRepository;

  public AuthController(UserRepository usersRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender,
      EncadrantRepository encadrantRepository, DoctorantRepository doctorantRepository, TheseRepository theseRepository,
      DirecteurRepository directeurRepository, UniteRechercheRepository uniteRechercheRepository,
      EtablissementRepository etablissementRepository) {
    this.usersRepository = usersRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailsender = mailSender;
    this.encadrantRepository = encadrantRepository;
    this.doctorantRepository = doctorantRepository;
    this.theseRepository = theseRepository;
    this.directeurRepository = directeurRepository;
    this.uniteRechercheRepository = uniteRechercheRepository;
    this.etablissementRepository = etablissementRepository;
  }

  @GetMapping("/testbackend")
  public ResponseEntity<?> test() {
    return ResponseEntity.ok(Map.of("message", "hello"));
  }

  @GetMapping("/")
  public ResponseEntity<?> showLoginPage(@RequestParam(value = "error", required = false) String error) {
    if (error != null) {
      return ResponseEntity.badRequest().body(Map.of("error", "Invalid email ou Password"));
    }
    return ResponseEntity.ok(Map.of("message", "Login page accessible"));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> loginrequest, HttpServletResponse response) {
    String email = loginrequest.get("email");
    String password = loginrequest.get("password");
    var userOptional = usersRepository.findByEmail(email);

    if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
      return ResponseEntity.status(401).body(Map.of("error", "Invalid email ou mot de passe"));
    }

    if (userOptional.get().getIsVerified() == false) {
      String token = UUID.randomUUID().toString();
      userOptional.get().setVerificationToken(token);
      usersRepository.save(userOptional.get());
      String verifyLink = "http://localhost:8081/verify?token=" + token;
      sendVerificationEmail(userOptional.get().getEmail(), userOptional.get().getUsername(), verifyLink);
      return ResponseEntity.status(403).body(Map.of("message", "Un email de verification a ete envoye"));
    }

    String token = jwtUtil.generateToken(email);
    Cookie jwtCookie = new Cookie("Authorization", token);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(24 * 60 * 60);
    response.addCookie(jwtCookie);

    return ResponseEntity.ok(Map.of("user", userOptional.get()));
  }

  private void sendVerificationEmail(String email, String username, String verifyLink) {
    String subject = "Verification de votre email";
    String htmlContent = "<div style='font-family: Arial, sans-serif; background: #f4f8fb; padding: 40px 0;'>" +
        "<div style='max-width: 480px; margin: auto; background: #fff; border-radius: 12px; box-shadow: 0 4px 24px rgba(0,0,0,0.07); padding: 32px;'>"
        +
        "<img src='https://cdn-icons-png.flaticon.com/512/561/561127.png' alt='Mail Icon' width='64' style='margin-bottom: 16px;'/>"
        +
        "<h2 style='color: #2d3748;'>Bienvenue, <span style='color: #3182ce;'>" + username + "</span> !</h2>" +
        "<p style='color: #4a5568; font-size: 16px;'>Merci pour votre inscription chez <b>theses.mr</b> !<br>Veuillez vérifier votre email en cliquant sur le bouton ci-dessous :</p>"
        +
        "<a href='" + verifyLink
        + "' style='display: inline-block; margin: 24px 0 16px 0; padding: 14px 32px; background: linear-gradient(90deg, #3182ce 0%, #63b3ed 100%); color: #fff; font-size: 18px; border-radius: 8px; text-decoration: none; font-weight: bold; box-shadow: 0 2px 8px rgba(49,130,206,0.15); transition: background 0.3s;'>Vérifier mon email</a>"
        +
        "<p style='color: #a0aec0; font-size: 13px;'>Si vous n'avez pas demandé cela, ignorez ce message.</p>" +
        "<hr style='margin: 24px 0; border: none; border-top: 1px solid #e2e8f0;'/>" +
        "<p style='color: #718096; font-size: 12px;'>L'équipe theses.mr</p>" +
        "</div></div>";
    try {
      MimeMessage message = mailsender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom("smtpverif793@gmail.com");
      helper.setTo(email);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);

      mailsender.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  @GetMapping("/verify")
  public void verifyEmail(@RequestParam Map<String, String> Token, HttpServletResponse response) throws IOException {
    String token = Token.get("token");
    System.out.println("token knh : " + token);
    var userOptional = usersRepository.findByVerificationToken(token);
    if (userOptional.isEmpty()) {
      response.sendRedirect("http://localhost:5173/error?msg=invalid_or_expired");
      return;
    }
    Users user = userOptional.get();
    user.setIsVerified(true);
    user.setVerificationToken(null);
    usersRepository.save(user);
    response.sendRedirect("http://localhost:5173/");
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
      Users user = request.getUser();
      Doctorants doctorant = request.getDoctorant();
      These these = request.getThese();

      // Vérification des champs utilisateur
      if (user == null || user.getEmail() == null || user.getPassword() == null ||
          user.getFirstName() == null || user.getLastName() == null || user.getRole() == null) {
        return ResponseEntity.badRequest().body(Map.of("error", "Tous les champs utilisateur sont requis"));
      }

      if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity.badRequest().body(Map.of("error", "Email déjà utilisé"));
      }

      if (user.getEtablissement() == null || user.getEtablissement().getId() == null) {
        return ResponseEntity.badRequest().body(Map.of("error", "L'établissement est requis"));
      }

      Etablissement etablissement = etablissementRepository.findById(user.getEtablissement().getId())
          .orElseThrow(() -> new RuntimeException("Établissement non trouvé"));

      // Génération du token de vérification
      String token = UUID.randomUUID().toString();

      // Création utilisateur
      Users newUser = Users.builder()
          .firstName(user.getFirstName())
          .lastname(user.getLastName())
          .email(user.getEmail())
          .username(user.getUsername() != null ? user.getUsername() : "")
          .phoneNumber(user.getPhoneNumber())
          .Nationality(user.getNationality())
          .birthdate(user.getBirthDate())
          .password(passwordEncoder.encode(user.getPassword()))
          .role(user.getRole())
          .etablissement(etablissement)
          .verificationToken(token)
          .isVerified(false)
          .build();

      usersRepository.save(newUser);

      // Si c’est un doctorant
      if (user.getRole() == Users.Role.doctorant) {
        if (doctorant == null) {
          return ResponseEntity.badRequest().body(Map.of("error", "Les informations du doctorant sont requises"));
        }

        if (doctorant.getNumeroInscription() == null || doctorant.getAnneeInscription() == null ||
            doctorant.getDirecteur() == null || doctorant.getDirecteur().getId() == null ||
            doctorant.getUniteRecherche() == null || doctorant.getUniteRecherche().getId() == null) {
          return ResponseEntity.badRequest().body(Map.of("error", "Champs du doctorant manquants"));
        }

        Directeur directeur = directeurRepository.findById(doctorant.getDirecteur().getId())
            .orElseThrow(() -> new RuntimeException("Directeur non trouvé"));

        UniteRecherche unite = uniteRechercheRepository.findById(doctorant.getUniteRecherche().getId())
            .orElseThrow(() -> new RuntimeException("Unité de recherche non trouvée"));

        Doctorants newDoctorant = Doctorants.builder()
            .user(newUser)
            .numeroInscription(doctorant.getNumeroInscription())
            .anneeInscription(doctorant.getAnneeInscription())
            .directeur(directeur)
            .uniteRecherche(unite)
            .build();

        doctorantRepository.save(newDoctorant);

        // Thèse
        if (these != null) {
          if (these.getTitre() == null || these.getResume() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Titre et résumé de la thèse requis"));
          }

          These newThese = new These();
          newThese.setTitre(these.getTitre());
          newThese.setResume(these.getResume());
          newThese.setDateInscription(these.getDateInscription());
          newThese.setDoctorant(newDoctorant);

          theseRepository.save(newThese);
        }
      }

      // Envoi de l'e-mail avec le bon token
      String verifyLink = "http://localhost:8081/verify?token=" + token;
      sendVerificationEmail(newUser.getEmail(), newUser.getUsername(), verifyLink);

      return ResponseEntity.ok(Map.of("message", "Un email de vérification a été envoyé"));

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(Map.of("error", "Erreur serveur: " + e.getMessage()));
    }
  }

  // 4i li 5assr m3 doctorants
  // @PostMapping("/register")
  // public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
  // try {
  // Users user = request.getUser();
  // Doctorants doctorant = request.getDoctorant();
  // These these = request.getThese();

  // // Validation utilisateur
  // if (user == null || user.getEmail() == null || user.getPassword() == null ||
  // user.getFirstName() == null || user.getLastName() == null || user.getRole()
  // == null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Tous les champs
  // obligatoires doivent être remplis"));
  // }

  // if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Email déjà
  // utilisé"));
  // }

  // // Vérification de l'établissement
  // if (user.getEtablissement() == null || user.getEtablissement().getId() ==
  // null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "L'établissement est
  // requis"));
  // }

  // Etablissement etablissement =
  // etablissementRepository.findById(user.getEtablissement().getId())
  // .orElseThrow(() -> new RuntimeException("Établissement non trouvé"));

  // // Création utilisateur
  // Users newUser = new Users();
  // newUser.setFirstName(user.getFirstName());
  // newUser.setLastName(user.getLastName());
  // newUser.setEmail(user.getEmail());
  // newUser.setUsername(user.getUsername() != null ? user.getUsername() : "");
  // newUser.setPhoneNumber(user.getPhoneNumber());
  // newUser.setNationality(user.getNationality());
  // newUser.setBirthDate(user.getBirthDate());
  // newUser.setPassword(passwordEncoder.encode(user.getPassword()));
  // newUser.setRole(user.getRole());
  // newUser.setEtablissement(etablissement);
  // newUser.setVerificationToken(UUID.randomUUID().toString());
  // newUser.setIsVerified(false);

  // usersRepository.save(newUser);

  // // Si c’est un doctorant, enregistrer aussi ses infos
  // if (user.getRole() == Users.Role.doctorant) {
  // if (doctorant == null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Les informations du
  // doctorant sont requises"));
  // }

  // if (doctorant.getNumeroInscription() == null ||
  // doctorant.getAnneeInscription() == null ||
  // doctorant.getDirecteurs() == null || doctorant.getDirecteurs().getId() ==
  // null ||
  // doctorant.getUniteRecherche() == null ||
  // doctorant.getUniteRecherche().getId() == null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Tous les champs du
  // doctorant doivent être remplis"));
  // }

  // Directeur directeur =
  // directeurRepository.findById(doctorant.getDirecteurs().getId())
  // .orElseThrow(() -> new RuntimeException("Directeur non trouvé"));

  // UniteRecherche unite =
  // uniteRechercheRepository.findById(doctorant.getUniteRecherche().getId())
  // .orElseThrow(() -> new RuntimeException("Unité de recherche non trouvée"));

  // Doctorants newDoctorant = new Doctorants();
  // newDoctorant.setUser(newUser);
  // newDoctorant.setNumeroInscription(doctorant.getNumeroInscription());
  // newDoctorant.setAnneeInscription(doctorant.getAnneeInscription());
  // newDoctorant.setDirecteurs(directeur); // Très important
  // newDoctorant.setUniteRecherche(unite);

  // doctorantRepository.save(newDoctorant);

  // // Gestion de la thèse
  // if (these != null) {
  // if (these.getTitre() == null || these.getResume() == null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Titre et résumé de
  // la thèse sont requis"));
  // }

  // These newThese = new These();
  // newThese.setTitre(these.getTitre());
  // newThese.setResume(these.getResume());
  // newThese.setDateInscription(these.getDateInscription());
  // newThese.setDoctorant(newDoctorant);

  // theseRepository.save(newThese);
  // }
  // }

  // // Envoi de l’email de vérification
  // String verifyLink = "http://localhost:8081/verify?token=" +
  // newUser.getVerificationToken();
  // sendVerificationEmail(user.getEmail(), user.getUsername(), verifyLink);

  // return ResponseEntity.ok(Map.of("message", "Un email de vérification a été
  // envoyé"));

  // } catch (Exception e) {
  // e.printStackTrace();
  // return ResponseEntity.status(500).body(Map.of("error", "Erreur serveur: " +
  // e.getMessage()));
  // }
  // }

  // @PostMapping("/register")
  // public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
  // try {
  // Users user = request.getUser();
  // Doctorants doctorant = request.getDoctorant();
  // These these = request.getThese();

  // // Validation utilisateur
  // if (user == null || user.getEmail() == null || user.getPassword() == null ||
  // user.getFirstName() == null || user.getLastName() == null || user.getRole()
  // == null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Tous les champs
  // obligatoires doivent être remplis"));
  // }

  // if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Email déjà
  // utilisé"));
  // }

  // System.err.println("-------------------------etabilisement --: " +
  // user.getEtablissement().getId());
  // // Vérification de l'établissement pour tous les utilisateurs
  // if (user.getEtablissement() == null || user.getEtablissement().getId() ==
  // null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "L'établissement est
  // requis"));
  // }

  // // Récupération de l'établissement
  // Etablissement etablissement =
  // etablissementRepository.findById(user.getEtablissement().getId())
  // .orElseThrow(() -> new RuntimeException("Établissement non trouvé"));

  // // Création utilisateur
  // Users newUser = new Users();
  // newUser.setFirstName(user.getFirstName());
  // newUser.setLastName(user.getLastName());
  // newUser.setEmail(user.getEmail());
  // newUser.setUsername(user.getUsername() != null ? user.getUsername() : "");
  // newUser.setPhoneNumber(user.getPhoneNumber());
  // newUser.setNationality(user.getNationality());
  // newUser.setBirthDate(user.getBirthDate());
  // newUser.setPassword(passwordEncoder.encode(user.getPassword()));
  // newUser.setRole(user.getRole());
  // newUser.setEtablissement(etablissement); // Assignation de l'établissement
  // newUser.setVerificationToken(UUID.randomUUID().toString());
  // newUser.setIsVerified(false);

  // usersRepository.save(newUser);

  // // Gestion doctorant
  // if (user.getRole() == Users.Role.doctorant) {
  // if (doctorant == null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Les informations du
  // doctorant sont requises"));
  // }

  // // Validation doctorant
  // if (doctorant.getNumeroInscription() == null ||
  // doctorant.getAnneeInscription() == null ||
  // doctorant.getDirecteur() == null || doctorant.getDirecteur().getId() == null
  // ||
  // doctorant.getUniteRecherche() == null ||
  // doctorant.getUniteRecherche().getId() == null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Tous les champs du
  // doctorant doivent être remplis"));
  // }

  // Directeur directeur =
  // directeurRepository.findById(doctorant.getDirecteur().getId())
  // .orElseThrow(() -> new RuntimeException("Directeur non trouvé"));
  // UniteRecherche unite =
  // uniteRechercheRepository.findById(doctorant.getUniteRecherche().getId())
  // .orElseThrow(() -> new RuntimeException("Unité de recherche non trouvée"));

  // Doctorants newDoctorant = new Doctorants();
  // newDoctorant.setUser(newUser);
  // newDoctorant.setNumeroInscription(doctorant.getNumeroInscription());
  // newDoctorant.setAnneeInscription(doctorant.getAnneeInscription());
  // newDoctorant.setDirecteur(directeur);
  // newDoctorant.setUniteRecherche(unite);
  // // L'établissement est déjà associé via l'utilisateur

  // doctorantRepository.save(newDoctorant);

  // // Gestion thèse
  // if (these != null) {
  // if (these.getTitre() == null || these.getResume() == null) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Titre et résumé de
  // la thèse sont requis"));
  // }

  // These newThese = new These();
  // newThese.setTitre(these.getTitre());
  // newThese.setResume(these.getResume());
  // newThese.setDateInscription(these.getDateInscription());
  // newThese.setDoctorant(newDoctorant);

  // theseRepository.save(newThese);
  // }
  // }

  // // Envoi email de vérification
  // String verifyLink = "http://localhost:8081/verify?token=" +
  // newUser.getVerificationToken();
  // sendVerificationEmail(user.getEmail(), user.getUsername(), verifyLink);

  // return ResponseEntity.ok(Map.of("message", "Un email de vérification a été
  // envoyé"));

  // } catch (Exception e) {
  // e.printStackTrace();
  // return ResponseEntity.status(500).body(Map.of("error", "Erreur serveur: " +
  // e.getMessage()));
  // }
  // }

  // private void sendResetEmail(String email, String resetLink) {
  // SimpleMailMessage message = new SimpleMailMessage();
  // message.setTo(email);
  // message.setSubject("Reinitialisation du mot de passe");
  // message.setText("Cliquez sur le lien pour reinitialiser le mot de passe:\n" +
  // resetLink);
  // mailsender.send(message);
  // }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> emailRequest) {
    try {
      // Validation de l'email
      if (emailRequest == null || !emailRequest.containsKey("email")) {
        return ResponseEntity.badRequest().body(Map.of("error", "Email requis"));
      }

      String email = emailRequest.get("email");
      var userOptional = usersRepository.findByEmail(email);

      // Réponse générique pour éviter l'énumération d'emails
      if (userOptional.isEmpty()) {
        return ResponseEntity.ok()
            .body(Map.of("message", "Si l'email existe, un lien de réinitialisation a été envoyé"));
      }

      Users user = userOptional.get();

      // Générer un token avec expiration (1 heure)
      String resetToken = UUID.randomUUID().toString();
      user.setResetToken(resetToken);
      // user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
      usersRepository.save(user);

      // Construire le lien de réinitialisation
      String resetLink = "http://localhost:5173/pages/resetpassword?rstoken=" + resetToken;

      // Envoyer l'email avec la même méthode que register
      sendResetEmail(user.getEmail(), resetLink);

      return ResponseEntity.ok().body(Map.of("message", "Si l'email existe, un lien de réinitialisation a été envoyé"));

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "Erreur lors du traitement de la demande"));
    }
  }

  @GetMapping("/reset-password")
  public ResponseEntity<?> verifyResetToken(@RequestParam String rstoken) {
    try {
      if (rstoken == null || rstoken.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("error", "Token invalide"));
      }

      var userOptional = usersRepository.findByResetToken(rstoken);

      if (userOptional.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("error", "Lien invalide"));
      }

      Users user = userOptional.get();

      // if (user.getResetTokenExpiry() != null &&
      // user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
      // return ResponseEntity.badRequest().body(Map.of("error", "Le lien a expiré"));
      // }

      return ResponseEntity.ok(Map.of("message", "Token valide", "rstoken", rstoken));

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "Erreur lors de la vérification du token"));
    }
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordData) {
    try {
      // Validation des données
      if (passwordData == null || !passwordData.containsKey("rstoken")
          || !passwordData.containsKey("newpass") || !passwordData.containsKey("confirmpass")) {
        return ResponseEntity.badRequest().body(Map.of("error", "Données manquantes"));
      }

      String rstoken = passwordData.get("rstoken");
      String newpass = passwordData.get("newpass");
      String confirmpass = passwordData.get("confirmpass");

      // Validation du mot de passe
      if (newpass == null || newpass.length() < 8) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "Le mot de passe doit contenir au moins 8 caractères"));
      }

      if (!newpass.equals(confirmpass)) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "Les mots de passe ne correspondent pas"));
      }

      var userOptional = usersRepository.findByResetToken(rstoken);

      if (userOptional.isEmpty()) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "Lien invalide"));
      }

      Users user = userOptional.get();

      // Vérifier l'expiration du token
      // if (user.getResetTokenExpiry() != null &&
      // user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
      // return ResponseEntity.badRequest()
      // .body(Map.of("error", "Le lien a expiré"));
      // }

      // Mettre à jour le mot de passe
      user.setResetToken(null);
      // user.setResetTokenExpiry(null);
      user.setPassword(passwordEncoder.encode(newpass));
      usersRepository.save(user);

      return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "Erreur lors de la réinitialisation du mot de passe"));
    }
  }

  // Méthode d'envoi d'email (identique à celle utilisée dans register)
  private void sendResetEmail(String toEmail, String resetLink) throws MessagingException {
    MimeMessage message = mailsender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom("smtpverif793@gmail.com"); // Doit correspondre à votre config SMTP
    helper.setTo(toEmail);
    helper.setSubject("Réinitialisation de votre mot de passe");

    String content = "<p>Bonjour,</p>"
        + "<p>Vous avez demandé à réinitialiser votre mot de passe.</p>"
        + "<p>Cliquez sur le lien ci-dessous pour changer votre mot de passe:</p>"
        + "<p><a href=\"" + resetLink + "\">Changer mon mot de passe</a></p>"
        + "<br>"
        + "<p>Ce lien expirera dans 1 heure.</p>"
        + "<p>Ignorez cet email si vous n'avez pas fait cette demande.</p>";

    helper.setText(content, true);
    mailsender.send(message);
  }

  // @PostMapping("/forgot-password")
  // public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String>
  // Email) {
  // String email = Email.get("email");
  // var userOptional = usersRepository.findByEmail(email);
  // if (userOptional.isEmpty()) {
  // return ResponseEntity.badRequest().body(Map.of("message", "L'email entré
  // n'existe pas"));
  // }
  // Users user = userOptional.get();
  // String resetToken = UUID.randomUUID().toString();
  // user.setResetToken(resetToken);
  // usersRepository.save(user);
  // String resetLink = "http://localhost:5173/pages/resetpassword?rstoken=" +
  // resetToken;
  // sendResetEmail(userOptional.get().getEmail(), resetLink);

  // return ResponseEntity.ok(Map.of("message", "Lien de reinitialisation de mot
  // de passe a ete envoye"));
  // }

  // @GetMapping("/reset-password")
  // public ResponseEntity<?> verifyResetToken(@RequestParam String rstoken) {
  // var userOptional = usersRepository.findByResetToken(rstoken);
  // if (userOptional.isEmpty()) {
  // return ResponseEntity.badRequest().body(Map.of("error", "Le lien n'est pas
  // valide ou a expire"));
  // }
  // return ResponseEntity.ok(Map.of("message", "Token valide", "rstoken",
  // rstoken));
  // }

  // @PostMapping("/reset-password")
  // public ResponseEntity<?> changePassword(@RequestBody Map<String, String>
  // passwordData) {
  // String rstoken = passwordData.get("rstoken");
  // String newpass = passwordData.get("newpass");
  // String confirmpass = passwordData.get("confirmpass");
  // var userOptional = usersRepository.findByResetToken(rstoken);

  // if (!newpass.equals(confirmpass)) {
  // return ResponseEntity.badRequest()
  // .body(Map.of("error", "Les mots de passe ne sont pas identiques", "rstoken",
  // rstoken));
  // }

  // Users user = userOptional.get();
  // user.setResetToken(null);
  // user.setPassword(passwordEncoder.encode(newpass));
  // usersRepository.save(user);

  // return ResponseEntity.ok(Map.of("message", "Mot de passe a ete reinitialise
  // avec succes"));
  // }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("Authorization", null);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    return ResponseEntity.ok(Map.of("message", "Deconnexion reussie", "redirectUrl", "/login"));
  }

  @GetMapping("/unites-recherche")
  public List<UniteRecherche> getUnitesRecherche() {
    return uniteRechercheRepository.findAll();
  }

  @GetMapping("/ecoles-doctorales")
  public List<Etablissement> getEcolesDoctorales() {
    return etablissementRepository.findAll();
  }
}