package com.practise.parking_lot;

import com.practise.parking_lot.controller.ParkingController;
import com.practise.parking_lot.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class ParkingLotApplication {

	public static void main(String[] args) {
		ParkingController parkingController = new ParkingController();
		parkingController.startParking();
	}
}
