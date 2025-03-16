package kg.nurtelecom.coffee_sale.payload.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MerchandiseInventoryRequest(
        @NotBlank(message = "Item ID is mandatory")
        String itemId,

        @NotBlank(message = "Item name is mandatory")
        String itemName,

        @NotNull(message = "Supplier ID is mandatory")
        Integer supId,

        Integer quantity,

        @NotNull(message = "Date is mandatory")
        LocalDate date
) {
}