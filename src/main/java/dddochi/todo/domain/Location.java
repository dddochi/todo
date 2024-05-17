package dddochi.todo.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Location {
    private double lat;
    private double lng;

    public Location(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }
}
