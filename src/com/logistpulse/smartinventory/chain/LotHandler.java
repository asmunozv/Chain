package com.logistpulse.smartinventory.chain;

import java.time.LocalDate;

public class LotHandler extends InventoryHandler {
    @Override
    protected ValidationResult validate(InventoryRequest request, InventoryService inventory) {
        if (request.getLotId() == null || request.getLotId().trim().isEmpty()) {
            return ValidationResult.rejected("Lote: es obligatorio indicar un lote");
        }
        InventoryService.Lot lot = inventory.findLot(request.getSku(), request.getWarehouse(), request.getLotId());
        if (lot == null) {
            return ValidationResult.rejected("Lote: no existe para ese SKU y bodega");
        }
        if (lot.isQuarantined()) {
            return ValidationResult.rejected("Lote: se encuentra en cuarentena");
        }
        if (lot.getExpiryDate().isBefore(LocalDate.now())) {
            return ValidationResult.rejected("Lote: está vencido");
        }
        if (lot.getAvailable() < request.getQuantity()) {
            return ValidationResult.rejected("Lote: unidades insuficientes (disponible " + lot.getAvailable() + ")");
        }
        return ValidationResult.approved();
    }
}
