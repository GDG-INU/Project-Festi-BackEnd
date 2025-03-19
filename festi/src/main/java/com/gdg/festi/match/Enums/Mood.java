package com.gdg.festi.match.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Mood {
    LONG("밤새도록"),
    SHORT("짧고굵게"),
    TALKING("시끌벅적"),
    DORANDORAN("도란도란");

    private final String value;

    Mood(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Mood fromValue(String value) {
        for (Mood mood : Mood.values()) {
            if (mood.value.equals(value)) {
                return mood;
            }
        }
        throw new IllegalArgumentException("정의되지 않은 값 입니다 : " + value);
    }
}
