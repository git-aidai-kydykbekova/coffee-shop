package kg.nurtelecom.coffee_sale.controller.api;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kg.nurtelecom.coffee_sale.payload.request.SupplierRequest;
import kg.nurtelecom.coffee_sale.payload.respone.SupplierResponse;
import kg.nurtelecom.coffee_sale.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierRestController {
    private static final Logger log = LoggerFactory.getLogger(SupplierRestController.class);

    private final SupplierService supplierService;

    public SupplierRestController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        try {
            List<SupplierResponse> suppliers = supplierService.findAll();

            if (suppliers.isEmpty()) {
                log.info("No suppliers found");
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(suppliers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while fetching suppliers", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable @NotNull Integer id) {
        try {
            SupplierResponse supplier = supplierService.findById(id);
            return new ResponseEntity<>(supplier, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while searching supplier by id", e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> createSupplier(@Valid @RequestBody SupplierRequest supplierRequest) {
        try {
            supplierService.create(supplierRequest);
            log.info("Supplier created successfully");
            return new ResponseEntity<>("Supplier was created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating supplier", e);
            return new ResponseEntity<>("Failed to create supplier: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> updateSupplier(
            @PathVariable @NotNull Integer id,
            @Valid @RequestBody SupplierRequest supplierRequest) {
        try {
            supplierService.update(id, supplierRequest);
            log.info("Supplier was updated successfully");
            return new ResponseEntity<>("Supplier was updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to update supplier", e);
            return new ResponseEntity<>("Supplier wasn't updated: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> deleteSupplier(@PathVariable @NotNull Integer id) {
        try {
            supplierService.delete(id);
            log.info("Supplier with ID " + id + " was deleted");
            return new ResponseEntity<>("Supplier was deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while deleting supplier", e);
            return new ResponseEntity<>("Error deleting supplier: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
