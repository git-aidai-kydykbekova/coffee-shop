package kg.nurtelecom.coffee_sale.service.jpa.h2;


import kg.nurtelecom.coffee_sale.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CoffeeH2ServiceJPA extends JpaRepository<Coffee, String> {

}
