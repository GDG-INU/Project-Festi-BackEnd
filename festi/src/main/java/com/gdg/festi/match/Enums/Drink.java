package com.gdg.festi.match.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Drink {
    ONE("1병"),
    ONE_AND_HALF("1병 반"),
    TWO("2병"),
    TWO_AND_HALF("2병 반"),
    THREE("3병"),
    THREE_AND_HALF("3병 반");

    private final String value;

    Drink(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Drink fromValue(String value) {
        for (Drink drink : Drink.values()) {
            if (drink.value.equals(value)) {
                return drink;
            }
        }
        throw new IllegalArgumentException("정의되지 않은 값 입니다 : " + value);
    }
}
