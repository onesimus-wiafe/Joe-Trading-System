package com.joe.trading.shared.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Event {
    USER_CREATED("user.created"),
    USER_UPDATED("user.updated"),
    USER_DELETED("user.deleted");New_OrdrNEW

    private final String value;
}
