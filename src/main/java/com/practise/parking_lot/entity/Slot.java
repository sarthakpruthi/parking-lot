package com.practise.parking_lot.entity;

import com.practise.parking_lot.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {
    int number;
    VehicleType vehicleType;
    boolean occupied;
    int price;
    Vehicle vehicle;
}
