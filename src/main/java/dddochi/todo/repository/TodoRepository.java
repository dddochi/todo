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

    //find max turn
    public Integer findMaxTurn(){
        return  em.createQuery("select Max(t.turn) from Todo t", Integer.class).getSingleResult();

    }

    //순서가 todo -1 인 todo 찾기
    public Todo findTodoByTurn(int turn) {
        List<Todo> todos = em.createQuery("select t from Todo t where t.turn = :turn", Todo.class)
                .setParameter("turn", turn)
                .getResultList();
        if(todos.size() > 1) throw new IllegalStateException("중복되는 순서가 있습니다.");
        if(todos.isEmpty()) throw new IllegalStateException("해당 순서의 Todo가 없습니다");

        return todos.get(0);
    }
}
