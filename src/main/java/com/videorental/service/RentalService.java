package com.videorental.service;

import com.videorental.controller.exceptions.NotFoundException;
import com.videorental.model.Film;
import com.videorental.model.FilmIdDuration;
import com.videorental.model.FilmIdReleaseId;
import com.videorental.model.FilmType;
import com.videorental.model.Order;
import com.videorental.model.RequestFilm;
import com.videorental.model.RequestReturnFilm;
import lombok.Data;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

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
    private Map<Integer, Map<Integer, Instant>> filmCassetteIds = new HashMap<>();
    private List<Film> films = new ArrayList<>();
    private Map<Integer, Map<Integer, Pair<Integer, Instant>>> releasedFilms = new ConcurrentHashMap<>(256);
    private Map<Integer, Integer> quantityMap = new ConcurrentHashMap<>(256);


    public List<Film> getPaginatedFilms(int page, int size) {

        List<Film> resultList = new ArrayList<>();
        for (int i = page * size; i < (page + 1) * size && i < films.size(); i++) {
            resultList.add(films.get(i));
        }

        return resultList;
    }

    public Film getFilmInfo(int id) {
        return Optional.ofNullable(storageFilms.get(id))
                .orElseThrow(() -> new NotFoundException("Film not found for id: " + id));
    }

    public void removeFilm(int removedFilmId) {
        storageFilms.remove(removedFilmId);
        films = new ArrayList<>(storageFilms.values());
    }

    public List<Order> returnFilms(RequestReturnFilm requestFilm) {
        return requestFilm.getFilmIdReleaseIds().stream()
                .peek(filmIdReleaseId -> increaseFilmsAvailable(filmIdReleaseId.getFilmId()))
                .map(this::createReturnOrder)
                .toList();
    }

    private int calculateDuration(Instant startDate) {
        return (int) ChronoUnit.DAYS.between(startDate, Instant.now());
    }

    public List<Order> distributeFilms(RequestFilm requestFilm) {
        return requestFilm.getFilmIdDurations().stream()
                .filter(filmDuration -> decreaseFilmsAvailable(filmDuration.getFilmId()) > -1)
                .map(this::createReleaseOrder)
                .toList();
    }

    private Order createReturnOrder(FilmIdReleaseId filmIdReleaseId) {
        var filmId = filmIdReleaseId.getFilmId();
        var film = storageFilms.get(filmId);

        var dates = releasedFilms.get(filmId).get(filmIdReleaseId.getReleaseId());

        var duration = calculateDuration(dates.getRight()) - dates.getLeft();
        return new Order()
                .setFilm(film)
                .setCost(calculatePriceReturn(duration, film.getFilmType()));
    }

    private Order createReleaseOrder(FilmIdDuration filmDuration) {
        var filmId = filmDuration.getFilmId();
        var film = storageFilms.get(filmId);

        final int releaseId = ThreadLocalRandom.current().nextInt();
        releasedFilms
                .get(filmId)
                .put(ThreadLocalRandom.current().nextInt(),
                        Pair.of(filmDuration.getDuration(), Instant.now()));

        return new Order()
                .setReleaseId(releaseId)
                .setCost(calculatePriceRelease(
                        filmDuration.getDuration(),
                        film.getFilmType()))
                .setFilm(film);
    }

    private Integer calculatePriceRelease(int duration, FilmType filmType) {
        return switch (filmType) {
            case NEW_RELEASES -> calculator(duration, pricePremium, 0);
            case REGULAR_FILM -> calculator(duration, priceBasic, 3);
            case OLD_FILM -> calculator(duration, priceBasic, 5);
        };
    }

    private Integer calculatePriceReturn(int duration, FilmType filmType) {
        return switch (filmType) {
            case NEW_RELEASES -> calculator(duration, pricePremium, 0);
            case REGULAR_FILM -> calculator(duration, priceBasic, 0);
            case OLD_FILM -> calculator(duration, priceBasic, 0);
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

    private int decreaseFilmsAvailable(int filmId) {

        return quantityMap.compute(filmId, (k, v) -> {
            if (v == null) {
                return 0;
            }
            return v > -1 ? v - 1 : -1;
        });
    }

    private void increaseFilmsAvailable(int filmId) {
        quantityMap.merge(filmId, 1, Integer::sum);
    }

}
