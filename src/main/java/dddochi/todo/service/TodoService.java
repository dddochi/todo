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
    public Long addTodo(Long member_id,
                        String content,
                        String detail,
                        LocalDateTime createdAt,
                        TodoStatus todoStatus,
                        String placeName,
                        Location location
                        ){
        Member member = memberRepository.findOne(member_id);
        Todo todo = new Todo();
        todo.createTodo(member, content, detail,createdAt,todoStatus, placeName, location);
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
    //Todo - 업데이트
    @Transactional
    public void updateTodo(Long todo_id,
                           String content,
                           String detail,
                           TodoStatus todoStatus,
                           String placeName,
                           Location location){
        Todo todo = todoRepository.findOne(todo_id);
        todo.setContent(content);
        todo.setDetail(detail);
        todo.setStatus(todoStatus);
        todo.setPlaceName(placeName);
        todo.setLocation(location);
    }
    //Todo - 삭제
    @Transactional
    public void deleteTodo(Long todo_id){
        Todo todo = todoRepository.findOne(todo_id);
        todoRepository.deleteTodo(todo);

    }
}
