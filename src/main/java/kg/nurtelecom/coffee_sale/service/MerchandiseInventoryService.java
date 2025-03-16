package kg.nurtelecom.coffee_sale.service;

import kg.nurtelecom.coffee_sale.payload.request.MerchandiseInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.MerchandiseInventoryResponse;
import java.util.List;

public interface MerchandiseInventoryService {
    List<MerchandiseInventoryResponse> findAll();

    MerchandiseInventoryResponse findById(String itemId);

    List<MerchandiseInventoryResponse> findBySupplier(Integer supId);

    MerchandiseInventoryResponse create(MerchandiseInventoryRequest request);

    MerchandiseInventoryResponse update(String itemId, MerchandiseInventoryRequest request);

    void delete(String itemId);

}