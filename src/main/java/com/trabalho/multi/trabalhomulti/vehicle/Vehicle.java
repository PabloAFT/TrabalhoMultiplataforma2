package com.trabalho.multi.trabalhomulti.vehicle;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.trabalho.multi.trabalhomulti.Attributes;
import com.trabalho.multi.trabalhomulti.position.Position;

@Component
@Table(value = "vehicle_by_id")
public class Vehicle {

    public Vehicle() {
        this.id = Uuids.timeBased();
    }

    public Vehicle(UUID id, String deviceImei, String deviceName, String maker, String model, int year,
            LocalDateTime lastUpdated, String driverName, String licensePlate, VehicleType vehicleType,
            String observation, Set<LocalDate> daysWithConnection, Set<LocalDate> daysOn,
            String color, Map<String, String> status) {
        if (id != null) {
            this.id = id;
        } else {
            this.id = Uuids.timeBased();
        }
        this.deviceImei = deviceImei;
        this.deviceName = deviceName;
        this.maker = maker;
        this.model = model;
        this.year = year;
        this.lastUpdated = lastUpdated;
        this.driverName = driverName;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.observation = observation;
        this.daysWithConnection = daysWithConnection;
        this.daysOn = daysOn;
        this.color = color;
        this.status = status;
    }

    public Vehicle setStatusFromPosition(Position position) {
        Map<String, String> status = this.getStatus();

        Map<String, String> attributes = position.getAttributes();

        status.putAll(attributes);
        status.put("speed", String.valueOf(position.getSpeed()));

        if (!attributes.containsKey(Attributes.KEY_ALARM.getKey())) {

        }
        return this;
    }

    public Vehicle updateDaysWithConnection(Vehicle vehicle) {

        Set<LocalDate> daysWithConnection = vehicle.getDaysWithConnection();

        if (!daysWithConnection.contains(LocalDate.now())) {
            daysWithConnection.add(LocalDate.now());
            vehicle.setDaysWithConnection(daysWithConnection);
        }
        return vehicle;
    }

    public Vehicle updateDaysOn(Vehicle vehicle, LocalDate day) {

        Set<LocalDate> daysOn = vehicle.getDaysOn();

        if (!daysOn.contains(day)) {
            daysOn.add(day);
            vehicle.setDaysOn(daysOn);
        }
        return vehicle;
    }

    private UUID id;

    private String deviceImei;

    private String deviceName;

    private String maker;

    private String model;

    private int year;

    private LocalDateTime lastUpdated;

    private String driverName;

    private String licensePlate;

    private VehicleType vehicleType;

    private String observation;

    private Set<LocalDate> daysWithConnection;

    private Set<LocalDate> daysOn;

    private String color;

    private Map<String, String> status;

    public Map<String, String> getStatus() {
        if (status == null) {
            this.status = new HashMap<>();
        }
        return status;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVehicleName() {
        return String.join(" ", getMaker(), getModel(), String.valueOf(getYear()));
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    @Id
    @PrimaryKeyColumn(name = "vehicle_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = Name.UUID)
    public UUID getId() {
        return id;
    }

    public String getMaker() {
        return maker;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    @Column("device_imei")
    @CassandraType(type = Name.TEXT)
    public String getDeviceImei() {
        return deviceImei;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Column("last_updated")
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Column("driver_name")
    public String getDriverName() {
        return this.driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Column("license_plate")
    public String getLicensePlate() {
        return this.licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    @Column("vehicle_type")
    public VehicleType getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getObservation() {
        return this.observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Column("device_name")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDaysOn(Set<LocalDate> daysOn) {
        this.daysOn = daysOn;
    }

    @Column("days_on_set")
    public Set<LocalDate> getDaysOn() {
        if (daysOn == null) {
            this.daysOn = new HashSet<LocalDate>();
        }
        return daysOn;
    }

    public void setDaysWithConnection(Set<LocalDate> daysWithConnection) {
        this.daysWithConnection = daysWithConnection;
    }

    @Column("days_with_connection_set")
    public Set<LocalDate> getDaysWithConnection() {
        if (daysWithConnection == null) {
            this.daysWithConnection = new HashSet<LocalDate>();
        }
        return daysWithConnection;
    }
}
