package kg.nurtelecom.coffee_sale.repository.user_details;


import kg.nurtelecom.coffee_sale.entity.Authority;
import kg.nurtelecom.coffee_sale.entity.UserEntity;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.AuthorityServiceJPA;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.UserServiceJPA;
import kg.nurtelecom.coffee_sale.service.user_details.CustomUserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomUserDetailsRepository implements CustomUserDetailsService {

    private final UserServiceJPA userPostgresJpaService;
    private final AuthorityServiceJPA authorityPostgresServiceJpa;

    public CustomUserDetailsRepository(UserServiceJPA userPostgresJpaService, AuthorityServiceJPA authorityPostgresServiceJpa) {
        this.userPostgresJpaService = userPostgresJpaService;
        this.authorityPostgresServiceJpa = authorityPostgresServiceJpa;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> optionalUserEntity = userPostgresJpaService.findById(username);
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();

            List<Authority> authorities = authorityPostgresServiceJpa.findAuthoritiesByUser_Username(username);
            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
            for (Authority authority : authorities) {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
                authorityList.add(simpleGrantedAuthority);
            }

            return User.withUsername(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .disabled(!userEntity.isEnabled())
                    .authorities(authorityList)
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
