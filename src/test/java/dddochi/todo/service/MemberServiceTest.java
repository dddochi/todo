package dddochi.todo.service;

import dddochi.todo.domain.Location;
import dddochi.todo.domain.Member;
import dddochi.todo.repository.MemberRepository;
import jakarta.annotation.security.RunAs;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    //회원 가입 TEST
    @Test
    public void join(){
        Member member = new Member();
        member.setName("member");
        Location location = new Location(37.5668661, 126.9766639);
        member.setHomeLocation(location);
        memberService.register(member);
    }

    //중복 회원 에러 TEST
    @Test
    public void duplicatedJoin() throws Exception{
        Member member1 = new Member();
        member1.setName("member1");

        memberService.register(member1);

        Member member2 = new Member();
        member2.setName("member1");

        assertThrows(IllegalStateException.class,()->{
            memberService.register(member2);
        });
    }

    //회원 조회
    @Test
    public void findMember(){
        Member member = new Member();
        member.setName("member");
        member.setHomeLocation(new Location(37.5668661, 126.9766639));

        Long memberId = memberService.register(member);
        Member findMember = memberService.findMember(memberId);
        assertThat(member).isEqualTo(findMember);
    }

    //전체 회원 조회
    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("member1");
        memberService.register(member1);

        Member member2 = new Member();
        member2.setName("member2");
        memberService.register(member2);

        List<Member> memberList = memberService.findAll();

        assertThat(memberList.get(0)).isEqualTo(member1);
        assertThat(memberList.get(1)).isEqualTo(member2);
    }

    //회원 탈퇴
    @Test
    public void deleteMember(){
        Member member = new Member();
        member.setName("member");
        member.setHomeLocation(new Location(37.5668661, 126.9766639));
        //회원 가입
        memberService.register(member);

        //탈퇴
        memberService.leave(member.getId());

        //탈퇴한 아이디 서치 결과 == null
        assertThat(memberService.findMember(member.getId())).isEqualTo(null);

    }
}