package com.videorental.controller;

import com.videorental.model.Film;
import com.videorental.model.RequestFilm;
import com.videorental.model.RequestReturnFilm;
import com.videorental.service.IdempotenceChecker;
import com.videorental.service.RentalService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${idempotence.queue_size}")
    private Integer idempotenceQueueSize;

    private final RentalService rentalService;
    private IdempotenceChecker idempotenceChecker;

    @PostConstruct
    public void init() {
        idempotenceChecker = new IdempotenceChecker(idempotenceQueueSize);
    }

    @GetMapping("/films")
    public ResponseEntity<List<Film>> getFilms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(rentalService.getPaginatedFilms(page, size), HttpStatus.OK);
    }

    @GetMapping("/films/{id}")
    public ResponseEntity<Film> getFilms(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(rentalService.getFilmInfo(id), HttpStatus.OK);
    }

    @PostMapping("/distribute")
    public ResponseEntity<String> distributeFilms(@RequestBody RequestFilm requestFilm) {

        idempotenceChecker.check(requestFilm.getTraceId());
        rentalService.distributeFilms(requestFilm);

        return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnFilm(@RequestBody RequestReturnFilm requestFilm) {

        idempotenceChecker.check(requestFilm.getTraceId());
        rentalService.returnFilms(requestFilm);

        return new ResponseEntity<>("Product added successfully", HttpStatus.OK);
    }
}
