package com.logistpulse.smartinventory.chain;

public class ValidationResult {
    private final boolean approved;
    private final String message;

    private ValidationResult(boolean approved, String message) {
        this.approved = approved;
        this.message = message;
    }

    public static ValidationResult approved() { return new ValidationResult(true, "Validaciones aprobadas"); }
    public static ValidationResult rejected(String reason) { return new ValidationResult(false, reason); }
    public boolean isApproved() { return approved; }
    public String getMessage() { return message; }
}
