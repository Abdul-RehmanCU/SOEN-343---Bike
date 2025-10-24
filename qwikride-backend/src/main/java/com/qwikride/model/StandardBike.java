package com.qwikride.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@DiscriminatorValue("STANDARD")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StandardBike extends Bike {

    public StandardBike(Long stationId) {
        setType(BikeType.STANDARD);
        setStationId(stationId);
        setStatus(BikeStatus.AVAILABLE);
    }

    @Override
    public boolean canCheckout() {
        return getStatus() == BikeStatus.AVAILABLE || getStatus() == BikeStatus.RESERVED;
    }

    @Override
    public boolean needsMaintenance() {
        return getLastMaintenance() == null ||
               getLastMaintenance().isBefore(LocalDateTime.now().minusMonths(3));
    }

    @Override
    public void performMaintenance() {
        setLastMaintenance(LocalDateTime.now());
        setStatus(BikeStatus.AVAILABLE);
    }
}
