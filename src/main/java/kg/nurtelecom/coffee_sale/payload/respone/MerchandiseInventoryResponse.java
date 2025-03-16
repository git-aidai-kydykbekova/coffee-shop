package kg.nurtelecom.coffee_sale.payload.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record MerchandiseInventoryResponse(
        @JsonProperty("item_id")
        String itemId,

        @JsonProperty("item_name")
        String itemName,

        @JsonProperty("supplier_id")
        Integer supId,

        @JsonProperty("quantity")
        Integer quantity,

        @JsonProperty("date")
        LocalDate date,

        @JsonProperty("supplier_name")
        String supplierName
) {
}