package com.example.backend.Services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
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
import com.example.backend.Dto.FileInfo;

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

    public FileEntity storeFile(MultipartFile file, Long userId) {
        // Génère un nom de fichier unique
        String fileName =  StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Vérifie que le nom est sûr
            if (fileName.contains("..")) {
                throw new FileStorageException("Désolé ! Le nom du fichier est invalide : " + fileName);
            }

            // Recherche de l'utilisateur
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId));

            // Copie physique du fichier
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Enregistrement des métadonnées dans FileInfo
            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(fileName);
            fileInfo.setUrl("/api/files/download/" + fileName);
            fileInfo.setUploadDate(LocalDateTime.now());
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(file.getContentType());
            fileInfo.setUserId(user.getId());
            fileInfoRepository.save(fileInfo);

            // Création de l'objet FileEntity (si utilisé ailleurs)
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setFilePath("/api/files/download/" + fileName);
            fileEntity.setFileSize(file.getSize());
            fileEntity.setFileType(file.getContentType());
            fileEntity.setUser(user);

            return fileEntity;

        } catch (IOException ex) {
            throw new FileStorageException("Impossible de stocker le fichier " + fileName + ". Réessayez !", ex);
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

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.fileStorageLocation, 1)
                    .filter(path -> !path.equals(this.fileStorageLocation))
                    .map(this.fileStorageLocation::relativize);
        } catch (IOException e) {
            throw new FileStorageException("Impossible de lire les fichiers stockés", e);
        }
    }
}
