package com.trabalho.multi.trabalhomulti.position;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Table("data_by_vehicle")
public class DataByVehicle extends Position implements Serializable {
    private LocalDate dayBucket;

    public DataByVehicle() {

    }

    public DataByVehicle(Position position) {
        super(position);
        setDayBucket(position.getDate().toLocalDate());
    }

    @Column("day")
    @CassandraType(type = Name.DATE)
    @Indexed
    public LocalDate getDayBucket() {
        return this.dayBucket;

    }

    @Override
    @PrimaryKeyColumn(name = "datetime", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    @CassandraType(type = Name.TIMESTAMP)
    public LocalDateTime getDate() {

        return super.getDate();
    }

    @Override
    @JsonIgnore
    @Transient
    public UUID getProviderId() {
        // TODO Auto-generated method stub
        return super.getProviderId();
    }

    public void setDayBucket(LocalDate dayBucket) {
        this.dayBucket = dayBucket;
    }

}
