package kg.nurtelecom.coffee_sale.controller.api;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/coffees")
public class CoffeeRestController {
    private static final Logger log = LoggerFactory.getLogger(CoffeeRestController.class);

    private final CoffeeService coffeeService;

    public CoffeeRestController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<List<CoffeeResponse>> getAllCoffees() {
        try {
            List<CoffeeResponse> coffees = coffeeService.findAll();

            if (coffees.isEmpty()) {
                log.info("No coffees found");
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(coffees, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while fetching coffees", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{coffeeName}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<CoffeeResponse> getCoffeeByName(
            @PathVariable @Valid @NotBlank @Size(min = 3, max = 50) String coffeeName) {
        try {
            CoffeeResponse coffee = coffeeService.findById(coffeeName);
            return new ResponseEntity<>(coffee, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while searching coffee by id", e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> createCoffee(@Valid @RequestBody CoffeeRequest coffeeRequest) {
        try {
            coffeeService.create(coffeeRequest);
            log.info("Coffee created successfully.");
            return new ResponseEntity<>("Coffee was saved successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating coffee", e);
            return new ResponseEntity<>("Failed to create coffee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{coffeeName}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> updateCoffee(
            @PathVariable @Valid @NotBlank @Size(min = 3, max = 50) String coffeeName,
            @Valid @RequestBody CoffeeRequest coffeeRequest) {
        try {
            coffeeService.update(coffeeName, coffeeRequest);
            log.info("Coffee was updated successfully");
            return new ResponseEntity<>("Coffee was updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to update coffee", e);
            return new ResponseEntity<>("Coffee wasn't updated: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{coffeeName}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> deleteCoffee(
            @PathVariable @Valid @NotBlank @Size(min = 3, max = 50) String coffeeName) {
        try {
            coffeeService.delete(coffeeName);
            log.info("Coffee with name " + coffeeName + " was deleted");
            return new ResponseEntity<>("Coffee was deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while deleting coffee", e);
            return new ResponseEntity<>("Error deleting coffee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}