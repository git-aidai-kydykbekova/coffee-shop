package kg.nurtelecom.coffee_sale.payload.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CoffeeHouseRequest(
        @NotNull(message = "Store ID is mandatory")
        Integer storeId,

        @NotBlank(message = "City is mandatory")
        String city,

        Integer coffee,

        Integer merch,

        Integer total
) {
}