package kg.nurtelecom.coffee_sale.service.jpa.postgres;
import kg.nurtelecom.coffee_sale.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AuthorityServiceJPA extends JpaRepository<Authority, String> {

    List<Authority> findAuthoritiesByUser_Username(String username);
}