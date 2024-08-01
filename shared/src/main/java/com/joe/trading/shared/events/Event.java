package com.joe.trading.shared.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Event {
    USER_CREATED("user.created"),
    USER_UPDATED("user.updated"),
    USER_DELETED("user.deleted"),

    NEW_ORDER_EX1("order.exchange1"),
    NEW_ORDER_EX2("order.exchange2"),

    MARKET_DATA_UPDATE("market.data.update"),

    DELETE_PORTFOLIO_REQUEST("portfolio.delete.request"),
    FULL_ORDER_BOOK("orderbook.complete");

    private final String value;
}
