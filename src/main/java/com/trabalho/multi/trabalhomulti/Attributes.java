package com.trabalho.multi.trabalhomulti;

public enum Attributes {
    KEY_ALT("alert"),
    KEY_SATELLITES("satellites"),
    KEY_GPS("gps"),//in percent
    KEY_GPRS("gprs"), //in percent
    KEY_EVENT("event"),
    KEY_ALARM("alarm"),
    KEY_STATUS("status"),
    KEY_ODOMETER("odometer"),

    KEY_ON("on"),
    KEY_OFF("off"),


    KEY_POWER("power"), // volts
    KEY_HOURS("hours"),
    KEY_BATTERY("battery"), // volts
    KEY_BATTERY_LEVEL("battery_level"), // percentage
    KEY_FUEL_LEVEL("fuel"), // liters

    KEY_CHARGE("charge"), // 
    KEY_BLOCKED("blocked"),

    KEY_IGNITION("ignition"),
    KEY_IN("in"),
    KEY_OUT("out"),

    KEY_RESPONSE("response"),
    KEY_INDEX("index"),
    KEY_ARCHIVE("archive"),
    KEY_ALIVE("alive"),
    KEY_LAST_COMMUNICATION("last_communication"),


    RECEIVED_BY_SERVER_AT("received_by_server_at"),
    // KEY_KEY(KEY_ALARM.getKey(),KEY_ALT.getKey()),


    ALARM_OVERSPEED("overspeed"),
    ALARM_GEOFENCE_EXIT("geofence_exit"),
    ALARM_GEOFENCE_ENTER("geofence_enter"),
    ALARM_SOS("sos"),
    ALARM_PARKING_LOCK("parking_lock"),
    ALARM_POWER_CUT("power_cut"),
    ALARM_ANTI_THEFT("anti-theft_alarm"),
    ALARM_SHOCK("shock"),
    ALARM_LOW_BATTERY("low_battery")


    ;

    private String key;

    Attributes(String key){
        this.key = key;
    }
    Attributes(String key,String description){

    }
    
    public String getKey() {
        return key;
    }
}
