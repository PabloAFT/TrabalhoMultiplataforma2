package com.trabalho.multi.trabalhomulti.vehicle;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.DefaultBatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.Node;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.update.UpdateStart;
import com.datastax.oss.driver.api.querybuilder.update.UpdateWithAssignments;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.google.api.services.storage.Storage.BucketAccessControls.Update;
import com.trabalho.multi.trabalhomulti.position.Position;

import io.micrometer.common.util.StringUtils;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepository;

    private CqlSession session;

    public VehicleService(VehicleRepository vehicleRepository, CqlSession session) {
        this.vehicleRepository = vehicleRepository;
        this.session = session;
        // QueryBuilder.literal("session"))
        // .whereColumn("null").isEqualTo(QueryBuilder.literal(session)).build());
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @CacheEvict(value = { "lastUpdated" }, key = "#id")
    public Vehicle updateVehicleFromController(UUID id, Vehicle updatedVehicle) {
        updatedVehicle.setId(id);

        if (StringUtils.isBlank(updatedVehicle.getDeviceImei())) {
            updatedVehicle.setDeviceName(null);
        }

        return vehicleRepository.save(updatedVehicle);

    }

    public Vehicle updateVehicle(UUID id, Vehicle updatedVehicle) {
        updatedVehicle.setId(id);
        if (StringUtils.isBlank(updatedVehicle.getDeviceImei())) {
            updatedVehicle.setDeviceName(null);
        }
        return vehicleRepository.save(updatedVehicle);
    }

    // Better naming.
    public Vehicle updateStatus(Vehicle vehicle) {

        return vehicleRepository.save(vehicle);
    }

    @Deprecated
    public Vehicle updateStatus(Position position, UUID vehicleId) throws JsonProcessingException {
        // VehicleStatus vehicleStatus = new VehicleStatus(position);
        // ObjectWriter objectWriter = new
        // ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        // .writer();
        // String statusJson = objectWriter.writeValueAsString(vehicleStatus);
        // ByteBuffer bb =
        // TypeCodecs.TIMESTAMP.encode(LocalDateTime.now().toInstant(ZoneOffset.UTC),
        // session.getContext().getProtocolVersion());

        UpdateWithAssignments update = QueryBuilder.update("vehicle_by_id").setColumn("last_updated",
                QueryBuilder.currentTimestamp());
        ;
        if (!position.getAttributes().containsKey("alert")) {
            update.setMapValue("status", QueryBuilder.literal("alert"), QueryBuilder.literal(null));
        }

        // session.getContext().getCodecRegistry().codecFor(VehicleStatus.class).encode(vehicleStatus,
        // session.getContext().getProtocolVersion());

        for (Map.Entry<String, String> entry : position.getAttributes().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Add setMapValue for each entry in the newMapValues
            update = update.setMapValue("status", QueryBuilder.literal(key), QueryBuilder.literal(value));
        }
        update.setMapValue("status", QueryBuilder.literal("speed"),
                QueryBuilder.literal(String.valueOf(position.getSpeed())));

        SimpleStatement simpleStatement = update.whereColumn("id").isEqualTo(QueryBuilder.literal(vehicleId)).build();
        session.execute(simpleStatement);

        return null;
    }

    private boolean externalRefToVehicleChanged(Vehicle oldVehicle, Vehicle newVehicle) {
        if (!oldVehicle.getModel().equals(newVehicle.getModel())) {
            return true;

        }
        if (!oldVehicle.getMaker().equals(newVehicle.getMaker())) {
            return true;

        }
        if (oldVehicle.getYear() != newVehicle.getYear()) {
            return true;

        }
        if (!oldVehicle.getLicensePlate().equals(newVehicle.getLicensePlate())) {
            return true;

        }

        return false;
    }

    @Cacheable("lastUpdated")
    public LocalDate getLastUpdated(UUID id) {
        return vehicleRepository.findDateById(id);
    }

    public Vehicle getVehicleById(UUID id) {
        return vehicleRepository.findVehicleById(id);
    }

    public Vehicle createVehicle(Vehicle vehicle) throws JsonProcessingException {

        if (vehicleRepository.existsById(vehicle.getId())) {
            throw new Error("vehicle already exists");
        }

        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Vehicle vehicle) {

        SimpleStatement fetchUsersByVehicle = QueryBuilder.selectFrom("user_by_vehicle").column("user_id")
                .whereColumn("vehicle_id").isEqualTo(QueryBuilder.literal(vehicle.getId())).build();
        ResultSet rs = session.execute(fetchUsersByVehicle);
        BatchStatementBuilder batchStatement = BatchStatement.builder(DefaultBatchType.LOGGED);
        SimpleStatement deleteUsersByVehicle = QueryBuilder.deleteFrom("user_by_vehicle").whereColumn("vehicle_id")
                .isEqualTo(QueryBuilder.literal(vehicle.getId())).build();
        SimpleStatement deleteVehicle = QueryBuilder.deleteFrom("vehicle_by_id").whereColumn("id")
                .isEqualTo(QueryBuilder.literal(vehicle.getId())).build();
        batchStatement.addStatement(deleteUsersByVehicle);
        batchStatement.addStatement(deleteVehicle);

        SimpleStatement deleteLastPositionStatement = QueryBuilder.deleteFrom("last_position")
                .whereColumn("vehicle_id").isEqualTo(QueryBuilder.literal(vehicle.getId()))
                .build();

        batchStatement.addStatement(deleteLastPositionStatement);

        for (Row row : rs) {

            SimpleStatement deleteVehicleByUser = QueryBuilder.deleteFrom("vehicle_by_user").whereColumn("user_id")
                    .isEqualTo(QueryBuilder.literal(row.getUuid("user_id")))
                    .whereColumn("vehicle_id").isEqualTo(QueryBuilder.literal(vehicle.getId()))
                    .whereColumn("maker").isEqualTo(QueryBuilder.literal(vehicle.getMaker()))
                    .whereColumn("model").isEqualTo(QueryBuilder.literal(vehicle.getModel()))
                    .whereColumn("year").isEqualTo(QueryBuilder.literal(vehicle.getYear()))
                    .build();
            batchStatement.addStatement(deleteVehicleByUser);
        }
        ResultSet result = session.execute(batchStatement.build());

        for (Entry<Node, Throwable> row : result.getExecutionInfo().getErrors()) {
            System.out.println(row.getKey());
            System.out.println(row.getValue());
        }

        // vehicleRepository.delete(vehicle);
    }

    public void deleteVehicleById(UUID vehicleId) {
        deleteVehicle(getVehicleById(vehicleId));
        // vehicleRepository.deleteById(vehicleId);
    }

    public void deleteAllById(List<UUID> vehicleIds) {

        vehicleRepository.deleteAllById(vehicleIds);
    }

    abstract class VehicleMixIn {
        VehicleMixIn(@JsonProperty("vehicle_id") UUID id) {
        }

        @JsonProperty("vehicle_id")
        abstract UUID getId();

        @JsonIgnore()
        abstract UUID getUserId();

        @JsonIgnore
        abstract UUID getSecondId();

    }

    abstract class UserMixIn {
        UserMixIn(@JsonProperty("user_id") UUID id) {
        }

        @JsonProperty("user_id")
        abstract UUID getId();

        @JsonProperty("vehicle_id")
        abstract UUID getVehicleId();

        @JsonIgnore
        abstract UUID getSecondId();

    }
}
