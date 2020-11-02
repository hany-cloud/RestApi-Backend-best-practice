package net.hka.common.web.multipart.file.storage.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Automatically binding properties defined in the application.properties file
 * to a POJO class. It is recommended to add
 * <artifactId>spring-boot-configuration-processor</artifactId> to pom file
 * 
 * @author Hany Kamal
 */
@Component
@ConfigurationProperties(prefix = "file") // bind file entry from properties file
public class FileStorageProperties {

	private String uploadDir; // bind file.upload-dir from properties file

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}	
}
