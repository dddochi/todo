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

    // id를 조회하면 -> turn값 리턴
    // turn이 하나 더 높은 애를 조회
    //각각 업데이트
    public void decreaseTurn(int turn){ //나의 턴 값보다 높은 레코드

        em.createQuery("update Todo t set t.turn = t.turn + 1 where t.turn = :turn")
                .setParameter("turn", turn);
    }

    //find max turn
    public Integer findMaxTurn(){
        return  em.createQuery("select Max(t.turn) from Todo t", Integer.class).getSingleResult();

    }

}
