package com.joe.trading.marketdataservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class MarketData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private Double LAST_TRADED_PRICE;
    @JsonProperty
    private String TICKER;
    @JsonProperty
    private Integer SELL_LIMIT;
    @JsonProperty
    private Double BID_PRICE;
    @JsonProperty
    private Integer BUY_LIMIT;
    @JsonProperty
    private Double ASK_PRICE;
    @JsonProperty
    private Double MAX_PRICE_SHIFT;

    private String EXCHANGE;

    @Override
    public String toString() {
        return "MarketData{" +
                "LAST_TRADED_PRICE=" + LAST_TRADED_PRICE +
                ", TICKER='" + TICKER + '\'' +
                ", SELL_LIMIT=" + SELL_LIMIT +
                ", BID_PRICE=" + BID_PRICE +
                ", BUY_LIMIT=" + BUY_LIMIT +
                ", ASK_PRICE=" + ASK_PRICE +
                ", MAX_PRICE_SHIFT=" + MAX_PRICE_SHIFT +
                ", EXCHANGE='" + EXCHANGE + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof MarketData that)) return false;
        return Objects.equals(LAST_TRADED_PRICE, that.LAST_TRADED_PRICE) && Objects.equals(TICKER, that.TICKER) && Objects.equals(SELL_LIMIT, that.SELL_LIMIT) && Objects.equals(BID_PRICE, that.BID_PRICE) && Objects.equals(BUY_LIMIT, that.BUY_LIMIT) && Objects.equals(ASK_PRICE, that.ASK_PRICE) && Objects.equals(MAX_PRICE_SHIFT, that.MAX_PRICE_SHIFT) && Objects.equals(EXCHANGE, that.EXCHANGE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(LAST_TRADED_PRICE, TICKER, SELL_LIMIT, BID_PRICE, BUY_LIMIT, ASK_PRICE, MAX_PRICE_SHIFT, EXCHANGE);
    }
}
