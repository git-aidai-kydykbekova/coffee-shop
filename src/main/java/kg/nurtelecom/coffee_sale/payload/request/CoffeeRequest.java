package kg.nurtelecom.coffee_sale.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CoffeeRequest(
        @NotBlank(message = "Coffee name is mandatory")
        String cofName,

        @NotNull(message = "Supplier ID is mandatory")
        Integer supId,

        @NotNull(message = "Price is mandatory")
        @Positive(message = "Price must be positive")
        Float price,

        Integer sales,

        Integer total
) {
}