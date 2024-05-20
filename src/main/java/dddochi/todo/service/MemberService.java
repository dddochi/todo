package dddochi.todo.service;

import dddochi.todo.domain.Location;
import dddochi.todo.domain.Member;
import dddochi.todo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //pending, completed 조회 하기

    //회원 가입 - 중복 check
    @Transactional
    public Long register(Member member){

        member.createMember(member.getName(), member.getHomeLocation());
        checkValidation(member.getName());
        memberRepository.save(member);
        return member.getId();

    }

    public void checkValidation(String name){
        List<Member> members = memberRepository.findByName(name);
        if(!members.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 조회
    public Member findMember(Long id){
        return memberRepository.findOne(id);
    }

    //모든 회원 조회
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    //회원 탈퇴
    @Transactional
    public void leave(Long id){
        Member member = memberRepository.findOne(id);
        memberRepository.deleteMember(member);
    }
}
