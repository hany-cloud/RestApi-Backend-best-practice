package net.hka.common.web.multipart.file.storage.rest.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.hka.common.web.multipart.file.storage.payload.FileResource;
import net.hka.common.web.multipart.file.storage.service.FileStorageService;

/**
 * Global purpose Restful controller for uploading file(s) 
 * Consider for using this controller to add @ComponentScan for the net.hka.common.web package
 */
@RestController
@RequestMapping("/files")
public class FileUploadController {
	
	@Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public FileResource uploadFile(@RequestParam("file") MultipartFile file) {
    	
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(fileStorageService.getSpringMvcServletPath() + "/files/download/")
            .path(fileName)
            .toUriString();
        
        return FileResource.create(fileStorageService.getFileCode(),
        		fileName, fileDownloadUri,
        		file.getContentType(), file.getSize());
    }

    @PostMapping("/upload/multiple")
    public List<FileResource> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
    	
        return Arrays.asList(files)
            .stream()
            .map(file -> uploadFile(file))
            .collect(Collectors.toList());
    }       
}
