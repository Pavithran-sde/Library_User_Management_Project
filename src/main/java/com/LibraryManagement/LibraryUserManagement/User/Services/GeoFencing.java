package com.LibraryManagement.LibraryUserManagement.User.Services;

import org.springframework.stereotype.Service;

@Service
public class GeoFencing {

    public boolean isUserNearResource(double userLat, double userLng, double tableLat, double tableLng, double radiusMeters) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(tableLat - userLat);
        double dLng = Math.toRadians(tableLng - userLng);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(tableLat))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;
        System.out.println(distance);
        return distance <= radiusMeters;
    }

}
