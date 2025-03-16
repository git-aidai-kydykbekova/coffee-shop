package kg.nurtelecom.coffee_sale.payload.respone;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoffeeResponse(
        @JsonProperty("name")
        String cofName,

        @JsonProperty("supplier_id")
        Integer supId,

        @JsonProperty("price")
        Float price,

        @JsonProperty("sales")
        Integer sales,

        @JsonProperty("total")
        Integer total,

        @JsonProperty("supplier_name")
        String supplierName
) {
}