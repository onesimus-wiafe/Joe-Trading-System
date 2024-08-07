package com.joe.trading.order_processing.mappers;

import com.joe.trading.order_processing.entities.Portfolio;
import com.joe.trading.order_processing.entities.dto.PortfolioResponseDTO;
import com.joe.trading.shared.dtos.PortfolioEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PortfolioMapper {
    PortfolioMapper INSTANCE = Mappers.getMapper(PortfolioMapper.class);

    Portfolio toPortfolio(PortfolioEventDto portfolioEventDto);

    Portfolio toPortfolio(PortfolioResponseDTO portfolioResponseDTO);

    PortfolioResponseDTO toPortfolioResponseDTO(Portfolio portfolio);

    List<PortfolioResponseDTO> toPortfolioResponseDTOs(List<Portfolio> portfolios);

    PortfolioEventDto toPortfolioEventDto(Portfolio portfolio);
}
