package net.hka.common.web.multipart.file.storage.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import net.hka.common.web.multipart.file.storage.exception.FileNotFoundException;
import net.hka.common.web.multipart.file.storage.exception.FileStorageException;
import net.hka.common.web.multipart.file.storage.payload.FileResource;
import net.hka.common.web.multipart.file.storage.property.FileStorageProperties;

@Service
@Slf4j
public class FileStorageService {

	private final static String DEFAULT_FILE_CODE_PREFIX = "DEFAULT_FILE_CODE_PREFIX";
	
	// Store file with unique code
	private String fileCode;
	
	// Store spring MVC Servlet Path from application.properties/yml
	@Value("${spring.mvc.servlet.path}")
	private String springMvcServletPath;
	
	private final Path fileStorageLocation;
	
	@Autowired
    public FileStorageService(final FileStorageProperties fileStorageProperties) {
		this(fileStorageProperties, DEFAULT_FILE_CODE_PREFIX);
    }
	
	public FileStorageService(final FileStorageProperties fileStorageProperties, String fileCodePrefix) {
		
		this.fileCode = fileCodePrefix + "_" + UUID.randomUUID().toString().substring(26).toUpperCase();
		
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
            .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
	
	public String getFileCode() {
		return fileCode;
    }
	
	public String storeFile(final MultipartFile file) {
		
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            logger.info("FileStorageService : {} " + "Save file to " + targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
	
	private  String storeFile(final MultipartFile file, String fileCode) {
		
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            String generatedCode = "_" + UUID.randomUUID().toString().substring(26).toUpperCase();
            Path targetLocation = this.fileStorageLocation.resolve(fileCode + generatedCode);
            logger.info("FileStorageService : {} " + "Save file to " + targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(final String fileName) {
    	
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
    
    public FileResource uploadFile(final MultipartFile file) {
    	
        String fileName = storeFile(file, this.getFileCode());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(this.getSpringMvcServletPath() + "/files/download/")
            .path(this.getFileCode())
            .toUriString();
        
        return FileResource.create(this.getFileCode(),
        		fileName, fileDownloadUri,
        		file.getContentType(), file.getSize());
    }
    
    public List<FileResource> uploadMultipleFiles(final MultipartFile[] files) {
    	
        return Arrays.asList(files)
            .stream()
            .map(file -> this.uploadFile(file))
            .collect(Collectors.toList());
    }    
    
    public String getSpringMvcServletPath() {
        return springMvcServletPath.isEmpty() ? "" : springMvcServletPath;
    }

	public void setFileCodePrefix(String fileCodePrefix) {
		this.fileCode = fileCodePrefix + "_" + UUID.randomUUID().toString().substring(26).toUpperCase();
	}
}
