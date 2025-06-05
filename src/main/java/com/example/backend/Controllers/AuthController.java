package com.example.backend.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
// import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Models.UniteRecherche;
import com.example.backend.Models.Etablissement;
import com.example.backend.Repositories.EtablissementRepository;
import com.example.backend.Repositories.UniteRechercheRepository;
import com.example.backend.Dto.RegisterRequest;
import com.example.backend.Models.Docteur;
import com.example.backend.Models.Doctorants;
import com.example.backend.Models.Encadrant;
import com.example.backend.Models.Encadrement;
// import com.example.backend.Models.Etablissement;
import com.example.backend.Models.Users;
import com.example.backend.jwtModule.utils.JwtUtil;
import com.example.backend.Repositories.DocteurRepository;
import com.example.backend.Repositories.DoctorantRepository;
import com.example.backend.Repositories.EncadrantRepository;
// import com.example.backend.repositories.SupervisionRepository;
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
  // private final SupervisionRepository supervisionRepository;
  private final UserRepository usersRepository;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailsender;
  private final EncadrantRepository encadrantRepository;
  private final DoctorantRepository doctorantRepository;
  private final TheseRepository theseRepository;
  private final DocteurRepository docteurRepository;
  private final UniteRechercheRepository uniteRechercheRepository;
  private final EtablissementRepository etablissementRepository;

  public AuthController(UserRepository usersRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, EncadrantRepository encadrantRepository, DoctorantRepository doctorantRepository,TheseRepository theseRepository, DocteurRepository docteurRepository, UniteRechercheRepository uniteRechercheRepository, EtablissementRepository etablissementRepository) {
    this.usersRepository = usersRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailsender = mailSender;
    this.encadrantRepository = encadrantRepository;
    this.doctorantRepository =doctorantRepository;
    this.theseRepository = theseRepository;
    this.docteurRepository = docteurRepository;
    this.uniteRechercheRepository = uniteRechercheRepository;
    this.etablissementRepository = etablissementRepository;
    // this.supervisionRepository = supervisionRepository;
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
  public ResponseEntity<?> login(@RequestBody Map<String,String> loginrequest,HttpServletResponse response
   ) {
    
    
    System.out.println("email" + loginrequest.get("email"));
    String email = loginrequest.get("email");
    String password = loginrequest.get("password");
    var userOptional = usersRepository.findByEmail(email);
    if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {

      // model.addAttribute("error", "Invallid email ou password");
      return ResponseEntity.status(401).body(Map.of("error"," Invallid email ou mot de pass"));
    }

    if (userOptional.get().getIsVerified() == false) {
      String token = UUID.randomUUID().toString();
      userOptional.get().setVerificationToken(token);
      usersRepository.save(userOptional.get());
      String verifyLink = "http://localhost:8084/verify?token=" + token;
      sendVerificationEmail(userOptional.get().getEmail(), userOptional.get().getUsername(), verifyLink);
      // model.addAttribute("message", "Un email de verification a ete envoyer ");

      return  ResponseEntity.status(403).body(Map.of("message", "Un email de verification a ete envoyer "));
    }

    String token = jwtUtil.generateToken(email);

    Cookie jwtCookie = new Cookie("Authorization", token);

    

    jwtCookie.setHttpOnly(true);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(24 * 60 * 60);

    response.addCookie(jwtCookie);

    System.out.println(" JWT Cookie set " + jwtCookie.getValue());

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // String username = authentication.getName();
    // System.out.println("Username: " + username);

    // return ResponseEntity.ok(Map.of("user", userOptional.get(),"redirectUrl", "/testbackend"));
    return ResponseEntity.ok(Map.of("user", userOptional.get()));


  }

  private void sendVerificationEmail(String email, String username, String verifyLink) {

    String subject = "Verification de votre email";
    String htmlContent = "<div style='font-family: Arial, sans-serif; background: #f4f8fb; padding: 40px 0;'>" +
    "<div style='max-width: 480px; margin: auto; background: #fff; border-radius: 12px; box-shadow: 0 4px 24px rgba(0,0,0,0.07); padding: 32px;'>" +
    "<img src='https://cdn-icons-png.flaticon.com/512/561/561127.png' alt='Mail Icon' width='64' style='margin-bottom: 16px;'/>" +
    "<h2 style='color: #2d3748;'>Bienvenue, <span style='color: #3182ce;'>" + username + "</span> !</h2>" +
    "<p style='color: #4a5568; font-size: 16px;'>Merci pour votre inscription chez <b>theses.mr</b> !<br>Veuillez vérifier votre email en cliquant sur le bouton ci-dessous :</p>" +
    "<a href='" + verifyLink + "' style='display: inline-block; margin: 24px 0 16px 0; padding: 14px 32px; background: linear-gradient(90deg, #3182ce 0%, #63b3ed 100%); color: #fff; font-size: 18px; border-radius: 8px; text-decoration: none; font-weight: bold; box-shadow: 0 2px 8px rgba(49,130,206,0.15); transition: background 0.3s;'>Vérifier mon email</a>" +
    "<p style='color: #a0aec0; font-size: 13px;'>Si vous n'avez pas demandé cela, ignorez ce message.</p>" +
    "<hr style='margin: 24px 0; border: none; border-top: 1px solid #e2e8f0;'/>" +
    "<p style='color: #718096; font-size: 12px;'>L'équipe theses.mr</p>" +
    "</div></div>";
    try {
      MimeMessage message = mailsender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(email);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);
      mailsender.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }

  }

 @GetMapping("/verify")
public void verifyEmail(@RequestParam Map<String,String> Token, HttpServletResponse response) throws IOException {
    String token = Token.get("token");
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
    Users user = request.getUser();
    Doctorants doctorant = request.getDoctorant();
    These these = request.getThese();
    // @RequestBody Encadrant encadrant,
    
    System.out.println("name"+ user.getUsername());
    if (user.getEmail() == null || user.getPassword() == null) {
        return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
    }
    if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
      // model.addAttribute("error", "Email existe deja");
      return ResponseEntity.badRequest().body(Map.of("error", "Email existe deja"));
    }

    Users newUser = new Users();
    newUser.setFirstName(user.getFirstName());
    newUser.setLastName(user.getLastName());
    newUser.setPhoneNumber(user.getPhoneNumber());
    newUser.setNationality(user.getNationality());
    newUser.setBirthDate(user.getBirthDate());
    newUser.setUsername(user.getUsername());
    newUser.setEmail(user.getEmail());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    newUser.setRole(user.getRole());
    usersRepository.save(newUser);
    if(user.getRole() == Users.Role.doctorant){
    //  var doctorant = usersRepository.findByEmail(newUser.getEmail());
    // Long id_doctorant = newUser.getId();
    
    Doctorants newDoctorant = new Doctorants();
    newDoctorant.setUser(newUser);
    newDoctorant.setNumeroInscription(doctorant.getNumeroInscription());
    newDoctorant.setUniteRecherche(doctorant.getUniteRecherche());
    newDoctorant.setAnneeInscription(doctorant.getAnneeInscription());
    // newDoctorant.setDocteur(doctorant.getDocteur());
    if (doctorant.getDocteur() == null || doctorant.getDocteur().getId() == null) {
        return ResponseEntity.badRequest().body(Map.of("error", "Docteur ID is required"));
    }

    Docteur realDocteur = docteurRepository.findById(doctorant.getDocteur().getId())
        .orElseThrow(() -> new RuntimeException("Docteur not found with id " + doctorant.getDocteur().getId()));

    newDoctorant.setDocteur(realDocteur);
    doctorantRepository.save(newDoctorant);
    
  //  Users encadrant = usersRepository.findByUsername(encadrant.getUsername());

  //  Encadrant encadrant = usersRepository.findByName(encadrant.get)
            // .orElseThrow(() -> new RuntimeException("Supervisor not found"));
  //  Supervision supervision = new Supervision();
  //  Supervision supervision = new Supervision();
  //       supervision.setSupervisor(supervisor);
  //       supervision.setDoctoralStudent(newDoctorant);
  //       supervision.setStartDate(LocalDate.now());
        // SupervisionRepository.save(supervision);
    // }


    These newthese = new These();
    newthese.setTitre(these.getTitre());
    newthese.setResume(these.getResume());
    newthese.setDateInscription(these.getDateInscription());
    newthese.setDoctorant(newDoctorant);
    theseRepository.save(newthese);
    
    
    



    }


    String token = UUID.randomUUID().toString();
    newUser.setVerificationToken(token);
    newUser.setIsVerified(false);

    usersRepository.save(newUser);

    String verifyLink = "http://localhost:8084/verify?token=" + token;
    sendVerificationEmail(user.getEmail(), user.getUsername(), verifyLink);

    // model.addAttribute("message", "Un email de verification a ete envoyer ");

    return ResponseEntity.ok(Map.of("message", "Un email de verification a ete envoyer "));

  }

  private void sendResetEmail(String email, String resetLink) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("Reinitialisation du mot de passe");
    message.setText("appuier sur le lein pour reinitialiser le mot de passe:\n" + resetLink);
    mailsender.send(message);
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestBody Map<String,String> Email) { 
     String email = Email.get("email");
    var userOptional = usersRepository.findByEmail(email);
    if (userOptional.isEmpty()) {
      // model.addAttribute("message", "l`email entrer n`existe pas ");
      return ResponseEntity.badRequest().body(Map.of("message", "l`email entrer n`existe pas "));
    }
    Users user = userOptional.get();
    String resetToken = UUID.randomUUID().toString();
    user.setResetToken(resetToken);
    usersRepository.save(user);
    // String resetLink = "http://localhost:8084/reset-password?rstoken=" + resetToken;
    //http://localhost:5173/pages/
    String resetLink = "http://localhost:5173/pages/resetpassword?rstoken=" + resetToken;
    sendResetEmail(userOptional.get().getEmail(), resetLink);

    // model.addAttribute("message", "Lien de reinitialisation de mot de passe a eter envoyer");

    return ResponseEntity.ok(Map.of("message", "Lien de reinitialisation de mot de passe a eter envoyer"));

  }

  @GetMapping("/reset-password")
public ResponseEntity<?> verifyResetToken(@RequestParam String rstoken) {
  var userOptional = usersRepository.findByResetToken(rstoken);
  if (userOptional.isEmpty()) {
    return ResponseEntity.badRequest().body(Map.of("error", "Le lien n est pas valide ou expire"));
  }
  return ResponseEntity.ok(Map.of("message", "Token valide", "rstoken", rstoken));
}

  @PostMapping("/reset-password")
  public ResponseEntity<?> changePassword(@RequestBody Map<String,String> passwordData) {
    String rstoken = passwordData.get("rstoken");
    String newpass = passwordData.get("newpass");
    String confirmpass = passwordData.get("confirmpass");
    var userOptional = usersRepository.findByResetToken(rstoken);
    
    if (!newpass.equals(confirmpass)) {
      // model.addAttribute("error", "Les mots de passe ne sont pas identique ");
      // model.addAttribute("rstoken", rstoken);
      return ResponseEntity.badRequest().body(Map.of("error", "Les mots de passe ne sont pas identique ","rstoken", rstoken));

    }

    Users user = userOptional.get();
    user.setResetToken(null);
    user.setPassword(passwordEncoder.encode(newpass));
    usersRepository.save(user);
    // model.addAttribute("message", "mot de pass a ete reinitialiser avec succes");
    
    return ResponseEntity.ok(Map.of("message", "mot de pass a ete reinitialiser avec succes"));

  }
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
