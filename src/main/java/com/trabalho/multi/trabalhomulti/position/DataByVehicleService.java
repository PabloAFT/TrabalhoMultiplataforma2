package com.trabalho.multi.trabalhomulti.position;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.trabalho.multi.trabalhomulti.vehicle.Vehicle;
import com.trabalho.multi.trabalhomulti.vehicle.VehicleService;

@Service
public class DataByVehicleService {
    // @Autowired
    // private Storage storage;

    @Autowired
    DataByVehicleRepository dataByVehicleRepository;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    CacheManager cacheManager;

    public DataByVehicleList getDataByVehicleAndDay(UUID id, LocalDate date) {
        System.out.println("database fetch");

        if (date == null) {
            date = LocalDate.now();
        }

        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(23, 59, 00);

        DataByVehicleList data = new DataByVehicleList();
        data.addAll(dataByVehicleRepository.findDataByVehicleAndDateTime(id, startDate, endDate));
        return data;
    }

    public DataByVehicle getLastPositionByVehicleId(UUID id) {
        return dataByVehicleRepository.findLastDataByVehicle(id);
    }

    public List<DataByVehicle> getAllLastPositions() {
        return dataByVehicleRepository.findAllLastData();
    }

    public static class DataByVehicleList extends ArrayList<DataByVehicle> {
        public DataByVehicleList() {
        }
    }

    public DataByVehicle getFirstDataByVehicleAndDay(UUID id, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();

        }
        return dataByVehicleRepository.findFirstDataByVehicleAndDay(id, date);
    }

    public List<DataByVehicle> getDataByVehicleAndDateRange(UUID id, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> allDates = new ArrayList<LocalDate>();
        LocalDate currentDate = startDate;
        while (currentDate.isAfter(endDate)) {
            allDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dataByVehicleRepository.findDataByVehicleAndDayList(id, allDates);

    }

    public void generateMockData() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        for (Vehicle vehicle : vehicles) {
            Position position = Position.createPositionWithinRadius(vehicle);
            DataByVehicle dataByVehicle = new DataByVehicle(position);
            createDataByVehicle(dataByVehicle);

        }
    }

    public List<DataByVehicle> getDataByVehicleAndDateRange(List<UUID> id, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> allDates = new ArrayList<LocalDate>();
        LocalDate currentDate = startDate;
        while (currentDate.isAfter(endDate)) {
            allDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dataByVehicleRepository.findDataByVehicleListAndDayList(id, allDates);

    }

    public DataByVehicle createDataByVehicle(DataByVehicle dataByVehicle) {
        // this.updateCacheDataByVehiclesList(dataByVehicle);
        System.out.println(cacheManager.getCache("data_by_vehicles").getNativeCache());
        return dataByVehicleRepository.save(dataByVehicle);
    }

    public static class DataByVehicleListWrapper {
        private List<DataByVehicle> dataByVehicleList;

        public DataByVehicleListWrapper() {
            this.dataByVehicleList = null;
        }

        public DataByVehicleListWrapper(List<DataByVehicle> dataByVehicleList) {
            this.dataByVehicleList = dataByVehicleList;
        }

        public List<DataByVehicle> getDataByVehicleList() {
            return dataByVehicleList;
        }
    }

}
