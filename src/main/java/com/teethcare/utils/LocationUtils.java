package com.teethcare.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocationUtils {
    public static double distance(double lat1, double lat2, double lon1, double lon2)
    {
        log.info("Lat input: " + lat1);
        log.info("Lat clinic: " + lat2);
        log.info("Long input: " + lon1);
        log.info("Long clinic: " + lon2);

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
//        Distance, d (miles) = 3963.0 * arccos[(sin(lat1) * sin(lat2)) + cos(lat1) * cos(lat2) * cos(long2 – long1)]
//        d in kilometers = 1.609344 * d in miles
        double differenceLong = lon2 - lon1;
        double differenceLat = lat2 - lat1;
        double a = Math.pow(Math.sin(differenceLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(differenceLong / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result

        log.info("Distance: " + c*r);
        return(c * r);
    }

    public static void main(String[] args) {
        double lat1 = 10.8472086;
        double lat2 = 10.8471035;
        double lon1 = 106.7998789;
        double lon2 = 106.7765438;
        System.out.println(distance(lat1, lat2,
                lon1, lon2) + " K.M");
    }
}
