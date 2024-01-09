package com.example.bookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotNull(message = "cannot be null")
    private String title;
    @NotNull(message = "cannot be null")
    private String author;
    @NotNull(message = "cannot be null")
    private String isbn;
    @NotNull(message = "cannot be null")
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryIds;
}
