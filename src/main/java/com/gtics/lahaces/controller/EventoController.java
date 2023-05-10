package com.gtics.lahaces.controller;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import com.gtics.lahaces.entity.Evento;
import com.gtics.lahaces.repository.EventoRepository;
import com.gtics.lahaces.repository.LocalRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

@Controller
@RequestMapping("/evento")
public class EventoController {

    final EventoRepository eventoRepository;
    final LocalRepository localRepository;

    public EventoController(EventoRepository eventoRepository, LocalRepository localRepository) {
        this.eventoRepository = eventoRepository;
        this.localRepository = localRepository;
    }

    @GetMapping(value = {"", "/", "/listar"})
    public String index(Model model) {
        model.addAttribute("lista", eventoRepository.findAll());
        return "evento/listaEvento";
    }

    @GetMapping("/nuevo")
    public String nuevoEvento(Model model) {
        model.addAttribute("listaLocales", localRepository.findAll());
        return "evento/nuevoEvento";
    }

    @PostMapping("/guardarImagen")
    public String guardarImagenEvento(@RequestParam("file") MultipartFile file, @RequestParam("id") int id, RedirectAttributes attr) {
        StringBuilder fileNames = new StringBuilder();
        String nombreArchivo= "foto-evento-" + id;
        uploadObject(file,nombreArchivo, "plenary-magpie-386203", "spring-bucket-jchavezs");
        return "redirect:/evento";

    }

    public static void uploadObject
            (MultipartFile multipartFile, String fileName, String projectId, String gcpBucketId) {
        try {
            byte[] fileData = FileUtils.readFileToByteArray(convertFile(multipartFile));
            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
            Bucket bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields());
//            RandomString id = new RandomString(6, ThreadLocalRandom.current());
            Blob blob = bucket.create("fotosEventos" + "/" + fileName + checkFileExtension(fileName), fileData);

            if (blob != null) {
//                LOGGER.debug("File successfully uploaded to GCS");
//                return new FileDto(blob.getName(), blob.getMediaLink());
            }
        } catch (Exception e) {
//            LOGGER.error("An error occurred while uploading data. Exception: ", e);
            throw new RuntimeException("An error occurred while storing data to GCS");
        }
    }

    private static File convertFile(MultipartFile file) {

        try {
            if (file.getOriginalFilename() == null) {
            }
            File convertedFile = new File(file.getOriginalFilename());
            FileOutputStream outputStream = new FileOutputStream(convertedFile);
            outputStream.write(file.getBytes());
            outputStream.close();
            return convertedFile;
        } catch (Exception e) {
            throw new RuntimeException("An error has occurred while converting the file");
        }
    }

    private static String checkFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            String[] extensionList = {".png", ".jpeg", ".pdf", ".doc", ".mp3"};

            for (String extension : extensionList) {
                if (fileName.endsWith(extension)) {
//                    LOGGER.debug("Accepted file type : {}", extension);
                    return extension;
                }
            }
        }
        return ".jpeg";
    }

    @PostMapping("/guardar")
    public String guardarEvento(Evento evento, RedirectAttributes attr) {
        eventoRepository.save(evento);
        System.out.println(evento.getId_evento());
        return "redirect:/evento";
    }

    @GetMapping("/editar")
    public String editarEvento(Model model, @RequestParam("id") int id) {

        Optional<Evento> optEvento = eventoRepository.findById(id);

        if (optEvento.isPresent()) {
            Evento evento = optEvento.get();
            model.addAttribute("listaLocales", localRepository.findAll());
            model.addAttribute("evento", evento);
            return "evento/editarEvento";
        } else {
            return "redirect:/evento";
        }
    }

    @GetMapping("/borrar")
    public String borrarTransportista(Model model, @RequestParam("id") int id,
                                      RedirectAttributes attr) {

        Optional<Evento> optEvento = eventoRepository.findById(id);
        if (optEvento.isPresent()) {
            eventoRepository.deleteById(id);
        }
        return "redirect:/evento";

    }
}
