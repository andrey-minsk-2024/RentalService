package com.videorental.model;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * @author andrey.semenov
 */
@Data
@Accessors(chain = true)
public class RequestFilm {
    private Integer traceId;
    private List<FilmIdDuration> filmIdDurations;
}
