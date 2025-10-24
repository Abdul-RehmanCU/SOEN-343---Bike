package com.qwikride.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@DiscriminatorValue("E_BIKE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EBike extends Bike {

    @Column(name = "battery_level")
    private int batteryLevel = 100;

    public EBike(Long stationId) {
        setType(BikeType.E_BIKE);
        setStationId(stationId);
        setStatus(BikeStatus.AVAILABLE);
        this.batteryLevel = 100;
    }

    @Override
    public boolean canCheckout() {
        return (getStatus() == BikeStatus.AVAILABLE || getStatus() == BikeStatus.RESERVED)
               && batteryLevel >= 20; // Minimum battery level for checkout
    }

    @Override
    public boolean needsMaintenance() {
        return getLastMaintenance() == null ||
               getLastMaintenance().isBefore(LocalDateTime.now().minusMonths(2)) ||
               batteryLevel < 10; // Battery needs charging
    }

    @Override
    public void performMaintenance() {
        setLastMaintenance(LocalDateTime.now());
        this.batteryLevel = 100; // Charge battery during maintenance
        setStatus(BikeStatus.AVAILABLE);
    }

    public void chargeBattery() {
        this.batteryLevel = 100;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
}
