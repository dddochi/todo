package dddochi.todo.service;


import dddochi.todo.domain.Location;
import dddochi.todo.domain.Member;
import dddochi.todo.domain.Todo;
import dddochi.todo.domain.TodoStatus;
import dddochi.todo.repository.TodoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class TodoServiceTest {
    @Autowired
    TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    MemberService memberService;
    //todo 생성
    @Test
    public void addTodo(){
        Member member = createMember();

        Long todoId = createTodo(member, "산책하기", "가는 길에 다이소 들리기", "석촌호수");

        Todo findTodo = todoService.findTodo(todoId);

        assertThat(findTodo.getId()).isEqualTo(todoId);
        //todo 생성했을 때 - STATUS = PENDING이어야 한다
        assertEquals(TodoStatus.PENDING,findTodo.getStatus());

        //todo 생성했을 때 - Location(위치)가 정확해야한다.
        assertEquals( 37.5113096,findTodo.getLocation().getLat());
        assertEquals( 127.1051525,findTodo.getLocation().getLng());


        //todo 생성했을 때 - Content
        assertEquals( "산책하기",findTodo.getContent());
        //todo 생성했을 때 - Detail
        assertEquals("가는 길에 다이소 들리기", findTodo.getDetail());
        //todo 생성했을 때 - PalceName
        assertEquals("석촌호수", findTodo.getPlaceName());
        //todo 생성했을 때 - Turn은 1이어야 한다.
        assertEquals(1, findTodo.getTurn());
    }


    //todo 순서 increment
    @Test
    public void turnIncrement(){
        //회원
        Member member = new Member();
        member.createMember("member1", new Location(1.0, 1.0));
        memberService.register(member);

        //todo1
        Long todo1_id = todoService.postTodo(
                member.getId(),
                "산책하기",
                "가는 길에 다이소 들리기",
                "석촌호수",
                new Location(37.5113096, 127.1051525),
                LocalDate.of(2024, 5, 22)
        );
        //todo2
        Long todo2_id = todoService.postTodo(
                member.getId(),
                "곱창 먹기",
                "소주 한잔 곁들여서",

                "석촌호수",
                new Location(37.5113096, 127.1051525),
                LocalDate.of(2024, 5, 22)
        );
        //todo3
        Long todo3_id = todoService.postTodo(
                member.getId(),
                "꽃 사기",
                "장미",
                "석촌호수",
                new Location(37.5113096, 127.1051525),
                LocalDate.of(2024, 5, 22)
        );

        Todo todo1 = todoService.findTodo(todo1_id);
        Todo todo2 = todoService.findTodo(todo2_id);
        Todo todo3 = todoService.findTodo(todo3_id);

        //원래 순서 todo1 = 1, todo2 = 2,todo3 = 3
        assertEquals(1, todo1.getTurn());
        assertEquals(2, todo2.getTurn());
        assertEquals(3, todo3.getTurn());

        //순서 Increment : 3 -> 2
        todoService.turnIncrement(todo3_id);

        Todo updatedTodo3 = todoService.findTodo(todo3_id);
        Todo updatedTodo2 = todoService.findTodo(todo2_id);

        assertThat(2).isEqualTo(updatedTodo3.getTurn());
        assertThat(3).isEqualTo(updatedTodo2.getTurn());

    }
    //todo 순서 decrement
    @Test
    public void turnDecrement(){
        //회원
        Member member = new Member();
        member.createMember("member1", new Location(1.0, 1.0));
        memberService.register(member);

        //todo1
        Long todo1_id = todoService.postTodo(
                member.getId(),
                "산책하기",
                "가는 길에 다이소 들리기",
                "석촌호수",
                new Location(37.5113096, 127.1051525),
                LocalDate.of(2024, 5, 22)
        );
        //todo2
        Long todo2_id = todoService.postTodo(
                member.getId(),
                "곱창 먹기",
                "소주 한잔 곁들여서",
                "석촌호수",
                new Location(37.5113096, 127.1051525),
                LocalDate.of(2024, 5, 22)
        );
        //todo3
        Long todo3_id = todoService.postTodo(
                member.getId(),
                "꽃 사기",
                "장미",
                "석촌호수",
                new Location(37.5113096, 127.1051525),
                LocalDate.of(2024, 5, 22)
        );

        Todo todo1 = todoService.findTodo(todo1_id);
        Todo todo2 = todoService.findTodo(todo2_id);
        Todo todo3 = todoService.findTodo(todo3_id);

        //원래 순서 todo1 = 1, todo2 = 2,todo3 = 3
        assertEquals(1, todo1.getTurn());
        assertEquals(2, todo2.getTurn());
        assertEquals(3, todo3.getTurn());

        //순서 Decrement : 1 -> 2
        todoService.turnDecrement(todo1_id);

        Todo updatedTodo1 = todoService.findTodo(todo1_id);
        Todo updatedTodo2 = todoService.findTodo(todo2_id);

        assertThat(2).isEqualTo(updatedTodo1.getTurn());
        assertThat(1).isEqualTo(updatedTodo2.getTurn());

    }
    //todo 완료 - compeleted
    @Test
    public void completedTodo(){
        Member member = createMember();

        Long todoId = createTodo(member, "산책하기", "가는 길에 다이소 들리기", "석촌호수");

        todoService.completeTodo(todoId);
        Todo updatedTodo = todoService.findTodo(todoId);
        assertThat(TodoStatus.COMPLETED).isEqualTo(updatedTodo.getStatus());
    }

    //todo 삭제
    @Test
    public void deleteTodo(){
        Member member = createMember();

        Long todoId = createTodo(member, "산책하기", "가는 길에 다이소 들리기", "석촌호수");

        todoService.deleteTodo(todoId);

        Todo todo = todoService.findTodo(todoId);
       assertThat(todo).isEqualTo(null);
    }

    private Long createTodo(Member member, String content, String detail, String placeName) {
        return todoService.postTodo(
                member.getId(),
                content,
                detail,
                placeName,
                new Location(37.5113096, 127.1051525),
                LocalDate.of(2024, 5, 22)
        );
    }

    private Member createMember() {
        Member member = new Member();
        member.createMember("member", new Location(1.0, 1.0));
        memberService.register(member);
        return member;
    }

}