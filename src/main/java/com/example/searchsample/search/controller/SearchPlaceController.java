package com.example.searchsample.search.controller;

import com.example.searchsample.search.service.SearchPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/local/place")
@RequiredArgsConstructor
public class SearchPlaceController {


    private final SearchPlaceService searchPlaceService;

    @GetMapping("/keyword/{name}")
    public ResponseEntity<Object> getSearch(@PathVariable String name) {
        try {
            var response = searchPlaceService.search(name);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
