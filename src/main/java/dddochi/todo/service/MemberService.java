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

    //회원 가입 - 중복 check
    @Transactional
    public Long register(String name, Location location){
        Member member = new Member();
        member.createMember(name, location);
        if(checkValidation(name)){ //이름 중복 허용 x
            memberRepository.save(member);
        }
        return member.getId();

    }

    public boolean checkValidation(String name){
        Member memberByName = memberRepository.findByName(name);
        if(memberByName.getName().equals(name)) return false;
        return true;
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
