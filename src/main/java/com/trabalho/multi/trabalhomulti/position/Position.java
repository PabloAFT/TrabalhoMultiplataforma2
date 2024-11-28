package com.trabalho.multi.trabalhomulti.position;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trabalho.multi.trabalhomulti.Attributes;
import com.trabalho.multi.trabalhomulti.vehicle.Vehicle;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

public class Position {
    // @CassandraType(type = Name.MAP)

    public enum DataType {
        STATUS, ALIVE, POSITION, ALERT, EMERGENCY
    }

    private Map<String, String> attributes = new HashMap<String, String>();

    private String type;

    @Transient
    private String vehicleName;

    @Transient
    private UUID driverId;

    @Transient
    private double course;

    private UUID vehicleId;

    private LocalDateTime date;

    private String imei;

    private double latitude;
    private double longitude;
    private double speed;

    private UUID providerId;

    public Position clone() {
        Position clonePosition = new Position();
        clonePosition.setAttributes(attributes);
        clonePosition.setDate(date);
        clonePosition.setImei(imei);
        clonePosition.setLatitude(latitude);
        clonePosition.setLongitude(longitude);
        clonePosition.setSpeed(speed);
        clonePosition.setType(type);
        clonePosition.setVehicleId(vehicleId);
        clonePosition.setVehicleName(vehicleName);
        return clonePosition;
    }

    public Position() {

    }

    public Position(Position position) {
        this.attributes = position.getAttributes();
        this.date = position.getDate();
        this.imei = position.getImei();
        this.latitude = position.getLatitude();
        this.longitude = position.getLongitude();
        this.speed = position.getSpeed();
        this.type = position.getType();
        this.vehicleId = position.getVehicleId();
        this.vehicleName = position.getVehicleName();
        this.providerId = position.getProviderId();

    }

    public static Position createPositionWithinRadius(Vehicle vehicle) {
        final double FORTALEZA_LAT = -3.859854;
        final double FORTALEZA_LON = -38.600087;
        final double RADIUS = 20.0;

        double radiusInDegrees = RADIUS / 111.32;
        double u = Math.random();
        double v = Math.random();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double randomLat = w * Math.cos(t);
        double randomLon = w * Math.sin(t) / Math.cos(Math.toRadians(FORTALEZA_LAT));

        double newLat = FORTALEZA_LAT + randomLat;
        double newLon = FORTALEZA_LON + randomLon;

        Position position = new Position();
        position.type = "VEHICLE_POSITION";
        position.vehicleId = vehicle.getId();
        position.date = LocalDateTime.now();
        position.imei = vehicle.getDeviceImei();
        position.latitude = newLat;
        position.longitude = newLon;
        position.speed = Math.random() * 100;
        position.providerId = UUID.randomUUID();

        return position;
    }

    @PrimaryKeyColumn(name = "vehicle_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = Name.UUID)
    public UUID getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getVehicleName() {
        return this.vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    @Transient
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column("attributes")
    @CassandraType(type = Name.MAP, typeArguments = { Name.TEXT, Name.TEXT })
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attributes key, String value) {
        attributes.put(key.getKey(), value);
    }

    public void addAttribute(Attributes key, Attributes value) {
        attributes.put(key.getKey(), value.getKey());
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public double getCourse() {
        return this.course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public void setProviderId(UUID providerId) {
        this.providerId = providerId;
    }

}
