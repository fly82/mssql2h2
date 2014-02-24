package model;

import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.Id;

import static javax.persistence.AccessType.*;

@Entity @Access(FIELD)
public class MapDB {
    @Id Long id;
    String name;
    String date;
    String address;
    String number;


    public MapDB() {
    }

    public MapDB(Long id, String name, String date, String address, String number) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.address = address;
        this.number = number;

    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", address='" + address + '\'' +
                ", number='" + number + '\'';
    }
}
