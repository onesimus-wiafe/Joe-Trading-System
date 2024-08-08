package com.joe.trading.order_processing.mappers;

import com.joe.trading.order_processing.entities.OrderBook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderBookMapper {

    OrderBookMapper INSTANCE = Mappers.getMapper(OrderBookMapper.class);

    OrderBook mapToOrderBook(com.joe.trading.shared.dtos.OrderBook orderBook);
}
