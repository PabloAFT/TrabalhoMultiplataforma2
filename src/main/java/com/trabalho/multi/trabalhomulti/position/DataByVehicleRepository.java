package com.trabalho.multi.trabalhomulti.position;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.trabalho.multi.trabalhomulti.vehicle.Vehicle;

@Repository
public interface DataByVehicleRepository extends CassandraRepository<DataByVehicle, UUID> {
    @Query("SELECT * FROM data_by_vehicle WHERE vehicle_id=?0 AND day=?1")
    @Deprecated
    List<DataByVehicle> findDataByVehicleAndDay(UUID id, LocalDate date);

    @Query("SELECT * FROM data_by_vehicle WHERE vehicle_id=?0 AND day=?1 LIMIT 1")
    @Deprecated
    DataByVehicle findFirstDataByVehicleAndDay(UUID id, LocalDate date);

    @Query("SELECT * FROM data_by_vehicle WHERE vehicle_id IN ?0 AND day=?1")
    List<DataByVehicle> findDataByVehicleListAndDay(List<UUID> id, LocalDate date);

    @Query("SELECT * FROM data_by_vehicle WHERE vehicle_id=?0 LIMIT 1")
    DataByVehicle findLastDataByVehicle(UUID id);

    @Query("SELECT * FROM data_by_vehicle PER PARTITION LIMIT 1")
    List<DataByVehicle> findAllLastData();

    @Query("SELECT * FROM data_by_vehicle WHERE vehicle_id IN ?0 AND day IN ?1")
    List<DataByVehicle> findDataByVehicleListAndDayList(List<UUID> id, List<LocalDate> date);

    @Query("SELECT * FROM data_by_vehicle WHERE vehicle_id IN ?0 AND day=?1")
    List<DataByVehicle> findDataByVehicleAndDayList(UUID id, List<LocalDate> date);

    @Query("SELECT * FROM data_by_vehicle WHERE vehicle_id=?0 AND datetime >= ?1 AND datetime <= ?2")
    List<DataByVehicle> findDataByVehicleAndDateTime(UUID id, LocalDateTime startTime, LocalDateTime endTime);

    // @Query("SELECT * FROM data_by_vehilce")

}
