package kg.nurtelecom.coffee_sale.payload.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record CoffeeInventoryResponse(
        @JsonProperty("id")
        Long id,

        @JsonProperty("warehouse_id")
        Integer warehouseId,

        @JsonProperty("coffee_name")
        String cofName,

        @JsonProperty("supplier_id")
        Integer supId,

        @JsonProperty("quantity")
        Integer quantity,

        @JsonProperty("date")
        LocalDate date,

        @JsonProperty("coffee_price")
        Float coffeePrice,

        @JsonProperty("supplier_name")
        String supplierName
) {
}