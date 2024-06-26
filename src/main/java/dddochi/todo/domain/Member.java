package dddochi.todo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;

    //집 위치
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "home_lat")),
            @AttributeOverride(name = "lng", column = @Column(name = "home_lng"))
    })
    private Location homeLocation;

    //체크필요
    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();

    public void createMember(String name, Location homeLocation){
        this.name = name;
        this.homeLocation = homeLocation;
    }
}
