package kg.nurtelecom.coffee_sale.service;

import kg.nurtelecom.coffee_sale.payload.request.CoffeeHouseRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeHouseResponse;

import java.util.List;


public interface CoffeeHouseService {
    List<CoffeeHouseResponse> findAll();

    CoffeeHouseResponse findById(Integer id);

    CoffeeHouseResponse create(CoffeeHouseRequest coffeeHouseRequest);

    CoffeeHouseResponse update(Integer id, CoffeeHouseRequest coffeeHouseRequest);

    void delete(Integer id);
}
