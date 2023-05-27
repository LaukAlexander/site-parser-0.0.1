package ru.lauk.siteparser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.lauk.siteparser.service.ParseService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

@RequestMapping("main")
@RestController
@RequiredArgsConstructor
public class MainController {

    private final ParseService parseService;

    @GetMapping(value = "parse/simple", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getPageCode(@RequestParam String uri) {
        try {
            return ResponseEntity.ok(parseService.simpleParse(uri));
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping(value = "parse/experiment")
    public ResponseEntity<String> parseExperiment(@RequestParam String uri) {
        try {
            return ResponseEntity.ok(parseService.parseExperiment(uri));
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }



}
