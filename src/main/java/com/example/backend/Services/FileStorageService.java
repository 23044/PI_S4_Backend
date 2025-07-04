package com.example.backend.Services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.Models.FileEntity;
import com.example.backend.Models.Users;
import com.example.backend.Repositories.FileInfoRepository;
import com.example.backend.Repositories.UserRepository;
import com.example.backend.exception.FileStorageException;
import com.example.backend.property.FileStorageProperties;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private UserRepository userRepository;

    // Initialisation du chemin de stockage à partir des propriétés
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException(
                    "Impossible de créer le répertoire où les fichiers téléchargés seront stockés.", ex);
        }
    }

    public FileEntity storeFile(MultipartFile file, Long userId, String title, String type, String notes) {
        try {
            // Nettoyage du nom d'origine
            String originalName = StringUtils.cleanPath(file.getOriginalFilename());

            // Supprimer un éventuel préfixe numérique suivi de "_"
            originalName = originalName.replaceFirst("^[0-9]+_", "");

            // Vérification de sécurité
            if (originalName.contains("..")) {
                throw new FileStorageException("Nom de fichier invalide : " + originalName);
            }

            // 🔥 Construire le chemin complet avec le nom d'origine
            Path targetLocation = this.fileStorageLocation.resolve(originalName);

            // Écraser un fichier existant avec le même nom (ou le refuser selon besoin)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 🔎 Récupérer l'utilisateur
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + userId));

            // 📝 Enregistrer les métadonnées
            FileEntity fileEntity = new FileEntity();
            fileEntity.setTitle(title);
            fileEntity.setType(type);
            fileEntity.setNotes(notes);
            fileEntity.setFileName(originalName); // 🟢 nom affiché et réel
            fileEntity.setFileType(file.getContentType());
            fileEntity.setFileSize(file.getSize());
            fileEntity.setUploadDate(LocalDateTime.now());
            fileEntity.setUser(user);
            fileEntity.setFilePath("/api/files/download/" + originalName); // URL d'accès

            fileInfoRepository.save(fileEntity);

            return fileEntity;

        } catch (IOException ex) {
            throw new FileStorageException("Erreur stockage fichier : " + file.getOriginalFilename(), ex);
        }
    }

    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Fichier non trouvé : " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Fichier non trouvé : " + fileName);
        }
    }

    public List<FileEntity> getAllFiles() {
        return fileInfoRepository.findAll();
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.fileStorageLocation, 1)
                    .filter(path -> !path.equals(this.fileStorageLocation))
                    .map(this.fileStorageLocation::relativize);
        } catch (IOException e) {
            throw new FileStorageException("Impossible de lire les fichiers stockés", e);
        }
    }

    public List<FileEntity> getFilesByUserId(Long userId) {
        return fileInfoRepository.findByUserId(userId);
    }
}