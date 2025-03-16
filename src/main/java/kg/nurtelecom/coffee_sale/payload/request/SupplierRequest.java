package kg.nurtelecom.coffee_sale.payload.request;

import jakarta.validation.constraints.NotBlank;


public record SupplierRequest(
        Integer supId,

        @NotBlank(message = "Supplier name is mandatory")
        String supName,

        @NotBlank(message = "Street is mandatory")
        String street,

        @NotBlank(message = "City is mandatory")
        String city,

        @NotBlank(message = "State is mandatory")
        String state,

        @NotBlank(message = "ZIP is mandatory")
        String zip
) {
}