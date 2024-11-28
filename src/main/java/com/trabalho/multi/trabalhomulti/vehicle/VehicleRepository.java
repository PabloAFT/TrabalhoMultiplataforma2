package com.trabalho.multi.trabalhomulti.vehicle;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface VehicleRepository extends CassandraRepository<Vehicle,UUID>{
    @Query("SELECT * FROM vehicle_by_id WHERE device_imei=?0 LIMIT 1")
    Vehicle findByDeviceImei(String deviceImei);
    @Query("SELECT device_imei FROM vehicle_by_id WHERE id=?0 ")
    String findDeviceImeiById(UUID id);

    @Query("SELECT last_updated FROM vehicle_by_id WHERE id=?0 LIMIT 1")
    LocalDate findDateById(UUID id);

    @Query("SELECT * FROM vehicle_by_id WHERE id=?0 LIMIT 1")
    Vehicle findVehicleById(UUID id);

}
