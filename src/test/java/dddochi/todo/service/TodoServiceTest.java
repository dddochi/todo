package dddochi.todo.service;


import dddochi.todo.domain.Location;
import dddochi.todo.domain.Member;
import dddochi.todo.domain.Todo;
import dddochi.todo.domain.TodoStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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
        //
        //then
//        Order getOrder = orderRepository.findOne(orderId);
//        assertEquals("상품주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
//        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
//        assertEquals("주문가격은 가격 * 수량이다.", 10000*orderCount, getOrder.getTotalPrice());
//        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());
    }


    //todo 순서 increment
//    @Test
//    public void turnIncrement(){
//        //회원
//        Member member = new Member();
//        member.createMember("member1", new Location(1.0, 1.0));
//        memberService.register(member);
//
//        //todo1
//        Long todoOneId = todoService.postTodo(
//                member.getId(),
//                "산책하기",
//                "가는 길에 다이소 들리기",
//                LocalDateTime.now(), //createdAt -> 자동으로 찍히게 해야함 (아마 controller에서 구현 now함수)
//                TodoStatus.PENDING,
//                "석촌호수",
//                new Location(37.5113096, 127.1051525)
//        );
//        //todo2
//        Long todoTwoId = todoService.postTodo(
//                member.getId(),
//                "곱창 먹기",
//                "소주 한잔 곁들여서",
//                LocalDateTime.now(), //createdAt -> 자동으로 찍히게 해야함 (아마 controller에서 구현 now함수)
//                TodoStatus.PENDING,
//                "석촌호수",
//                new Location(37.5113096, 127.1051525)
//        );
//        //todo3
//        Long todoThreeId = todoService.postTodo(
//                member.getId(),
//                "꽃 사기",
//                "장미",
//                LocalDateTime.now(), //createdAt -> 자동으로 찍히게 해야함 (아마 controller에서 구현 now함수)
//                TodoStatus.PENDING,
//                "석촌호수",
//                new Location(37.5113096, 127.1051525)
//        );
//
//    }

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
                LocalDateTime.now(), //createdAt -> 자동으로 찍히게 해야함 (아마 controller에서 구현 now함수)
                TodoStatus.PENDING,
                placeName,
                new Location(37.5113096, 127.1051525)
        );
    }

    private Member createMember() {
        Member member = new Member();
        member.createMember("member", new Location(1.0, 1.0));
        memberService.register(member);
        return member;
    }
}