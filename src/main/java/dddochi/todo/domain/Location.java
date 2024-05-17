package dddochi.todo.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
public class Location {
    private double lat;
    private double lng;

    //값 타입 -> immutable -> setter 아예제공 x -> 기본 생성자 필요 but 못부르게 protected
    protected Location() {
    }

    public Location(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

}
