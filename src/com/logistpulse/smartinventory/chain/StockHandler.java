package com.logistpulse.smartinventory.chain;

public class StockHandler extends InventoryHandler {
    @Override
    protected ValidationResult validate(InventoryRequest request, InventoryService inventory) {
        if (request.getWarehouse() == null || request.getWarehouse().trim().isEmpty()) {
            return ValidationResult.rejected("Stock: falta la bodega");
        }
        if (request.getQuantity() <= 0) {
            return ValidationResult.rejected("Stock: la cantidad debe ser mayor que cero");
        }
        int available = inventory.getAvailableStock(request.getSku(), request.getWarehouse());
        if (available < request.getQuantity()) {
            return ValidationResult.rejected("Stock: insuficiente (disponible " + available + ", solicitado " + request.getQuantity() + ")");
        }
        return ValidationResult.approved();
    }
}
