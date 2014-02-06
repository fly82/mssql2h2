package models;

public class Location {
    public float latitude;
    public float longitude;

    public Location() {
    }

    public Location(double latitude, double longitude) {
        this.latitude = (float) latitude;
        this.longitude = (float) longitude;
    }

    @Override
    public String toString() {
        return "{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
