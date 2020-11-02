package net.hka.common.web.multipart.file.storage.payload;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@ToString
@Embeddable
public class FileResource {
	
	private @NotBlank String fileCode;
	
	private @NotBlank String fileName;
	
    private @NotBlank String fileDownloadUri;
    
    private String fileType;
    
    private Long size;

    public static FileResource create(final String fileCode, final String fileName,
    		final String fileDownloadUri, String fileType, Long size) {

		if(fileCode.isEmpty()) throw new IllegalArgumentException("The fileCode paremter is empty");
		if(fileName.isEmpty()) throw new IllegalArgumentException("The fileName paremter is empty");
		if(fileDownloadUri.isEmpty()) throw new IllegalArgumentException("The fileDownloadUri paremter is empty");
		
		return new FileResource(fileCode, fileName, fileDownloadUri, fileType, size);
	}
}
