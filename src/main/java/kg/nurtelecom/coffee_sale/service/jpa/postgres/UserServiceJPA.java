package kg.nurtelecom.coffee_sale.service.jpa.postgres;

import kg.nurtelecom.coffee_sale.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserServiceJPA extends JpaRepository<UserEntity, String> {
}
