package com.logistpulse.smartinventory.chain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class InventoryService {
    public static class Lot {
        private final LocalDate expiryDate;
        private final boolean quarantined;
        private final int quantity;
        private int reserved;

        public Lot(int quantity, LocalDate expiryDate, boolean quarantined) {
            this.quantity = quantity;
            this.expiryDate = expiryDate;
            this.quarantined = quarantined;
        }
        public LocalDate getExpiryDate() { return expiryDate; }
        public boolean isQuarantined() { return quarantined; }
        public int getAvailable() { return quantity - reserved; }
        private void reserve(int amount) { reserved += amount; }
    }

    private final Set<String> products = new HashSet<>();
    private final Map<String, Lot> lots = new HashMap<>();
    private final Map<String, Integer> reservationLimits = new HashMap<>();
    private final Map<String, Integer> reservedByWarehouse = new HashMap<>();
    private final Set<String> processedOrders = new HashSet<>();
    private final InventoryChain chain = new InventoryChain();

    private static String key(String sku, String warehouse, String lotId) {
        return sku + "|" + warehouse + "|" + lotId;
    }

    public void addProduct(String sku) { products.add(sku); }
    public boolean hasProduct(String sku) { return products.contains(sku); }

    public void addLot(String sku, String warehouse, String lotId, int quantity, LocalDate expiry, boolean quarantined) {
        if (!hasProduct(sku) || quantity < 0 || expiry == null) {
            throw new IllegalArgumentException("Producto, cantidad o fecha de lote inválidos");
        }
        lots.put(key(sku, warehouse, lotId), new Lot(quantity, expiry, quarantined));
    }

    public void setReservationCapacity(String warehouse, int limit) {
        if (limit < 0) throw new IllegalArgumentException("Capacidad inválida");
        reservationLimits.put(warehouse, limit);
    }

    public Lot findLot(String sku, String warehouse, String lotId) {
        return lots.get(key(sku, warehouse, lotId));
    }

    public int getAvailableStock(String sku, String warehouse) {
        int available = 0;
        String prefix = sku + "|" + warehouse + "|";
        for (Map.Entry<String, Lot> item : lots.entrySet()) {
            if (item.getKey().startsWith(prefix)) available += item.getValue().getAvailable();
        }
        return available;
    }

    public int getReservationCapacityRemaining(String warehouse) {
        return reservationLimits.getOrDefault(warehouse, 0) - reservedByWarehouse.getOrDefault(warehouse, 0);
    }

    // Validación y reserva se realizan juntas para que una falla no descuente unidades.
    public synchronized ValidationResult reserve(InventoryRequest request) {
        if (request == null) return ValidationResult.rejected("Solicitud nula");
        if (request.getOrderId() == null || request.getOrderId().trim().isEmpty()) {
            return ValidationResult.rejected("Pedido: identificador obligatorio");
        }
        if (processedOrders.contains(request.getOrderId())) {
            return ValidationResult.rejected("Pedido: ya fue procesado (se evita reserva duplicada)");
        }
        ValidationResult result = chain.validate(request, this);
        if (!result.isApproved()) return result;

        Lot lot = findLot(request.getSku(), request.getWarehouse(), request.getLotId());
        lot.reserve(request.getQuantity());
        reservedByWarehouse.merge(request.getWarehouse(), request.getQuantity(), Integer::sum);
        processedOrders.add(request.getOrderId());
        return ValidationResult.approved();
    }
}
