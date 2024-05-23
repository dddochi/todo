package dddochi.todo.controller;

import dddochi.todo.domain.Location;
import dddochi.todo.domain.Member;
import dddochi.todo.domain.Todo;
import dddochi.todo.domain.TodoStatus;
import dddochi.todo.repository.TodoRepository;
import dddochi.todo.service.TodoService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jakarta.persistence.FetchType.LAZY;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
    private final TodoRepository todoRepository;
    //todo 생성
    @PostMapping("/api/v1/todo")
    public CreateTodoResponse createTodo (@RequestBody CreateTodoRequest request){
        Long todoId = todoService.postTodo(
                request.getMemberId(),
                request.getContent(),
                request.getDetail(),
                request.getPlaceName(),
                new Location(request.getLat(), request.getLng()),
                request.getDate());
        return new CreateTodoResponse(todoId);
    }
//    {
//        "memberId": 1,
//            "content": "해인 카페 놀러가기",
//            "detail": "가는 길에 아이스림 사오기~",
//            "placeName": "기흥 호수",
//            "lat": 37.52,
//            "lng": 125.7,
//            "date": "2023-05-22"
//    }
//{
//    "name": "dain",
//        "lat": 1.5,
//        "lng": 1.5
//}
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateTodoResponse{
        Long todoId;
    }
    @Data
    @AllArgsConstructor
    static class CreateTodoRequest{
        Long memberId;
        String content;
        String detail;
        String placeName;
        double lat;
        double lng;
        LocalDate date;
    }
    //todo 조회
    @GetMapping("/api/v1/todo")
    public Result readTodo(){
        List<Todo> findTodos = todoService.findAll();
        List<TodoDto> collect = findTodos.stream().map(todo -> new TodoDto(
                todo.getId(),
                todo.getContent(),
                todo.getDetail(),
                todo.getCreatedAt(),
                todo.getStatus(),
                todo.getPlaceName(),
                todo.getLocation().getLat(),
                todo.getLocation().getLng(),
                todo.getDate(),
                todo.getTurn()
        )).collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class TodoDto{
        private Long id;
        private String content;
        private String detail;
        private LocalDateTime createdAt;
        private TodoStatus status;
        private String placeName;
        private double lat;
        private double lng;
        private LocalDate date;
        private int turn;
    }
    //todo 수정
    @PutMapping("/api/v1/todo/{id}")
    public UpdateTodoResponse updateTodo(@PathVariable(name = "id") Long id, @RequestBody UpdateTodoRequest request){
        todoService.updateTodo(id,
                request.getContent(),
                request.getDetail(),
                request.getPlaceName(),
                new Location(request.getLat(), request.getLng()));
        Todo todo = todoService.findTodo(id);
        return new UpdateTodoResponse(
                todo.getId(),
                todo.getContent(),
                todo.getDetail(),
                todo.getPlaceName(),
                todo.getLocation().getLat(),
                todo.getLocation().getLng(),
                todo.getDate()
        );
    }
    @Data
    @AllArgsConstructor
    static class UpdateTodoResponse{
        private Long id;
        private String content;
        private String detail;
        private String placeName;
        private double lat;
        private double lng;
        private LocalDate date;
    }
    @Data
    @AllArgsConstructor
    static class UpdateTodoRequest{
        private String content;
        private String detail;
        private String placeName;
        private double lat;
        private double lng;
        private LocalDate date;
    }
//    {
//        "content": "산책하기",
//            "detail": "아아 마시기~",
//            "placeName": "기흥 호수",
//            "lat": 37.52,
//            "lng": 125.7,
//            "date": "2023-05-23"
//    }
    //todo 수정 - 장소이름, 위치 수정
    @PutMapping("/api/v1/todo/update_place/{id}")
    public UpdatePlaceResponse updateTodo(@PathVariable(name = "id") Long id, @RequestBody UpdatePlaceRequest request){
        todoService.updatePlace(id, request.getPlaceName(), new Location(request.getLat(), request.getLng()));
        Todo todo = todoService.findTodo(id);
        return new UpdatePlaceResponse(todo.getId(), todo.getPlaceName(), todo.getLocation().getLat(), todo.getLocation().getLng());
    }
    @Data
    @AllArgsConstructor
    static class UpdatePlaceResponse{
        Long id;
        String placeName;
        double lat;
        double lng;
    }
    @Data
    @AllArgsConstructor
    static class UpdatePlaceRequest{
        private String placeName;
        private double lat;
        private double lng;
    }
    //todo - status : completed
    @PutMapping("/api/v1/todo/complete/{id}")
    public CompleteTodoResponse completeTodo(@PathVariable(name = "id") Long id){
        todoService.completeTodo(id);
        Todo todo = todoService.findTodo(id);
        return new CompleteTodoResponse(todo.getId(), todo.getStatus());

    }
    @Data
    @AllArgsConstructor
    static class CompleteTodoResponse{
        private Long id;
        private TodoStatus status;
    }
    //todo - turn : increment
    @PutMapping("/api/v1/todo/increase/{id}")
    public IncreaseTodoTurn increaseTodoTurn(@PathVariable(name = "id") Long id){
        todoService.turnIncrement(id);
        Todo increasedTodo = todoService.findTodo(id);
        Todo decreasedTodo = todoRepository.findTodoByTurn(increasedTodo.getTurn() + 1);
        return new IncreaseTodoTurn(
                increasedTodo.getId(),
                increasedTodo.getTurn(),
                decreasedTodo.getId(),
                decreasedTodo.getTurn()
        );
    }
    @Data
    @AllArgsConstructor
    static class IncreaseTodoTurn{
        private Long IncreasedId;
        private int IncreasedTurn;
        private Long decreasedId;
        private int decreasedTurn;
    }
    //todo - turn : decrement
    @PutMapping("/api/v1/todo/decrease/{id}")
    public DecreaseTodoTurn decreaseTodoTurn(@PathVariable(name = "id") Long id){
        todoService.turnDecrement(id);
        Todo decreasedTodo = todoService.findTodo(id);
        Todo increasedTodo = todoRepository.findTodoByTurn(decreasedTodo.getTurn() - 1);
        return new DecreaseTodoTurn(
                decreasedTodo.getId(),
                decreasedTodo.getTurn(),
                increasedTodo.getId(),
                increasedTodo.getTurn()
        );
    }
    @Data
    @AllArgsConstructor
    static class DecreaseTodoTurn{
        private Long decreasedId;
        private int decreasedTurn;
        private Long increasedId;
        private int increasedTurn;
    }
}
