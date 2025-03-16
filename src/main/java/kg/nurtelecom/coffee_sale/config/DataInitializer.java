package kg.nurtelecom.coffee_sale.config;

import kg.nurtelecom.coffee_sale.entity.*;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Component
public class DataInitializer implements CommandLineRunner {

    private final UserServiceJPA userService;
    private final AuthorityServiceJPA authorityService;
    private final CoffeePostgresServiceJPA coffeeRepo;
    private final SupplierPostgresServiceJPA supplierRepo;
    private final CoffeeHousePostgresServiceJPA coffeeHouseRepo;
    private final CoffeeInventoryPostgresServiceJPA coffeeInventoryRepo;
    private final MerchandiseInventoryPostgresServiceJPA merchInventoryRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserServiceJPA userService, AuthorityServiceJPA authorityService, CoffeePostgresServiceJPA coffeeRepo, SupplierPostgresServiceJPA supplierRepo, CoffeeHousePostgresServiceJPA coffeeHouseRepo, CoffeeInventoryPostgresServiceJPA coffeeInventoryRepo, MerchandiseInventoryPostgresServiceJPA merchInventoryRepo, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authorityService = authorityService;
        this.coffeeRepo = coffeeRepo;
        this.supplierRepo = supplierRepo;
        this.coffeeHouseRepo = coffeeHouseRepo;
        this.coffeeInventoryRepo = coffeeInventoryRepo;
        this.merchInventoryRepo = merchInventoryRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initUsers();
        initSuppliers();
        initCoffees();
        initCoffeeInventories();
        initCoffeeHouses();
        initMerchandiseInventories();
    }

    private void initUsers() {
        if (userService.count() == 0) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("user");
            userEntity.setPassword(passwordEncoder.encode("user"));
            userEntity.setEnabled(true);
            userService.save(userEntity);

            Authority authority = new Authority();
            authority.setUser(userEntity);
            authority.setAuthority("ROLE_USER");
            authorityService.save(authority);

            UserEntity userEntityAdmin = new UserEntity();
            userEntityAdmin.setUsername("admin");
            userEntityAdmin.setPassword(passwordEncoder.encode("admin"));
            userEntityAdmin.setEnabled(true);
            userService.save(userEntityAdmin);

            Authority authorityAdmin = new Authority();
            authorityAdmin.setUser(userEntityAdmin);
            authorityAdmin.setAuthority("ROLE_ADMIN");
            authorityService.save(authorityAdmin);
        }
    }
    private void initSuppliers() {
        if (supplierRepo.count() == 0) {
            List<Supplier> suppliers = List.of(
                    new Supplier("Coffee Supplier 1", "123 Main St", "New York", "NY", "10001"),
                    new Supplier("Coffee Supplier 2", "456 Market St", "Los Angeles", "CA", "90001"),
                    new Supplier("Coffee Supplier 3", "789 Elm St", "Chicago", "IL", "60601"),
                    new Supplier("Coffee Supplier 4", "321 Oak St", "Houston", "TX", "77001"),
                    new Supplier("Coffee Supplier 5", "654 Pine St", "Miami", "FL", "33101"),
                    new Supplier("Coffee Supplier 6", "987 Cedar St", "Seattle", "WA", "98101"),
                    new Supplier("Coffee Supplier 7", "111 Maple St", "Boston", "MA", "02101")
            );
            supplierRepo.saveAll(suppliers);
        }
    }

    private void initCoffees() {
        if (coffeeRepo.count() == 0) {
            List<Coffee> coffees = List.of(
                    new Coffee("Espresso", 1, 3.99f, 100, 500),
                    new Coffee("Americano", 2, 4.49f, 80, 400),
                    new Coffee("Latte", 3, 5.99f, 120, 600),
                    new Coffee("Cappuccino", 4, 5.49f, 90, 450),
                    new Coffee("Mocha", 5, 6.49f, 70, 350),
                    new Coffee("Macchiato", 6, 4.99f, 60, 300),
                    new Coffee("Flat White", 7, 5.29f, 50, 250)
            );
            coffeeRepo.saveAll(coffees);
        }
    }

    private void initCoffeeHouses() {
        if (coffeeHouseRepo.count() == 0) {
            List<CoffeeHouse> coffeeHouses = List.of(
                    new CoffeeHouse(1, "New York", 200, 50, 250),
                    new CoffeeHouse(2, "Los Angeles", 180, 60, 240),
                    new CoffeeHouse(3, "Chicago", 160, 40, 200),
                    new CoffeeHouse(4, "Houston", 190, 55, 245),
                    new CoffeeHouse(5, "Miami", 170, 45, 215),
                    new CoffeeHouse(6, "Seattle", 150, 35, 185),
                    new CoffeeHouse(7, "Boston", 140, 30, 170)
            );
            coffeeHouseRepo.saveAll(coffeeHouses);
        }
    }

    private void initCoffeeInventories() {
        if (coffeeInventoryRepo.count() == 0) {
            List<CoffeeInventory> inventories = List.of(
                    new CoffeeInventory(1, "Espresso", 1, 100, LocalDate.now()),
                    new CoffeeInventory(2, "Americano", 2, 80, LocalDate.now()),
                    new CoffeeInventory(3, "Latte", 3, 120, LocalDate.now()),
                    new CoffeeInventory(4, "Cappuccino", 4, 90, LocalDate.now()),
                    new CoffeeInventory(5, "Mocha", 5, 70, LocalDate.now()),
                    new CoffeeInventory(6, "Macchiato", 6, 60, LocalDate.now()),
                    new CoffeeInventory(7, "Flat White", 7, 50, LocalDate.now())
            );
            coffeeInventoryRepo.saveAll(inventories);
        }
    }

    private void initMerchandiseInventories() {
        if (merchInventoryRepo.count() == 0) {
            List<MerchandiseInventory> merchInventories = List.of(
                    new MerchandiseInventory("M1", "Coffee Mug", 1, 200, LocalDate.now()),
                    new MerchandiseInventory("M2", "T-Shirt", 2, 150, LocalDate.now()),
                    new MerchandiseInventory("M3", "Coffee Beans", 3, 300, LocalDate.now()),
                    new MerchandiseInventory("M4", "Water Bottle", 4, 120, LocalDate.now()),
                    new MerchandiseInventory("M5", "Coffee Machine", 5, 80, LocalDate.now()),
                    new MerchandiseInventory("M6", "Gift Card", 6, 250, LocalDate.now()),
                    new MerchandiseInventory("M7", "Notebook", 7, 100, LocalDate.now())
            );
            merchInventoryRepo.saveAll(merchInventories);
        }
    }
}
