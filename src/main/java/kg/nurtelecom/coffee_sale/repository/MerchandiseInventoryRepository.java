package kg.nurtelecom.coffee_sale.repository;

import kg.nurtelecom.coffee_sale.entity.MerchandiseInventory;
import kg.nurtelecom.coffee_sale.payload.request.MerchandiseInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.MerchandiseInventoryResponse;
import kg.nurtelecom.coffee_sale.service.MerchandiseInventoryService;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.MerchandiseInventoryPostgresServiceJPA;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MerchandiseInventoryRepository implements MerchandiseInventoryService {

    private final MerchandiseInventoryPostgresServiceJPA merchandiseInventoryServiceJPA;

    public MerchandiseInventoryRepository(MerchandiseInventoryPostgresServiceJPA merchandiseInventoryServiceJPA) {
        this.merchandiseInventoryServiceJPA = merchandiseInventoryServiceJPA;
    }

    @Override
    public List<MerchandiseInventoryResponse> findAll() {
        List<MerchandiseInventory> inventoryItems = merchandiseInventoryServiceJPA.findAll();
        List<MerchandiseInventoryResponse> responses = new ArrayList<>();

        for (MerchandiseInventory item : inventoryItems) {
            responses.add(mapToResponse(item));
        }

        return responses;
    }

    @Override
    public MerchandiseInventoryResponse findById(String itemId) {
        Optional<MerchandiseInventory> itemOptional = merchandiseInventoryServiceJPA.findById(itemId);
        if (itemOptional.isPresent()) {
            return mapToResponse(itemOptional.get());
        }
        throw new RuntimeException("Merchandise inventory item not found with id: " + itemId);
    }

    @Override
    public List<MerchandiseInventoryResponse> findBySupplier(Integer supId) {
        List<MerchandiseInventory> inventoryItems = merchandiseInventoryServiceJPA.findBySupId(supId);
        List<MerchandiseInventoryResponse> responses = new ArrayList<>();

        for (MerchandiseInventory item : inventoryItems) {
            responses.add(mapToResponse(item));
        }

        return responses;
    }

    @Override
    @Transactional
    public MerchandiseInventoryResponse create(MerchandiseInventoryRequest request) {
        if (merchandiseInventoryServiceJPA.existsById(request.itemId())) {
            throw new RuntimeException("Item with ID " + request.itemId() + " already exists");
        }

        MerchandiseInventory item = new MerchandiseInventory();
        updateItemFromRequest(item, request);

        MerchandiseInventory savedItem = merchandiseInventoryServiceJPA.save(item);
        return mapToResponse(savedItem);
    }

    @Override
    @Transactional
    public MerchandiseInventoryResponse update(String itemId, MerchandiseInventoryRequest request) {
        Optional<MerchandiseInventory> itemOptional = merchandiseInventoryServiceJPA.findById(itemId);
        if (itemOptional.isPresent()) {
            MerchandiseInventory item = itemOptional.get();

            if (!itemId.equals(request.itemId()) && merchandiseInventoryServiceJPA.existsById(request.itemId())) {
                throw new RuntimeException("Cannot change item ID to " + request.itemId() + " as it already exists");
            }

            updateItemFromRequest(item, request);

            MerchandiseInventory updatedItem = merchandiseInventoryServiceJPA.save(item);
            return mapToResponse(updatedItem);
        }
        throw new RuntimeException("Merchandise inventory item not found with id: " + itemId);
    }

    @Override
    @Transactional
    public void delete(String itemId) {
        if (merchandiseInventoryServiceJPA.existsById(itemId)) {
            merchandiseInventoryServiceJPA.deleteById(itemId);
        } else {
            throw new RuntimeException("Merchandise inventory item not found with id: " + itemId);
        }
    }



    private void updateItemFromRequest(MerchandiseInventory item, MerchandiseInventoryRequest request) {
        item.setItemId(request.itemId());
        item.setItemName(request.itemName());
        item.setSupId(request.supId());

        item.setQuantity(request.quantity() != null ? request.quantity() : 0);
        item.setDate(request.date());
    }

    private MerchandiseInventoryResponse mapToResponse(MerchandiseInventory item) {
        String supplierName = item.getSupplier() != null ? item.getSupplier().getSupName() : null;

        return new MerchandiseInventoryResponse(
                item.getItemId(),
                item.getItemName(),
                item.getSupId(),
                item.getQuantity(),
                item.getDate(),
                supplierName
        );
    }
}