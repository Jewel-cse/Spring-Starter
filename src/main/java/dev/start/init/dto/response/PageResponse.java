package dev.start.init.dto.response;


import lombok.Data;
import org.springframework.data.domain.Page;
@Data
public class PageResponse <T>{
    T data;
    int pageSize;
    long totalElements;

    public PageResponse(Page<T> pages){
        this.pageSize = pages.getTotalPages();
        this.totalElements = pages.getTotalElements();
        data = (T) pages.stream().toList();
    }

}

