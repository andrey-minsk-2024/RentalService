package com.videorental.model;

/**
 * @author andrey.semenov
 */
public enum FilmType {

    NEW_RELEASES("new releases"),
    REGULAR_FILM("regular film"),
    OLD_FILM("old film");

    public final String type;

    private FilmType(String type) {
        this.type = type;
    }
}
