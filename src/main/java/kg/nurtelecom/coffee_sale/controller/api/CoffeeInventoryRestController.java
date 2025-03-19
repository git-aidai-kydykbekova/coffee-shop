package kg.nurtelecom.coffee_sale.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeInventoryResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Coffee Inventory", description = "Управление запасами кофе")
public class CoffeeInventoryRestController {
    private static final Logger log = LoggerFactory.getLogger(CoffeeInventoryRestController.class);
    private final CoffeeInventoryService coffeeInventoryService;

    public CoffeeInventoryRestController(CoffeeInventoryService coffeeInventoryService) {
        this.coffeeInventoryService = coffeeInventoryService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить все запасы", description = "Возвращает список всех запасов кофе")
    public ResponseEntity<List<CoffeeInventoryResponse>> getAllInventory() {
        try {
            List<CoffeeInventoryResponse> inventoryList = coffeeInventoryService.findAll();

            if (inventoryList.isEmpty()) {
                log.info("No inventory items found");
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(inventoryList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while fetching inventory", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить запасы по ID", description = "Возвращает информацию о запасах кофе по заданному ID")
    public ResponseEntity<CoffeeInventoryResponse> getInventoryById(@PathVariable Long id) {
        try {
            CoffeeInventoryResponse inventory = coffeeInventoryService.findById(id);
            return new ResponseEntity<>(inventory, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while searching inventory by id", e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Создать запись о запасах", description = "Добавляет новую запись о запасах кофе")
    public ResponseEntity<String> createInventory(@Valid @RequestBody CoffeeInventoryRequest request) {
        try {
            coffeeInventoryService.create(request);
            log.info("Inventory record created successfully.");
            return new ResponseEntity<>("Inventory record was created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating inventory record", e);
            return new ResponseEntity<>("Failed to create inventory record: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Обновить запись о запасах", description = "Обновляет существующую запись о запасах кофе по ID")
    public ResponseEntity<String> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody CoffeeInventoryRequest request) {
        try {
            coffeeInventoryService.update(id, request);
            log.info("Inventory record was updated successfully");
            return new ResponseEntity<>("Inventory record was updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to update inventory record", e);
            return new ResponseEntity<>("Inventory record wasn't updated: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Удалить запись о запасах", description = "Удаляет запись о запасах кофе по ID")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        try {
            coffeeInventoryService.delete(id);
            log.info("Inventory record with ID " + id + " was deleted");
            return new ResponseEntity<>("Inventory record was deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while deleting inventory record", e);
            return new ResponseEntity<>("Error deleting inventory record: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
