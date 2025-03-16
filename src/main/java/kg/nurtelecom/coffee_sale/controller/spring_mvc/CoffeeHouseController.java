package kg.nurtelecom.coffee_sale.controller.spring_mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeHouseRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeHouseResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeHouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;

import java.util.List;

@Controller
@RequestMapping("/coffee-houses")
public class CoffeeHouseController {

    private final CoffeeHouseService coffeeHouseService;

    public CoffeeHouseController(CoffeeHouseService coffeeHouseService) {
        this.coffeeHouseService = coffeeHouseService;
    }

    @GetMapping
    public String listCoffeeHouses(Model model) {
        List<CoffeeHouseResponse> coffeeHouses = coffeeHouseService.findAll();
        model.addAttribute("coffeeHouses", coffeeHouses);
        return "coffee-houses/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("coffeeHouse", new CoffeeHouseRequest(null, "", 0, 0, 0));
        return "coffee-houses/create";
    }

    @PostMapping
    public String createCoffeeHouse(@Valid @ModelAttribute("coffeeHouse") CoffeeHouseRequest coffeeHouseRequest,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "coffee-houses/create";
        }

        try {
            coffeeHouseService.create(coffeeHouseRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Coffee house created successfully");
            return "redirect:/coffee-houses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating coffee house: " + e.getMessage());
            return "coffee-houses/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        try {
            CoffeeHouseResponse coffeeHouse = coffeeHouseService.findById(id);
            CoffeeHouseRequest request = new CoffeeHouseRequest(
                    coffeeHouse.storeId(),
                    coffeeHouse.city(),
                    coffeeHouse.coffee(),
                    coffeeHouse.merch(),
                    coffeeHouse.total()
            );
            model.addAttribute("coffeeHouse", request);
            return "coffee-houses/edit";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coffee house not found", e);
        }
    }

    @PostMapping("/{id}")
    public String updateCoffeeHouse(@PathVariable Integer id,
                                    @Valid @ModelAttribute("coffeeHouse") CoffeeHouseRequest coffeeHouseRequest,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "coffee-houses/edit";
        }

        try {
            coffeeHouseService.update(id, coffeeHouseRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Coffee house updated successfully");
            return "redirect:/coffee-houses";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating coffee house: " + e.getMessage());
            return "coffee-houses/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCoffeeHouse(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            coffeeHouseService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Coffee house deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting coffee house: " + e.getMessage());
        }
        return "redirect:/coffee-houses";
    }
}