package ru.lauk.siteparser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.lauk.siteparser.service.ParseService;

import java.io.IOException;

@RequestMapping("main")
@RestController
@RequiredArgsConstructor
public class MainController {

    private final ParseService parseService;

    @RequestMapping("parse")
    public ResponseEntity<String> getPageCode(@RequestParam String uri) {
        try {
            return ResponseEntity.ok(parseService.simpleParse(uri));
        } catch (IOException e) {
            return ResponseEntity
                    .internalServerError()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

}
