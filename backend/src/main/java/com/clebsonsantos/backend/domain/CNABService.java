package com.clebsonsantos.backend.domain;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CNABService {
  private final Path fileStorageLocation;

  public CNABService(@Value("${file.upload-dir}") String fileUploadDir) {
    this.fileStorageLocation = Paths.get(fileUploadDir);
  }

  public void uploadCNABFile(MultipartFile file) throws Exception {
    var fileName = StringUtils.cleanPath(file.getOriginalFilename());
    var targetLocation = fileStorageLocation.resolve(fileName);
    file.transferTo(targetLocation);
  }
}
