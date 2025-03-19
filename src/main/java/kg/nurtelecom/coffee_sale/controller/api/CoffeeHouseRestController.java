package kg.nurtelecom.coffee_sale.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Coffee House API", description = "Управление кофейнями")
public class CoffeeHouseRestController {
    private static final Logger log = LoggerFactory.getLogger(CoffeeHouseRestController.class);

    private final CoffeeHouseService coffeeHouseService;

    public CoffeeHouseRestController(CoffeeHouseService coffeeHouseService) {
        this.coffeeHouseService = coffeeHouseService;
    }

    @Operation(summary = "Получить список всех кофеен", description = "Возвращает список всех кофеен, доступных в системе.")
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

    @Operation(summary = "Получить кофейню по ID", description = "Возвращает данные о кофейне по указанному ID.")
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

    @Operation(summary = "Создать новую кофейню", description = "Добавляет новую кофейню в систему.")
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

    @Operation(summary = "Обновить кофейню", description = "Обновляет информацию о кофейне по указанному ID.")
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

    @Operation(summary = "Удалить кофейню", description = "Удаляет кофейню по указанному ID.")
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
