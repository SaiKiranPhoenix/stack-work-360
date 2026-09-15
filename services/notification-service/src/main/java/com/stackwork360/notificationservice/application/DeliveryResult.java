package com.stackwork360.notificationservice.application;

public record DeliveryResult(
        boolean delivered,
        String providerReference,
        String failureReason
) {
    public static DeliveryResult delivered(String providerReference) {
        return new DeliveryResult(true, providerReference, null);
    }

    public static DeliveryResult failed(String failureReason) {
        return new DeliveryResult(false, null, failureReason);
    }
}
