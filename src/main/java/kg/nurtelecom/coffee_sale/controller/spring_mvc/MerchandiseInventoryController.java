package kg.nurtelecom.coffee_sale.controller.spring_mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.coffee_sale.payload.request.MerchandiseInventoryRequest;
import kg.nurtelecom.coffee_sale.payload.respone.MerchandiseInventoryResponse;
import kg.nurtelecom.coffee_sale.payload.respone.SupplierResponse;
import kg.nurtelecom.coffee_sale.service.MerchandiseInventoryService;
import kg.nurtelecom.coffee_sale.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/merchandise-inventory")
public class MerchandiseInventoryController {

    private final MerchandiseInventoryService merchandiseInventoryService;
    private final SupplierService supplierService;

    public MerchandiseInventoryController(MerchandiseInventoryService merchandiseInventoryService,
                                          SupplierService supplierService) {
        this.merchandiseInventoryService = merchandiseInventoryService;
        this.supplierService = supplierService;
    }

    @GetMapping
    public String listInventory(Model model) {
        List<MerchandiseInventoryResponse> inventory = merchandiseInventoryService.findAll();
        model.addAttribute("inventoryItems", inventory);
        return "merchandise/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        MerchandiseInventoryRequest inventoryRequest = new MerchandiseInventoryRequest(
                "", "", null, 0, LocalDate.now()
        );
        model.addAttribute("inventoryRequest", inventoryRequest);

        List<SupplierResponse> suppliers = supplierService.findAll();
        model.addAttribute("suppliers", suppliers);

        return "merchandise/create";
    }

    @PostMapping
    public String createInventoryItem(@Valid @ModelAttribute("inventoryRequest") MerchandiseInventoryRequest inventoryRequest,
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<SupplierResponse> suppliers = supplierService.findAll();
            model.addAttribute("suppliers", suppliers);
            return "merchandise/create";
        }

        try {
            merchandiseInventoryService.create(inventoryRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Inventory item created successfully");
            return "redirect:/merchandise-inventory";
        } catch (Exception e) {
            List<SupplierResponse> suppliers = supplierService.findAll();
            model.addAttribute("suppliers", suppliers);
            model.addAttribute("errorMessage", "Error creating inventory item: " + e.getMessage());
            return "merchandise/create";
        }
    }

    @GetMapping("/{itemId}/edit")
    public String showEditForm(@PathVariable String itemId, Model model) {
        try {
            MerchandiseInventoryResponse item = merchandiseInventoryService.findById(itemId);

            MerchandiseInventoryRequest inventoryRequest = new MerchandiseInventoryRequest(
                    item.itemId(),
                    item.itemName(),
                    item.supId(),
                    item.quantity(),
                    item.date()
            );

            model.addAttribute("inventoryRequest", inventoryRequest);

            List<SupplierResponse> suppliers = supplierService.findAll();
            model.addAttribute("suppliers", suppliers);

            return "merchandise/edit";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found", e);
        }
    }

    @PostMapping("/{itemId}")
    public String updateInventoryItem(@PathVariable String itemId,
                                      @Valid @ModelAttribute("inventoryRequest") MerchandiseInventoryRequest inventoryRequest,
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<SupplierResponse> suppliers = supplierService.findAll();
            model.addAttribute("suppliers", suppliers);
            return "merchandise/edit";
        }

        try {
            merchandiseInventoryService.update(itemId, inventoryRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Inventory item updated successfully");
            return "redirect:/merchandise-inventory";
        } catch (Exception e) {
            List<SupplierResponse> suppliers = supplierService.findAll();
            model.addAttribute("suppliers", suppliers);
            model.addAttribute("errorMessage", "Error updating inventory item: " + e.getMessage());
            return "merchandise/edit";
        }
    }


    @PostMapping("/{itemId}/delete")
    public String deleteInventoryItem(@PathVariable String itemId, RedirectAttributes redirectAttributes) {
        try {
            merchandiseInventoryService.delete(itemId);
            redirectAttributes.addFlashAttribute("successMessage", "Inventory item deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting inventory item: " + e.getMessage());
        }
        return "redirect:/merchandise-inventory";
    }

    @GetMapping("/supplier/{supId}")
    public String listInventoryBySupplier(@PathVariable Integer supId, Model model) {
        try {
            List<MerchandiseInventoryResponse> inventory = merchandiseInventoryService.findBySupplier(supId);
            SupplierResponse supplier = supplierService.findById(supId);

            model.addAttribute("inventoryItems", inventory);
            model.addAttribute("supplier", supplier);

            return "merchandise/list-by-supplier";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found", e);
        }
    }
}