package kg.nurtelecom.coffee_sale.service;


import kg.nurtelecom.coffee_sale.payload.request.CoffeeInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeInventoryResponse;

import java.util.List;

public interface CoffeeInventoryService {
    List<CoffeeInventoryResponse> findAll();

    CoffeeInventoryResponse findById(Long id);

    CoffeeInventoryResponse create(CoffeeInventoryRequest request);

    CoffeeInventoryResponse update(Long id, CoffeeInventoryRequest request);

    void delete(Long id);

}