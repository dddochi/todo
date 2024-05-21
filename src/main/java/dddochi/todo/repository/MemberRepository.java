package dddochi.todo.repository;

import dddochi.todo.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    //create member
    public void save(Member member){
        em.persist(member);
    }

    //select all members
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //select a member
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    //find by name
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

    }

    //remove member
    public void deleteMember(Member member){
        em.remove(member);
    }
}
