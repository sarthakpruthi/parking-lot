package com.practise.parking_lot.enums;

public enum Operation {
    CREATE_PARKING_LOT("create_parking_lot"), PARK_VEHICLE("park_vehicle"), UNPARK_VEHICLE("unpark_vehicle"),
    DISPLAY("display"),
    EXIT("exit"), INVALID_OPERATION("invalid_operation");
        String value;

    Operation(String val) {
        this.value = val;
    }

    public String getValue() {
        return value;
    }
}
