package com.logistpulse.smartinventory.chain;

public class CapacityHandler extends InventoryHandler {
    @Override
    protected ValidationResult validate(InventoryRequest request, InventoryService inventory) {
        int remaining = inventory.getReservationCapacityRemaining(request.getWarehouse());
        if (remaining < request.getQuantity()) {
            return ValidationResult.rejected("Capacidad: el límite de reservas de la bodega se supera (restan " + remaining + ")");
        }
        return ValidationResult.approved();
    }
}
