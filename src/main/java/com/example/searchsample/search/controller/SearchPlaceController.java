package com.example.searchsample.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchPlaceController {


    @GetMapping("/get")
    public Object getSearch(){

        return "";
    }
}