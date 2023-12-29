package com.clebsonsantos.backend.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clebsonsantos.backend.service.CNABService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("cnab")
public class CNABController {

  private final CNABService service;

  public CNABController(CNABService service) {
    this.service = service;
  }

  @PostMapping("upload")
  public String upload(@RequestParam("file") MultipartFile file) throws Exception {
    this.service.uploadCNABFile(file);
    return "Proccess started in background!";
  }
}
