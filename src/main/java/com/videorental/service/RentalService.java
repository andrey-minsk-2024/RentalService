package com.videorental.service;

import com.videorental.controller.exceptions.NotFoundException;
import com.videorental.model.Film;
import com.videorental.model.FilmType;
import com.videorental.model.RequestFilm;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.tuple.Pair;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Data
@Service
public class RentalService {

    @Value("${price.premium}")
    private Integer pricePremium;

    @Value("${price.basic}")
    private Integer priceBasic;

    private Map<Integer, Film> storageFilms = new HashMap<>();
    private List<Film> films = new ArrayList<>();
    private Map<Integer, Integer> quantityMap = new ConcurrentHashMap<>(256);

    public List<Film> getPaginatedProducts(int page, int size) {

        List<Film> resultList = new ArrayList<>();

        for (int i = page * size; i < (page + 1) * size && i < films.size(); i++) {

            resultList.add(films.get(i));
        }
        System.out.println(resultList);

        return resultList;
    }

    public Film getProduct(int id) {
        return Optional.ofNullable(storageFilms.get(id))
                .orElseThrow(() -> new NotFoundException("Film not found for id: " + id));
    }

    public void returnFilms(RequestFilm requestFilm) {
        requestFilm.getFilmIdDurations()
                .forEach(filmIdDuration -> returnFilm(filmIdDuration.getLeft()));
    }

    public void returnFilm(int filmId) {
        quantityMap.merge(filmId, 1, Integer::sum);
    }

    public void removeFilm(int removedFilmId) {
        storageFilms.remove(removedFilmId);
        films = new ArrayList<>(storageFilms.values());
    }

    public List<Pair<Film, Integer>> distributeFilms(RequestFilm requestFilm) {

        return requestFilm.getFilmIdDurations().stream()
                .filter(filmDuration -> distributeFilm(filmDuration.getLeft()) > -1)
                .map(filmDuration -> {
                    var filmId = filmDuration.getLeft();
                    var film = storageFilms.get(filmDuration.getLeft());

                    return Pair.of(film, calculatePrice(filmDuration.getRight(), film.getFilmType()));
                })
                .toList();
    }

    private Integer calculatePrice(int duration, FilmType filmType) {
        return switch (filmType) {
            case NEW_RELEASES -> calculator(duration, pricePremium, 0);
            case REGULAR_FILM -> calculator(duration, priceBasic, 3);
            case OLD_FILM -> calculator(duration, priceBasic, 5);
        };
    }

    private Integer calculator(int duration, int price, int initialPeriod) {
        var fee = price;
        duration -= initialPeriod;

        while (duration > 0) {
            fee += price;
        }
        return fee;
    }

    private int distributeFilm(int filmId) {

        return quantityMap.compute(filmId, (k, v) -> {
            if (v == null) {
                return 0;
            }
            return v > -1 ? v - 1 : -1;
        });
    }

}
