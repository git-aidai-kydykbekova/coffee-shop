package kg.nurtelecom.coffee_sale.service.jpa.postgres;


import kg.nurtelecom.coffee_sale.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CoffeePostgresServiceJPA extends JpaRepository<Coffee, String> {

}
