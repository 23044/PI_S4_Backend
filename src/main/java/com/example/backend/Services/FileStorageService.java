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

            if (originalName.contains("..")) {
                throw new FileStorageException("Nom de fichier invalide : " + originalName);
            }

            // Générer un nom unique avec UUID
            String extension = "";
            int i = originalName.lastIndexOf(".");
            if (i > 0) {
                extension = originalName.substring(i);
            }
            String uniqueFileName = UUID.randomUUID().toString() + extension;

            // Construire le chemin
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);

            // Supprimer un éventuel fichier déjà là avec le même nom
            try {
                Files.deleteIfExists(targetLocation);
            } catch (IOException e) {
                throw new FileStorageException("Impossible de supprimer un ancien fichier : " + uniqueFileName, e);
            }

            // Copier le fichier
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Trouver l'utilisateur
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + userId));

            // Enregistrer l'entité en base
            FileEntity fileEntity = new FileEntity();
            fileEntity.setTitle(title);
            fileEntity.setType(type);
            fileEntity.setNotes(notes);
            fileEntity.setFileName(originalName); // nom original affiché
            fileEntity.setFileType(file.getContentType());
            fileEntity.setFileSize(file.getSize());
            fileEntity.setUploadDate(LocalDateTime.now());
            fileEntity.setUser(user);
            fileEntity.setFilePath("/api/files/download/" + uniqueFileName); // chemin d'accès API

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
