package kg.nurtelecom.coffee_sale.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "coffee_houses")
public class CoffeeHouse {

    @Id
    @Column(name = "store_id", nullable = false)
    @NotNull(message = "Store ID is mandatory")
    private Integer storeId;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "City is mandatory")
    private String city;

    @Column(name = "coffee", nullable = false)
    private Integer coffee = 0;

    @Column(name = "merch", nullable = false)
    private Integer merch = 0;

    @Column(name = "total", nullable = false)
    private Integer total = 0;

    public CoffeeHouse() {
    }

    public CoffeeHouse(Integer storeId, String city, Integer coffee, Integer merch, Integer total) {
        this.storeId = storeId;
        this.city = city;
        this.coffee = coffee;
        this.merch = merch;
        this.total = total;
    }


    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCoffee() {
        return coffee;
    }

    public void setCoffee(Integer coffee) {
        this.coffee = coffee;
    }

    public Integer getMerch() {
        return merch;
    }

    public void setMerch(Integer merch) {
        this.merch = merch;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}