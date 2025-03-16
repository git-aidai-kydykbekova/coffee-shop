package kg.nurtelecom.coffee_sale.payload.respone;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoffeeHouseResponse(
        @JsonProperty("store_id")
        Integer storeId,

        @JsonProperty("city")
        String city,

        @JsonProperty("coffee_sales")
        Integer coffee,

        @JsonProperty("merchandise_sales")
        Integer merch,

        @JsonProperty("total_sales")
        Integer total
) {
}

