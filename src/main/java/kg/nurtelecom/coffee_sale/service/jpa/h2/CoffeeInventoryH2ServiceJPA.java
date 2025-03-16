package kg.nurtelecom.coffee_sale.service.jpa.h2;


import kg.nurtelecom.coffee_sale.entity.CoffeeInventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CoffeeInventoryH2ServiceJPA extends JpaRepository<CoffeeInventory, Long> {

}