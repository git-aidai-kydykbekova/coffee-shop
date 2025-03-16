package kg.nurtelecom.coffee_sale.service.jpa.h2;


import kg.nurtelecom.coffee_sale.entity.CoffeeHouse;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CoffeeHouseH2ServiceJPA extends JpaRepository<CoffeeHouse, Integer> {

}