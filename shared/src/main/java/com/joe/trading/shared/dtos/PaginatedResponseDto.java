package com.joe.trading.shared.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
