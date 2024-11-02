package com.practise.parking_lot.service;

import com.practise.parking_lot.entity.*;
import com.practise.parking_lot.enums.Display;
import com.practise.parking_lot.enums.VehicleType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.practise.parking_lot.enums.Display.*;

@Service
public class ParkingService {
    Parking parking;
    Map<Floor, List<Vehicle>> floorVehicleMap;

    public void display(String[] input) {
        VehicleType vehicleType = VehicleType.valueOf(input[2]);
        Display displayType = Display.valueOf(input[1].toUpperCase());
        printFloorSlot(displayType, vehicleType);
    }

    private void printFloorSlot(Display displayType, VehicleType vehicleType) {
        AtomicInteger freeCount = new AtomicInteger();
        parking.getFloors().forEach(floor-> {
            displayHeading(displayType,floor.getNumber(),vehicleType);
            floor.getSlots().stream()
                    .filter(slot->slot.getVehicleType().equals(vehicleType))
                    .forEach(slot->{
                        if(slot.isOccupied() && OCCUPIED_SLOTS == displayType || !slot.isOccupied() && FREE_SLOTS == displayType){
                            System.out.print(slot.getNumber() +",");
                        }
                        else if(!slot.isOccupied()){
                            freeCount.getAndIncrement();
                        }
                    });
            if(displayType == FREE_COUNT){
                System.out.print(freeCount.get());
                freeCount.set(0);
            }
            System.out.println();
        });
    }

    private void displayHeading(Display displayType, int floor, VehicleType vehicleType) {
        switch (displayType){
            case FREE_COUNT:
                System.out.print("Number of free slots for " + vehicleType + " on Floor " + floor + " : ");
                break;
            case FREE_SLOTS:
                System.out.print("Free slots for " + vehicleType + " on Floor " + floor + " : ");
                break;
            case OCCUPIED_SLOTS:
                System.out.print("Occupied slots for " + vehicleType + " on Floor " + floor + " : ");
                break;
        }
    }

    public void unparkVehicle(String[] input) {
        String[] floorSlot = input[1].split("_");
        int unparkFloor = Integer.parseInt(floorSlot[1]), unparkSlot = Integer.parseInt(floorSlot[2]);
        if(!validateUnparkTicket(floorSlot[0], unparkFloor, unparkSlot)){
            return;
        }
        Slot parkedSlot = parking.getFloors().get(unparkFloor-1).getSlots().get(unparkSlot-1);
        System.out.println("Unparked vehicle with Registration Number: " + parkedSlot.getVehicle().getNumber() +
                " and color: " + parkedSlot.getVehicle().getColor());
        parkedSlot.setOccupied(false);
        parkedSlot.setVehicle(null);
    }

    private boolean validateUnparkTicket(String ticketId, int unparkFloor, int unparkSlot) {
        if(!parking.getId().equals(ticketId) ||
                parking.getFloors().size() < unparkFloor ||
                parking.getFloors().get(unparkFloor-1).getSlots().size() < unparkSlot
        ){
            System.out.println("Invalid Ticket");
            return false;
        }
        Slot parkedSlot = parking.getFloors().get(unparkFloor-1).getSlots().get(unparkSlot-1);
        if(!parkedSlot.isOccupied()){
            System.out.println("Invalid Ticket");
            return false;
        }
        return true;
    }


    public void parkVehicle(String[] input) {
        VehicleType vehicleType = VehicleType.valueOf(input[1]);
        AtomicReference<Integer> floorNumber = new AtomicReference<>(null);
        Optional<Slot> optionalSlot = parking.getFloors().stream()
                .flatMap(floor -> floor.getSlots().stream()
                .filter(s -> s.getVehicleType().equals(vehicleType) && !s.isOccupied())
                .peek(s-> floorNumber.set(floor.getNumber())))
                .findFirst();
        if(optionalSlot.isEmpty()){
            System.out.println("No parking slots available for " + vehicleType);
            return;
        }
        Slot slot = optionalSlot.get();
        Vehicle vehicle = Vehicle.builder()
                .number(input[2])
                .color(input[3])
                .type(vehicleType).build();
        slot.setVehicle(vehicle);
        slot.setOccupied(true);
        Ticket ticket = Ticket.builder()
                .id(parking.getId() + "_" + floorNumber + "_" + slot.getNumber())
                .slot(slot).build();
        System.out.println("Parked vehicle. Ticket ID: "+ ticket.getId());
    }

    public void createParking(String[] input) {
        String parkingId = input[1];
        int numFloors = Integer.parseInt(input[2]), slotsCount = Integer.parseInt(input[3]);
        parking = Parking.builder().id(parkingId)
                .floors(getFloors(numFloors, slotsCount))
                .build();
        System.out.println("Created parking lot with " + numFloors + " floors and " + slotsCount + " slots per floor");
    }

    private static List<Floor> getFloors(int numFloors, int slotsPerFloor) {
        List<Floor> floors = new ArrayList<>();
        for(int i = 1; i <= numFloors; i++) {
            List<Slot> slots = new ArrayList<>();
            for(int j = 1; j<= slotsPerFloor; j++){
                Slot slot = Slot.builder()
                        .number(j)
                        .occupied(false)
                        .build();
                if(j==1){
                    slot.setPrice(200);
                    slot.setVehicleType(VehicleType.TRUCK);
                }
                else if(j<4){
                    slot.setPrice(50);
                    slot.setVehicleType(VehicleType.BIKE);
                }
                else{
                    slot.setPrice(100);
                    slot.setVehicleType(VehicleType.CAR);
                }
                slots.add(slot);
            }
            Floor floor = Floor.builder()
                    .number(i)
                    .slots(slots)
                    .build();
            floors.add(floor);
        }
        return floors;
    }

    public void exit() {
        System.out.println("shutting down parking");
        System.exit(200);
    }
}
