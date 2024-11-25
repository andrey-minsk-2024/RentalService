package com.videorental.initializer;

import static com.videorental.model.FilmType.NEW_RELEASES;
import static com.videorental.model.FilmType.OLD_FILM;
import static com.videorental.model.FilmType.REGULAR_FILM;

import com.videorental.model.Film;
import com.videorental.service.RentalService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
@RequiredArgsConstructor
public class Initializer {

    private final RentalService rentalService;

    @PostConstruct
    public void initRepo() {
        rentalService.setQuantityMap(getQuantityMap());
        rentalService.setStorageFilms(getStorageFilms());
        rentalService.removeFilm(0);
    }

    public Map<Integer, Integer> getQuantityMap() {
        return new HashMap<>(Map.of(123, 5, 321, 7, 777, 2));
    }

    public Map<Integer, Film> getStorageFilms() {
        return new HashMap<>(Map.of(123, new Film()
                        .setId(123)
                        .setName("Dune")
                        .setFilmType(NEW_RELEASES),
                321, new Film()
                        .setId(321)
                        .setName("Oblivion")
                        .setFilmType(REGULAR_FILM),
                777, new Film()
                        .setId(777)
                        .setName("Terminator")
                        .setFilmType(OLD_FILM)));
    }
}
