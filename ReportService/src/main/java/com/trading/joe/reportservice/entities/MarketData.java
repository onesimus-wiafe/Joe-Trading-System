package com.trading.joe.reportservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("marketdata")
public class MarketData {

    private String id;
    private Double LAST_TRADED_PRICE;
    private String TICKER;
    private Integer SELL_LIMIT;
    private Double BID_PRICE;
    private Integer BUY_LIMIT;
    private Double ASK_PRICE;
    private Double MAX_PRICE_SHIFT;

    private String EXCHANGE;
}
