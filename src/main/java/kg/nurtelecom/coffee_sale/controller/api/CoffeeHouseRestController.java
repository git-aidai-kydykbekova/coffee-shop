package kg.nurtelecom.coffee_sale.controller.api;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeHouseRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeHouseResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeHouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coffee-houses")
public class CoffeeHouseRestController {
    private static final Logger log = LoggerFactory.getLogger(CoffeeHouseRestController.class);

    private final CoffeeHouseService coffeeHouseService;

    public CoffeeHouseRestController(CoffeeHouseService coffeeHouseService) {
        this.coffeeHouseService = coffeeHouseService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<List<CoffeeHouseResponse>> getAllCoffeeHouses() {
        try {
            List<CoffeeHouseResponse> coffeeHouses = coffeeHouseService.findAll();

            if (coffeeHouses.isEmpty()) {
                log.info("No coffee houses found");
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(coffeeHouses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while fetching coffee houses", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<CoffeeHouseResponse> getCoffeeHouseById(@PathVariable @NotNull Integer id) {
        try {
            CoffeeHouseResponse coffeeHouse = coffeeHouseService.findById(id);
            return new ResponseEntity<>(coffeeHouse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while searching coffee house by id", e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> createCoffeeHouse(@Valid @RequestBody CoffeeHouseRequest coffeeHouseRequest) {
        try {
            coffeeHouseService.create(coffeeHouseRequest);
            log.info("Coffee house created successfully.");
            return new ResponseEntity<>("Coffee house was saved successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating coffee house", e);
            return new ResponseEntity<>("Failed to create coffee house: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> updateCoffeeHouse(
            @PathVariable @NotNull Integer id,
            @Valid @RequestBody CoffeeHouseRequest coffeeHouseRequest) {
        try {
            coffeeHouseService.update(id, coffeeHouseRequest);
            log.info("Coffee house was updated successfully");
            return new ResponseEntity<>("Coffee house was updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to update coffee house", e);
            return new ResponseEntity<>("Coffee house wasn't updated: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> deleteCoffeeHouse(@PathVariable @NotNull Integer id) {
        try {
            coffeeHouseService.delete(id);
            log.info("Coffee house with ID " + id + " was deleted");
            return new ResponseEntity<>("Coffee house was deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while deleting coffee house", e);
            return new ResponseEntity<>("Error deleting coffee house: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}