package kg.nurtelecom.coffee_sale.payload.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SupplierResponse(
        @JsonProperty("supplier_id")
        Integer supId,

        @JsonProperty("name")
        String supName,

        @JsonProperty("street")
        String street,

        @JsonProperty("city")
        String city,

        @JsonProperty("state")
        String state,

        @JsonProperty("zip")
        String zip,

        @JsonProperty("coffees")
        List<String> coffeeNames
) {
}