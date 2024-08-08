package com.trading.joe.reportservice.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("orderbooks")
public class OrderBooks {

    private String orderID;
    private Double price;
    private String product;
    private Integer quantity;
    private Integer cumulatitiveQuantity;
    private Double cumulatitivePrice;
    private String side;
    private String orderType;
}
