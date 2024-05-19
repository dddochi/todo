package dddochi.todo.domain;

import ch.qos.logback.core.status.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {
    @Id @GeneratedValue
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") //foreign key - member_id
    private Member member;

    private String content;

    private String detail;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private TodoStatus status;

    private String placeName;

    @Embedded
    private Location location;

    private Date date; //todo 작성 날짜 선택
    private int turn; //순서 //auto-generate로 해줘야함! 그날 todo db에 들어오는 순서대로 넣어줘야함 - createdAt 활용? no 순서 user가 바꿀 수 있음
    //front에서 순서 바뀌면 바뀐 순서대로 넣어주기


    public static Todo createTodo(Member member,
                           String content,
                           String detail,
                           LocalDateTime createdAt,
                           TodoStatus status,
                           String placeName,
                           Location location){
        Todo todo = new Todo();
        todo.setMember(member);
        todo.setContent(content);
        todo.setDetail(detail);
        todo.setCreatedAt(createdAt);
        todo.setStatus(status);
        todo.setPlaceName(placeName);
        todo.setLocation(location);
        return todo;
    }

}
