package ru.sbrf.hackaton.fraudbusters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sbrf.hackaton.fraudbusters.FileManager;
import ru.sbrf.hackaton.fraudbusters.exceptions.CustomGenericException;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/file")
public class ClientFileController {

  private FileManager fileManager;

  @Autowired
  public ClientFileController(FileManager fileManager) {
    this.fileManager = fileManager;
  }

  @PostMapping("/send")
  public String sendFile(@RequestParam("file") MultipartFile file) throws Exception {
    String fileName = file.getOriginalFilename();
    if(fileName.isEmpty())
      throw new CustomGenericException("Файл не выбран.");
    return fileManager.sendFile(fileName, file.getInputStream());
  }

  @GetMapping(value = "/get", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
  public void getFile(@RequestParam("uri") String uri, HttpServletResponse response) throws Exception {
    if(uri.isEmpty())
      throw new CustomGenericException("Хэш файла не указан.");
    fileManager.getFile(uri, response.getOutputStream());
  }

  @GetMapping(value = "/pass", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
  public String getPass(@RequestParam("uri") String shaZip) throws Exception {
    if(shaZip.isEmpty())
      throw new CustomGenericException("Хэш zip-файла не указан.");
    return fileManager.getPass(shaZip);
  }

  @GetMapping(value = "/validation", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
  public Boolean validation(@PathVariable("sha") String sha, @PathVariable("shaZip") String shaZip, @PathVariable("pass") String pass){
    fileManager.validation(sha, shaZip, pass);
    return false;
  }
}
