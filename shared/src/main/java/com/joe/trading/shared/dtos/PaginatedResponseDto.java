package com.joe.trading.shared.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaginatedResponseDto<T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int currentPage;
}
