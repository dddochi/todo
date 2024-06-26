package dddochi.todo.service;

import dddochi.todo.domain.Location;
import dddochi.todo.domain.Member;
import dddochi.todo.domain.Todo;
import dddochi.todo.domain.TodoStatus;
import dddochi.todo.repository.MemberRepository;
import dddochi.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    //Todo - 생성
    @Transactional
    public Long postTodo(Long member_id,
                        String content,
                        String detail,
                        String placeName,
                        Location location,
                         LocalDate date
                        ){
        Member member = memberRepository.findOne(member_id);


        //순서 로직 추가하기
        //max값 조회 - findMaxTurn
        Integer maxTurn = todoRepository.findMaxTurn();

        int newTurn = (maxTurn == null) ? 1 : maxTurn + 1;

        //todo생성
        Todo todo = Todo.createTodo(member, content, detail,LocalDateTime.now(),TodoStatus.PENDING, placeName, location, newTurn, date);
        todoRepository.save(todo);
        return todo.getId();
    }
    //todo - 조회
    public Todo findTodo(Long todo_id){
        return todoRepository.findOne(todo_id);
    }
    public List<Todo> findAll(){
        return todoRepository.findAll();
    }
    //Todo - completed
    @Transactional
    public void completeTodo(Long id){
        Todo todo = todoRepository.findOne(id);
        todo.setStatus(TodoStatus.COMPLETED);
    }
    //Todo - 순서 increment
    @Transactional
    public void turnIncrement(Long id){
        Todo todo = todoRepository.findOne(id);

        if(todo.getTurn() == 1){ //defensive coding
            throw new IllegalStateException("순서가 1일 때는 Increment 할 수 없습니다.");
        }

        // 위에 있는 순서 하나 내리기
        //순서가 todo -1 인 todo 찾기
        Todo higherTodo = todoRepository.findTodoByTurn(todo.getTurn() - 1);
        higherTodo.setTurn(higherTodo.getTurn() + 1);

        //내 순서 하나 올리기
        todo.setTurn(todo.getTurn() - 1);
    }
    //Todo - 순서 decrement
    @Transactional
    public void turnDecrement(Long id)
    {
        Todo todo = todoRepository.findOne(id);

        Integer maxTurn = todoRepository.findMaxTurn();

        //max값일 때 내릴 수 없음
        if(todo.getTurn() == maxTurn){ //defensive coding
            throw new IllegalStateException("순서가 Max일 때는 Decrement 할 수 없습니다.");
        }


        // 아래에 있는 순서 하나 올리기
        //순서가 todo +1 인 todo 찾기
        Todo lowerTodo = todoRepository.findTodoByTurn(todo.getTurn() + 1);
        lowerTodo.setTurn(lowerTodo.getTurn() - 1);

        //내 순서 하나 내리기
        todo.setTurn(todo.getTurn() + 1);
    }

    //Todo - 업데이트
    @Transactional
    public void updateTodo(Long todo_id,
                           String content,
                           String detail,
                           String placeName,
                           Location location){
        Todo todo = todoRepository.findOne(todo_id);
        todo.setContent(content);
        todo.setDetail(detail);
        todo.setPlaceName(placeName);
        todo.setLocation(location);
    }
    //Todo - 삭제
    @Transactional
    public void deleteTodo(Long todo_id){
        Todo todo = todoRepository.findOne(todo_id);
        todoRepository.deleteTodo(todo);

    }

    //update - 위치
    @Transactional
    public void updatePlace(Long id, String placeName, Location location){
        Todo todo = todoRepository.findOne(id);
        todo.setPlaceName(placeName);
        todo.setLocation(location);
    }
}
