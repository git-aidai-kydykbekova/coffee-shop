package kg.nurtelecom.coffee_sale.repository;

import kg.nurtelecom.coffee_sale.entity.Coffee;
import kg.nurtelecom.coffee_sale.entity.CoffeeInventory;
import kg.nurtelecom.coffee_sale.entity.Supplier;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeInventoryResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeInventoryService;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.CoffeeInventoryPostgresServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.CoffeePostgresServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.SupplierPostgresServiceJPA;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CoffeeInventoryRepository implements CoffeeInventoryService {

    private final CoffeeInventoryPostgresServiceJPA coffeeInventoryServiceJPA;
    private final CoffeePostgresServiceJPA coffeeServiceJPA;
    private final SupplierPostgresServiceJPA supplierServiceJPA;

    public CoffeeInventoryRepository(CoffeeInventoryPostgresServiceJPA coffeeInventoryServiceJPA, CoffeePostgresServiceJPA coffeeServiceJPA, SupplierPostgresServiceJPA supplierServiceJPA) {
        this.coffeeInventoryServiceJPA = coffeeInventoryServiceJPA;
        this.coffeeServiceJPA = coffeeServiceJPA;
        this.supplierServiceJPA = supplierServiceJPA;
    }


    @Override
    public List<CoffeeInventoryResponse> findAll() {
        List<CoffeeInventory> inventories = coffeeInventoryServiceJPA.findAll();
        List<CoffeeInventoryResponse> responses = new ArrayList<>();

        for (CoffeeInventory inventory : inventories) {
            responses.add(mapToResponse(inventory));
        }

        return responses;
    }

    @Override
    public CoffeeInventoryResponse findById(Long id) {
        Optional<CoffeeInventory> inventoryOptional = coffeeInventoryServiceJPA.findById(id);
        if (inventoryOptional.isPresent()) {
            return mapToResponse(inventoryOptional.get());
        }
        throw new RuntimeException("Inventory not found with id: " + id);
    }

    @Override
    public CoffeeInventoryResponse create(CoffeeInventoryRequest request) {
        Optional<Coffee> coffeeOptional = coffeeServiceJPA.findById(request.cofName());
        if (coffeeOptional.isEmpty()) {
            throw new RuntimeException("Coffee not found with name: " + request.cofName());
        }
        Optional<Supplier> supplierOptional = supplierServiceJPA.findById(request.supId());
        if (supplierOptional.isEmpty()) {
            throw new RuntimeException("Supplier not found with id: " + request.supId());
        }
        CoffeeInventory inventory = new CoffeeInventory();
        updateInventoryFromRequest(inventory, request);

        CoffeeInventory savedInventory = coffeeInventoryServiceJPA.save(inventory);
        return mapToResponse(savedInventory);
    }

    @Override
    public CoffeeInventoryResponse update(Long id, CoffeeInventoryRequest request) {
        Optional<CoffeeInventory> inventoryOptional = coffeeInventoryServiceJPA.findById(id);
        if (inventoryOptional.isEmpty()) {
            throw new RuntimeException("Inventory not found with id: " + id);
        }

        Optional<Coffee> coffeeOptional = coffeeServiceJPA.findById(request.cofName());
        if (coffeeOptional.isEmpty()) {
            throw new RuntimeException("Coffee not found with name: " + request.cofName());
        }

        Optional<Supplier> supplierOptional = supplierServiceJPA.findById(request.supId());
        if (supplierOptional.isEmpty()) {
            throw new RuntimeException("Supplier not found with id: " + request.supId());
        }

        CoffeeInventory inventory = inventoryOptional.get();
        updateInventoryFromRequest(inventory, request);

        CoffeeInventory updatedInventory = coffeeInventoryServiceJPA.save(inventory);
        return mapToResponse(updatedInventory);
    }

    @Override
    public void delete(Long id) {
        if (!coffeeInventoryServiceJPA.existsById(id)) {
            throw new RuntimeException("Inventory not found with id: " + id);
        }
        coffeeInventoryServiceJPA.deleteById(id);
    }

    private void updateInventoryFromRequest(CoffeeInventory inventory, CoffeeInventoryRequest request) {
        inventory.setWarehouseId(request.warehouseId());
        inventory.setCofName(request.cofName());
        inventory.setSupId(request.supId());
        inventory.setQuantity(request.quantity() != null ? request.quantity() : 0);
        inventory.setDate(request.date());
    }

    private CoffeeInventoryResponse mapToResponse(CoffeeInventory inventory) {
        Float coffeePrice = null;
        String supplierName = null;

        if (inventory.getCoffee() != null) {
            coffeePrice = inventory.getCoffee().getPrice();
        }

        if (inventory.getSupplier() != null) {
            supplierName = inventory.getSupplier().getSupName();
        }

        return new CoffeeInventoryResponse(
                inventory.getId(),
                inventory.getWarehouseId(),
                inventory.getCofName(),
                inventory.getSupId(),
                inventory.getQuantity(),
                inventory.getDate(),
                coffeePrice,
                supplierName
        );
    }
}