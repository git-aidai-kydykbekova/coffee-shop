package kg.nurtelecom.coffee_sale.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CoffeeInventoryRequest(
        @NotNull(message = "Warehouse ID is mandatory")
        Integer warehouseId,

        @NotBlank(message = "Coffee name is mandatory")
        String cofName,

        @NotNull(message = "Supplier ID is mandatory")
        Integer supId,

        Integer quantity,

        @NotNull(message = "Date is mandatory")
        LocalDate date
) {
}