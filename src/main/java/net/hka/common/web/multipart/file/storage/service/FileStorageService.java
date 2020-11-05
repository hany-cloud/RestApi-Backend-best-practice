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
	private final static char FILE_CODE_SEPERATOR = '_';
	
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
		
		this.fileCode = new StringBuilder(fileCodePrefix).append(FILE_CODE_SEPERATOR).append(UUID.randomUUID().toString().substring(26).toUpperCase()).toString();
		
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
	
	
	public String storeFile(final MultipartFile file) throws FileStorageException, IllegalArgumentException {
		
		if(file == null) throw new IllegalArgumentException("The paremter is null");
		
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
	
	public  String storeFile(final MultipartFile file, String fileCode) throws FileStorageException, IllegalArgumentException {
		
		if(file == null) throw new IllegalArgumentException("The file paremter is null");
		if(fileCode.isEmpty()) setFileCodePrefix(DEFAULT_FILE_CODE_PREFIX);
		
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)            
            Path targetLocation = this.fileStorageLocation.resolve(fileCode);
            logger.info("FileStorageService : {} " + "Save file to " + targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

	
    public Resource loadFileAsResource(final String fileName) throws FileStorageException, IllegalArgumentException {
    	
    	if(fileName.isEmpty()) throw new IllegalArgumentException("The paremter is null");
    	
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
    
    
    public void deleteFile(final MultipartFile file) throws FileStorageException, IllegalArgumentException {
    	
    	if(file == null) throw new IllegalArgumentException("The paremter is null");
		
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        deleteByFileName(fileName);
    }
    
    public void deleteFile(final String fileName) throws FileStorageException, IllegalArgumentException {
    	
    	if(fileName.isEmpty()) throw new IllegalArgumentException("The paremter is null");
		    
    	deleteByFileName(fileName);
    }
           
    public void deleteFiles(final MultipartFile[] files) throws FileStorageException, IllegalArgumentException {
    	
    	if(files == null) throw new IllegalArgumentException("The paremter is null");
    	
        Arrays.asList(files).forEach(file -> this.deleteFile(file));
    } 
    
    public void deleteFiles(final String[] fileNames) throws FileStorageException, IllegalArgumentException {
    	
    	if(fileNames == null) throw new IllegalArgumentException("The paremter is null");
    	
        Arrays.asList(fileNames).forEach(fileName -> this.deleteFile(fileName));
    } 
    
    
    public FileResource upload(final MultipartFile file) throws FileStorageException, IllegalArgumentException {
    	
    	if(file == null) throw new IllegalArgumentException("The paremter is null");
    	
    	String generatedCode = new StringBuilder(this.fileCode).append(FILE_CODE_SEPERATOR).append(UUID.randomUUID().toString().substring(26).toUpperCase()).toString();
        String fileName = storeFile(file, generatedCode);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(this.getSpringMvcServletPath() + "/files/download/")
            .path(generatedCode)
            .toUriString();
        
        return FileResource.create(generatedCode,
        		fileName, fileDownloadUri,
        		file.getContentType(), file.getSize());
    }
    
    public List<FileResource> upload(final MultipartFile[] files) throws FileStorageException, IllegalArgumentException {
    	
    	if(files == null) throw new IllegalArgumentException("The paremter is null");
    	
        return Arrays.asList(files)
            .stream()
            .map(file -> this.upload(file))
            .collect(Collectors.toList());
    }    

    public void delete(final FileResource fileResource) throws FileStorageException, IllegalArgumentException {
    	
    	if(fileResource == null) throw new IllegalArgumentException("The paremter is null");
    	
    	deleteFile(fileResource.getFileCode());
    }
    
    public void delete(final List<FileResource> fileResources) throws FileStorageException, IllegalArgumentException {
    	
    	if(fileResources == null) throw new IllegalArgumentException("The paremter is null");
    	System.out.println(fileResources);
        fileResources.forEach(file -> this.delete(file));
    }
    
    
    public String getSpringMvcServletPath() {
        return springMvcServletPath.isEmpty() ? "" : springMvcServletPath;
    }
    
	public void setFileCodePrefix(String fileCodePrefix) {
		if(fileCodePrefix.isEmpty()) fileCodePrefix = DEFAULT_FILE_CODE_PREFIX;
		this.fileCode = new StringBuilder(fileCodePrefix).append(FILE_CODE_SEPERATOR).append(UUID.randomUUID().toString().substring(26).toUpperCase()).toString();
	}

	
	private void deleteByFileName(String fileName) {
    	try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Delete file from the target location
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            logger.info("FileStorageService : {} " + "Delete file from " + targetLocation);
            Files.delete(targetLocation);

        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file " + fileName + ". Please try again!", ex);
        }
    }
}
