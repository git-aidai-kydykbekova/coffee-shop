package kg.nurtelecom.coffee_sale.service;

import kg.nurtelecom.coffee_sale.payload.request.CoffeeRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeResponse;

import java.util.List;


public interface CoffeeService {
    List<CoffeeResponse> findAll();

    CoffeeResponse findById(String coffeeName);

    CoffeeResponse create(CoffeeRequest coffeeRequest);

    CoffeeResponse update(String coffeeName, CoffeeRequest coffeeRequest);

    void delete(String coffeeName);

}
