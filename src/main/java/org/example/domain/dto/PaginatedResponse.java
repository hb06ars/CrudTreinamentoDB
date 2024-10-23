package org.example.domain.dto;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number;

    public PaginatedResponse(List<T> content, int totalPages, long totalElements, int number) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.number = number;
    }

    // Getters e Setters
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

