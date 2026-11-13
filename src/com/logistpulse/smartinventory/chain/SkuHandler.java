package com.logistpulse.smartinventory.chain;

public class SkuHandler extends InventoryHandler {
    @Override
    protected ValidationResult validate(InventoryRequest request, InventoryService inventory) {
        if (request.getSku() == null || request.getSku().trim().isEmpty()) {
            return ValidationResult.rejected("SKU: es obligatorio indicar el producto");
        }
        if (!inventory.hasProduct(request.getSku())) {
            return ValidationResult.rejected("SKU: el producto " + request.getSku() + " no existe");
        }
        return ValidationResult.approved();
    }
}
