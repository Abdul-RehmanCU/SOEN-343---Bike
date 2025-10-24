package com.qwikride.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bikes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "bike_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public abstract class Bike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "bike_type", insertable = false, updatable = false)
    private BikeType type;

    @Column(name = "station_id")
    private Long stationId;

    @Enumerated(EnumType.STRING)
    private BikeStatus status;

    @Column(name = "last_maintenance")
    private LocalDateTime lastMaintenance;

    @Column(name = "current_user_id")
    private Long currentUserId;

    @Column(name = "reserved_by_user_id")
    private Long reservedByUserId;

    @Column(name = "reservation_time")
    private LocalDateTime reservationTime;

    @Column(name = "reservation_expires_at")
    private LocalDateTime reservationExpiresAt;

    public abstract boolean canCheckout();
    public abstract boolean needsMaintenance();
    public abstract void performMaintenance();

    public void reserve(Long userId, int expiresAfterMinutes) {
        if (this.status == BikeStatus.AVAILABLE) {
            this.status = BikeStatus.RESERVED;
            this.reservedByUserId = userId;
            this.reservationTime = LocalDateTime.now();
            this.reservationExpiresAt = LocalDateTime.now().plusMinutes(expiresAfterMinutes);
        } else {
            throw new IllegalStateException("Bike is not available for reservation.");
        }
    }

    public void cancelReservation() {
        if (this.status == BikeStatus.RESERVED) {
            this.status = BikeStatus.AVAILABLE;
            this.reservedByUserId = null;
            this.reservationTime = null;
            this.reservationExpiresAt = null;
        }
    }

    public void checkout(Long userId) {
        if (canCheckout()) {
            this.status = BikeStatus.IN_USE;
            this.currentUserId = userId;
            this.reservedByUserId = null;
            this.reservationTime = null;
            this.reservationExpiresAt = null;
        } else {
            throw new IllegalStateException("Bike cannot be checked out.");
        }
    }

    public void returnBike(Long stationId) {
        if (this.status == BikeStatus.IN_USE) {
            this.status = BikeStatus.AVAILABLE;
            this.stationId = stationId;
            this.currentUserId = null;
        } else {
            throw new IllegalStateException("Bike is not currently in use.");
        }
    }

    public void moveToStation(Long newStationId) {
        if (this.status == BikeStatus.AVAILABLE || this.status == BikeStatus.MAINTENANCE) {
            this.stationId = newStationId;
        } else {
            throw new IllegalStateException("Bike cannot be moved while in use or reserved.");
        }
    }

    public boolean isReservationExpired() {
        return this.status == BikeStatus.RESERVED && 
               this.reservationExpiresAt != null && 
               LocalDateTime.now().isAfter(this.reservationExpiresAt);
    }
}
