package com.joe.trading.shared.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Event {
    // USER SERVICE
    USER_CREATED("user.created"),
    USER_UPDATED("user.updated"),
    USER_DELETED("user.deleted"),

    DELETE_PORTFOLIO_REQUEST("portfolio.delete.request"),

    // MARKET DATA SERVICE & ORDER PROCESSING SERVICE
    NEW_ORDER_EX1("order.exchange1"),
    NEW_ORDER_EX2("order.exchange2"),

    PORTFOLIO_CREATED("order.portfolio.created"),

    MARKET_DATA_UPDATE("market.data.update"),

    MSFT_ORDER_BOOK("orderbook.msft"),
    NFLX_ORDER_BOOK("orderbook.nflx"),
    GOOGL_ORDER_BOOK("orderbook.googl"),
    AAPL_ORDER_BOOK("orderbook.aapl"),
    TSLA_ORDER_BOOK("orderbook.tsla"),
    IBM_ORDER_BOOK("orderbook.ibm"),
    ORCL_ORDER_BOOK("orderbook.orcl"),
    AMZN_ORDER_BOOK("orderbook.amzn");

    private final String value;
}
