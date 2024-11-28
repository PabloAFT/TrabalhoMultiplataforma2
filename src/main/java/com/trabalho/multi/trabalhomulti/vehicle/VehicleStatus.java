package com.trabalho.multi.trabalhomulti.vehicle;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import com.trabalho.multi.trabalhomulti.position.Position;

@UserDefinedType("vehicle_status")
public class VehicleStatus {

    public VehicleStatus() {
    }

    public VehicleStatus(Position position) {
        this.battery = position.getAttributes().get("battery") != null
                ? Boolean.valueOf(position.getAttributes().get("battery"))
                : null;
        this.batteryLevel = position.getAttributes().get("battery_level") != null
                ? Double.valueOf(position.getAttributes().get("battery_level"))
                : null;
        this.odometer = position.getAttributes().get("odometer") != null
                ? Long.valueOf(position.getAttributes().get("odometer"))
                : 0;
        this.ignition = position.getAttributes().get("ignition") != null
                ? Boolean.valueOf(position.getAttributes().get("ignition"))
                : false;
        this.charge = position.getAttributes().get("charge") != null
                ? Boolean.valueOf(position.getAttributes().get("charge"))
                : null;
        this.power = position.getAttributes().get("power") != null
                ? Float.valueOf(position.getAttributes().get("power"))
                : null;
        this.gps = position.getAttributes().get("gps") != null
                ? Integer.valueOf(position.getAttributes().get("gps"))
                : null;
        this.inOut = parseInOut(position.getAttributes());

    }

    private double batteryLevel;
    private boolean battery;
    private long odometer;
    private boolean ignition;
    private boolean charge;
    private float power;
    private int gps;
    private Map<String, String> attributes;
    private Map<String, Boolean> inOut;

    public Map<String, Boolean> parseInOut(Map<String, String> attributes) {
        Map<String, Boolean> inOutMap = new HashMap<>();
        inOutMap.put("in1", attributes.get("in1") != null
                ? Boolean.valueOf(attributes.get("in1"))
                : null);
        inOutMap.put("in2", attributes.get("in2") != null
                ? Boolean.valueOf(attributes.get("in2"))
                : null);
        inOutMap.put("in3", attributes.get("in3") != null
                ? Boolean.valueOf(attributes.get("in3"))
                : null);
        inOutMap.put("out1", attributes.get("out1") != null
                ? Boolean.valueOf(attributes.get("out1"))
                : null);
        inOutMap.put("out2", attributes.get("out2") != null
                ? Boolean.valueOf(attributes.get("out2"))
                : null);
        inOutMap.put("out3", attributes.get("out3") != null
                ? Boolean.valueOf(attributes.get("out3"))
                : null);
     
        
        return null;

    }

    @Column("battery_level")
    public double getBatteryLevel() {

        return this.batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public boolean isBattery() {
        return this.battery;
    }

    public boolean getBattery() {
        return this.battery;
    }

    public void setBattery(boolean battery) {
        this.battery = battery;
    }

    public long getOdometer() {
        return this.odometer;
    }

    public void setOdometer(long odometer) {
        this.odometer = odometer;
    }

    public boolean isIgnition() {
        return this.ignition;
    }

    public boolean getIgnition() {
        return this.ignition;
    }

    public void setIgnition(boolean ignition) {
        this.ignition = ignition;
    }

    public boolean isCharge() {
        return this.charge;
    }

    public boolean getCharge() {
        return this.charge;
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

    public float getPower() {
        return this.power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public int getGps() {
        return this.gps;
    }

    public void setGps(int gps) {
        this.gps = gps;
    }

    @Column("in_out")
    public Map<String, Boolean> getInOut() {
        return this.inOut;
    }

    public void setInOut(Map<String, Boolean> inOut) {
        this.inOut = inOut;
    }

}
