package com.practise.parking_lot.controller;

import com.practise.parking_lot.enums.Operation;
import com.practise.parking_lot.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class ParkingController {

    ParkingService parkingService;

    public ParkingController(){
        parkingService = new ParkingService();
    }

    public void startParking() {
        Scanner s = new Scanner(System.in);
        while(true){
            String[] input = s.nextLine().split(" ");
            Operation operation = getOperation(input[0]);
            switch (operation){
                case CREATE_PARKING_LOT:
                    parkingService.createParking(input);
                    break;
                case PARK_VEHICLE:
                    parkingService.parkVehicle(input);
                    break;
                case UNPARK_VEHICLE:
                    parkingService.unparkVehicle(input);
                    break;
                case DISPLAY:
                    parkingService.display(input);
                    break;
                case EXIT:
                    parkingService.exit();
                    break;
                default:
                    break;
            }
        }
    }


    private Operation getOperation(String operation){
        try {
            return Operation.valueOf(operation.toUpperCase());
        }catch (Exception e){
            System.out.println("invalid operation : " + operation);
            return Operation.INVALID_OPERATION;
        }
    }

}
