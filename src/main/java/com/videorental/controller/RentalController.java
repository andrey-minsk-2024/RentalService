package com.videorental.controller;

import com.videorental.model.Film;
import com.videorental.model.RequestFilm;
import com.videorental.service.IdempotenceChecker;
import com.videorental.service.RentalService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrey.semenov
 */
@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final IdempotenceChecker idempotenceChecker = new IdempotenceChecker(1000);

    @GetMapping("/products")
    public ResponseEntity<List<Film>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(rentalService.getPaginatedProducts(page, size), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Film> getProduct(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(rentalService.getProduct(id), HttpStatus.OK);
    }

    @PostMapping("/distribute")
    public ResponseEntity<String> distributeFilms(@RequestBody RequestFilm requestFilm) {

        idempotenceChecker.check(requestFilm.getTraceId());
        rentalService.distributeFilms(requestFilm);

        return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnFilm(@RequestBody RequestFilm requestFilm) {

        idempotenceChecker.check(requestFilm.getTraceId());
        rentalService.returnFilms(requestFilm);

        return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
    }
}