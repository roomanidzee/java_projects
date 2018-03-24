package ru.itis.romanov_andrey.perpenanto.utils;

import com.itextpdf.text.pdf.BaseFont;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import ru.itis.romanov_andrey.perpenanto.models.usermodels.FileInfo;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.Product;
import ru.itis.romanov_andrey.perpenanto.repositories.ProfileRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * 01.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class FileStorageUtil {

    @Autowired
    private ProfileRepository profileRepository;

    @Value("${storage.path}")
    private String storagePath;

    public String getStoragePath() {
        return storagePath;
    }

    private String getURLOfFile(String storageFileName){

        StringBuilder sb = new StringBuilder();
        sb.append(this.storagePath).append("\\").append(storageFileName);
        return sb.toString();

    }

    private String createFileStorageName(String originalFileName){

        String extension = FilenameUtils.getExtension(originalFileName);
        String newFileName = UUID.randomUUID().toString();

        StringBuilder sb = new StringBuilder();
        sb.append(newFileName).append(".").append(extension);
        return sb.toString();

    }

    @SneakyThrows
    public void copyToStorage(MultipartFile file, String storageFileName){
        Files.copy(file.getInputStream(), Paths.get(this.storagePath, storageFileName));
    }

    public FileInfo convertFromMultipart(MultipartFile file){

        String originalFileName = file.getOriginalFilename();
        String type = file.getContentType();
        long size = file.getSize();
        String storageFileName = this.createFileStorageName(originalFileName);
        String fileURL = this.getURLOfFile(storageFileName);

        return FileInfo.builder()
                       .originalFileName(originalFileName)
                       .storageFileName(storageFileName)
                       .url(fileURL)
                       .size(size)
                       .type(type)
                       .build();

    }

    @SneakyThrows
    public FileInfo convertHTMLToPDF(Long id, List<Product> products, Integer price, Timestamp timestamp){

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("products", products);
        context.setVariable("price", price);
        context.setVariable("timestamp", timestamp);

        String html = templateEngine.process("email/order_quittance", context);

        StringBuilder sb = new StringBuilder();
        sb.append("reservation#").append(id).append(".pdf");
        String filename = this.createFileStorageName(sb.toString());
        String fileURL = this.getURLOfFile(filename);

        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();

        ClassPathResource timesNewRoman = new ClassPathResource(
                                           "/static/fonts/TimesNewRomanRegular/TimesNewRomanRegular.ttf"
                                            );
        fontResolver.addFont(timesNewRoman.getURL().toString(), BaseFont.IDENTITY_H, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(bos);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Path filePath = Paths.get(fileURL);
        Files.copy(bis, filePath);

        long size = Files.size(filePath);
        String type = "application/pdf";

        return FileInfo.builder()
                       .originalFileName(sb.toString())
                       .storageFileName(filename)
                       .url(fileURL)
                       .size(size)
                       .type(type)
                       .build();
    }

}
