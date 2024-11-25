package com.videorental.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author andrey.semenov
 */
@Data
@Accessors(chain = true)
public class FilmIdDuration {
    private int filmId;
    private int duration;
}
