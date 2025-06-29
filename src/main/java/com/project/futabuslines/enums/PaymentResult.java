package com.project.futabuslines.enums;

public enum PaymentResult {
    SUCCESS("Payment successful"),
    FAILED("Payment failed"),
    HASH_MISMATCH("Hash verification failed"),
    MISSING_HASH("Missing security hash"),
    INVALID_PARAMS("Invalid parameters"),
    ERROR("System error");

    private final String description;

    PaymentResult(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    @lombok.Data
    @lombok.Builder
    public static class PaymentDetails {
        private String orderId;
        private long amount;
        private String responseCode;
        private String transactionNo;
        private String bankCode;
        private String payDate;
    }
}
