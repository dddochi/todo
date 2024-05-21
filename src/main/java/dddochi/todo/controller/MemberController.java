package dddochi.todo.controller;

import dddochi.todo.domain.Location;
import dddochi.todo.domain.Member;
import dddochi.todo.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //회원 가입
    @PostMapping("/api/v1/member")
    public CreateMemberResponse createMember(@RequestBody CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        member.setHomeLocation(new Location(request.getLat(), request.getLng()));
        Long id = memberService.register(member);
        return new CreateMemberResponse(id);
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberRequest{
        String name;
        double lat;
        double lng;
    }
    @Data
    @AllArgsConstructor
    static class CreateMemberResponse{
        Long id;
    }

    //로그인 - (회원 조회) : '이름' 입력 하면 -> id, 집주소 반환
    @GetMapping("/api/v1/member/{id}")
    public ReadMemberResponse readMember(@PathVariable String name){
        List<Member> members = memberService.findMemberByName(name);
        Member member = members.get(0);
        return new ReadMemberResponse(member.getId(), member.getName(), member.getHomeLocation().getLat(), member.getHomeLocation().getLng());
    }
    @Data
    @AllArgsConstructor
    static class ReadMemberResponse{
        private Long id;
        private String name;
        private double lat;
        private double lng;
    }
    //이름 수정
    @PutMapping("/api/v1/update_name/{id}")
    public UpdateNameResponse updateName(@PathVariable(name = "id") Long id, @RequestBody UpdateNameRequest request){
        memberService.updateName(id, request.getName());
        Member member = memberService.findMember(id);
        return new UpdateNameResponse(member.getId(), member.getName());
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateNameRequest{
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateNameResponse{
        private Long id;
        private String name;
    }
    //집 위치 수정
    @PutMapping("/api/v1/update_home/{id}")
    public UpdateHomeResponse updateHome(@PathVariable(name = "id") Long id, @RequestBody UpdateHomeRequest request){
        memberService.updateHome(id, new Location(request.lat, request.lng));
        Member member = memberService.findMember(id);
        return new UpdateHomeResponse(member.getId(), member.getHomeLocation().getLat(), member.getHomeLocation().getLng());
    }
    @Data
    @AllArgsConstructor
    static class UpdateHomeRequest{
        private double lat;
        private double lng;
    }
    @Data
    @AllArgsConstructor
    static class UpdateHomeResponse{
        private Long id;
        private double lat;
        private double lng;
    }
    //탈퇴
    @DeleteMapping("/api/v1/member/{id}")
    public void deleteMember(@PathVariable Long id){
        memberService.leave(id);
    }




}
