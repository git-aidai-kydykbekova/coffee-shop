package kg.nurtelecom.coffee_sale.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kg.nurtelecom.coffee_sale.payload.request.MerchandiseInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.MerchandiseInventoryResponse;
import kg.nurtelecom.coffee_sale.service.MerchandiseInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchandise-inventory")
@Tag(name = "Merchandise Inventory Management", description = "Операции по управлению инвентарем товаров")
public class MerchandiseInventoryRestController {
    private static final Logger log = LoggerFactory.getLogger(MerchandiseInventoryRestController.class);

    private final MerchandiseInventoryService merchandiseInventoryService;

    public MerchandiseInventoryRestController(MerchandiseInventoryService merchandiseInventoryService) {
        this.merchandiseInventoryService = merchandiseInventoryService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить все товары инвентаря", description = "Возвращает список всех доступных товаров в инвентаре")
    public ResponseEntity<List<MerchandiseInventoryResponse>> getAllInventoryItems() {
        try {
            List<MerchandiseInventoryResponse> inventoryItems = merchandiseInventoryService.findAll();

            if (inventoryItems.isEmpty()) {
                log.info("No inventory items found");
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(inventoryItems, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while fetching inventory items", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить товар по ID", description = "Возвращает информацию о товаре в инвентаре по его ID")
    public ResponseEntity<MerchandiseInventoryResponse> getInventoryItemById(
            @PathVariable @Valid @NotBlank String itemId) {
        try {
            MerchandiseInventoryResponse inventoryItem = merchandiseInventoryService.findById(itemId);
            return new ResponseEntity<>(inventoryItem, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while searching inventory item by id", e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/supplier/{supId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Получить товары по поставщику", description = "Возвращает список товаров инвентаря по ID поставщика")
    public ResponseEntity<List<MerchandiseInventoryResponse>> getInventoryItemsBySupplier(
            @PathVariable Integer supId) {
        try {
            List<MerchandiseInventoryResponse> inventoryItems = merchandiseInventoryService.findBySupplier(supId);

            if (inventoryItems.isEmpty()) {
                log.info("No inventory items found for supplier with ID " + supId);
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(inventoryItems, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while fetching inventory items by supplier", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Добавить новый товар в инвентарь", description = "Создает новый товар в инвентаре")
    public ResponseEntity<String> createInventoryItem(@Valid @RequestBody MerchandiseInventoryRequest inventoryRequest) {
        try {
            merchandiseInventoryService.create(inventoryRequest);
            log.info("Inventory item created successfully");
            return new ResponseEntity<>("Inventory item was created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating inventory item", e);
            return new ResponseEntity<>("Failed to create inventory item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Обновить информацию о товаре в инвентаре", description = "Обновляет информацию о товаре по его ID")
    public ResponseEntity<String> updateInventoryItem(
            @PathVariable @Valid @NotBlank String itemId,
            @Valid @RequestBody MerchandiseInventoryRequest inventoryRequest) {
        try {
            merchandiseInventoryService.update(itemId, inventoryRequest);
            log.info("Inventory item was updated successfully");
            return new ResponseEntity<>("Inventory item was updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to update inventory item", e);
            return new ResponseEntity<>("Inventory item wasn't updated: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Operation(summary = "Удалить товар из инвентаря", description = "Удаляет товар из инвентаря по ID")
    public ResponseEntity<String> deleteInventoryItem(
            @PathVariable @Valid @NotBlank String itemId) {
        try {
            merchandiseInventoryService.delete(itemId);
            log.info("Inventory item with ID " + itemId + " was deleted");
            return new ResponseEntity<>("Inventory item was deleted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while deleting inventory item", e);
            return new ResponseEntity<>("Error deleting inventory item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
