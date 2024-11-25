package com.videorental.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author andrey.semenov
 */
@Data
@Accessors(chain = true)
public class Film {
    private Integer id;
    private FilmType filmType;
    private String name;
}
