package kg.nurtelecom.coffee_sale.controller.spring_mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.coffee_sale.payload.request.CoffeeRequest;
import kg.nurtelecom.coffee_sale.payload.respone.CoffeeResponse;
import kg.nurtelecom.coffee_sale.payload.respone.SupplierResponse;
import kg.nurtelecom.coffee_sale.service.CoffeeService;
import kg.nurtelecom.coffee_sale.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/coffees")
public class CoffeeController {

    private final CoffeeService coffeeService;
    private final SupplierService supplierService;

    public CoffeeController(CoffeeService coffeeService,
                            SupplierService supplierService) {
        this.coffeeService = coffeeService;
        this.supplierService = supplierService;
    }

    @GetMapping
    public String listCoffees(Model model) {
        List<CoffeeResponse> coffees = coffeeService.findAll();
        model.addAttribute("coffees", coffees);
        return "coffees/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("coffeeRequest", new CoffeeRequest("", null, 0.0f, 0, 0));
        List<SupplierResponse> suppliers = supplierService.findAll();
        model.addAttribute("suppliers", suppliers);

        return "coffees/create";
    }

    @PostMapping
    public String createCoffee(@Valid @ModelAttribute("coffeeRequest") CoffeeRequest coffeeRequest,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<SupplierResponse> suppliers = supplierService.findAll();
            model.addAttribute("suppliers", suppliers);
            return "coffees/create";
        }

        try {
            coffeeService.create(coffeeRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Coffee created successfully");
            return "redirect:/coffees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating coffee: " + e.getMessage());
            return "redirect:/coffees/new";
        }
    }

    @GetMapping("/{coffeeName}/edit")
    public String showEditForm(@PathVariable String coffeeName, Model model) {
        try {
            CoffeeResponse coffee = coffeeService.findById(coffeeName);
            CoffeeRequest coffeeRequest = new CoffeeRequest(
                    coffee.cofName(),
                    coffee.supId(),
                    coffee.price(),
                    coffee.sales(),
                    coffee.total()
            );

            model.addAttribute("coffeeRequest", coffeeRequest);
            model.addAttribute("originalCoffeeName", coffeeName);
            List<SupplierResponse> suppliers = supplierService.findAll();
            model.addAttribute("suppliers", suppliers);

            return "coffees/edit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Coffee not found: " + e.getMessage());
            return "redirect:/coffees";
        }
    }

    @PostMapping("/{coffeeName}")
    public String updateCoffee(@PathVariable String coffeeName,
                               @Valid @ModelAttribute("coffeeRequest") CoffeeRequest coffeeRequest,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<SupplierResponse> suppliers = supplierService.findAll();
            model.addAttribute("suppliers", suppliers);
            model.addAttribute("originalCoffeeName", coffeeName);
            return "coffees/edit";
        }

        try {
            coffeeService.update(coffeeName, coffeeRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Coffee updated successfully");
            return "redirect:/coffees";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating coffee: " + e.getMessage());
            return "redirect:/coffees/" + coffeeName + "/edit";
        }
    }
    @PostMapping("/{coffeeName}/delete")
    public String deleteCoffee(@PathVariable String coffeeName, RedirectAttributes redirectAttributes) {
        try {
            coffeeService.delete(coffeeName);
            redirectAttributes.addFlashAttribute("successMessage", "Coffee deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting coffee: " + e.getMessage());
        }
        return "redirect:/coffees";
    }
}