package com.joe.trading.shared.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderBookUpdateDto {
    private List<OrderBook> orderBooks;
    private String sourceExchange;
}
