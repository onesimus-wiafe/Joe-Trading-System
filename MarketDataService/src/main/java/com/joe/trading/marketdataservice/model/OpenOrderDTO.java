package com.joe.trading.marketdataservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OpenOrderDTO {
    private String orderType;
    private String product;
    private String side;
    private String orderID;
    private Double price;
    private Integer qty;
    private Integer cumQty;
    private Double cumPrx;
    private String exchange;

    @Override
    public String toString() {
        return "OpenOrderDTO{" +
                "orderType='" + orderType + '\'' +
                ", product='" + product + '\'' +
                ", side='" + side + '\'' +
                ", orderID='" + orderID + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", cumQty=" + cumQty +
                ", cumPrx=" + cumPrx +
                '}';
    }
}
