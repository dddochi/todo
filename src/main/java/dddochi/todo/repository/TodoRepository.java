package dddochi.todo.repository;

import dddochi.todo.domain.Todo;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoRepository {
    private final EntityManager em;

    //create todo
    public void save(Todo todo){
        em.persist(todo);
    }

    //find a todo
    public Todo findOne(Long id){
        return em.find(Todo.class, id);
    }

    //find all todos
    public List<Todo> findAll(){
        return em.createQuery("select t from Todo t", Todo.class)
                .getResultList();
    }

    //delete member
    public void deleteTodo(Todo todo){
        em.remove(todo);
    }

    //complete- todo -> service에서 구현하기
}
