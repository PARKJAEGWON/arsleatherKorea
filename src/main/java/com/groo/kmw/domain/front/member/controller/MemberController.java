package com.groo.kmw.domain.front.member.controller;

import com.groo.kmw.domain.front.member.dto.request.MemberLoginRequest;
import com.groo.kmw.domain.front.member.dto.request.MemberRestoreRequest;
import com.groo.kmw.domain.front.member.dto.request.MemberSignupRequest;
import com.groo.kmw.domain.front.member.dto.request.MemberUpdateRequest;
import com.groo.kmw.domain.front.member.entity.Member;
import com.groo.kmw.domain.front.member.service.MemberService;
import com.groo.kmw.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    //회원가입 페이지 이동
    @GetMapping("/signup")
    public String signupPage(){
        return "front/member/signup";
    }
    //회원가입
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberSignupRequest memberSignupRequest, BindingResult bindingResult, HttpServletRequest httpServletRequest){

        //바인딩리절트를 쓰지않으면 에러시 에러페이지로 넘어감 그래서 바인딩지절트로 에러를 담아서 에러를 if로 잡고 에러시에 signup폼 그대로 남게함
        if(bindingResult.hasErrors()){
            return "front/member/signup";
        }
        try{
            this.memberService.signup(
                    memberSignupRequest.getMemberLoginId(),
                    memberSignupRequest.getMemberPassword(),
                    memberSignupRequest.getMemberName(),
                    memberSignupRequest.getMemberPhone(),
                    memberSignupRequest.getMemberEmail(),
                    memberSignupRequest.getMemberBirthDate(),
                    memberSignupRequest.getMemberGender(),
                    memberSignupRequest.getMemberZipCode(),
                    memberSignupRequest.getMemberAddress1(),
                    memberSignupRequest.getMemberAddress2(),
                    //boolean은 get이 아니고 is사용 자바의 bean규칙
                    memberSignupRequest.isMemberEmailAgree(),
                    memberSignupRequest.isMemberSmsAgree()
            );
            return "redirect:/member/login";
        } catch (IllegalArgumentException e) {
            httpServletRequest.setAttribute("signupError",e.getMessage());
            return "front/member/signup";
        }
    }

    //내 계정
    @GetMapping("/my")
    public String my(HttpServletRequest httpServletRequest, Model model){
        Cookie[] cookies = httpServletRequest.getCookies();
        String memberAccessToken = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    memberAccessToken = cookie.getValue();
                    break;
                }
            }
        }
        if(memberAccessToken == null){
            return "redirect:/member/login";
        }

        //jwt는 json을 디폴트로 함 디코딩이 필요함
        Map<String,Object> claims = jwtProvider.getClaims(memberAccessToken);
        Long memberId = ((Integer)claims.get("memberId")).longValue();
        Member member = memberService.findById(memberId);

        //add어튜리뷰트로 jsp에서 "member"속성명을 만들어서 "member" 이름으로 memeber 객체에 접근할 수 있게 해줌
        model.addAttribute("member",member);

        return "front/member/my";
    }

//    회원 정보 가져오기
    @GetMapping("/profile")
    public String profile(HttpServletRequest httpServletRequest, Model model){
        Cookie[] cookies = httpServletRequest.getCookies();

        String memberAccessToken = null;
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("memberAccessToken")) {
                    memberAccessToken = cookie.getValue();
                    break;
                }
            }
        }
        if(memberAccessToken == null) {
            return "redirect:/member/login";
        }

        Map<String, Object> claims = jwtProvider.getClaims(memberAccessToken);
        //두단계 캐스팅 인테이저로 변환하고 롱으로 또 변환 그래서 괄호 추가
        Long memberId = ((Integer)claims.get("memberId")).longValue();
        Member member = memberService.findById(memberId);

        model.addAttribute("member", member);

        return "front/member/profile";
    }

    //회원정보 수정
    @PostMapping("/profile")
    public String update(@Valid @ModelAttribute MemberUpdateRequest memberUpdateRequest, BindingResult bindingResult, HttpServletRequest httpServletRequest){

            //일단 이걸 쓰고 나중에 prg패턴 공부해서 리펙토링
        if(bindingResult.hasErrors()){
            //dto의 검증 메세지를 표시 할 수 있음
            httpServletRequest.setAttribute("errors", bindingResult.getAllErrors());
            return "front/member/profile";
        }
//        //dto에 담긴 유효값 오류 콘솔에 가져오기
//        if(bindingResult.hasErrors()){
//            //백엔드 콘솔에 유효성 검사 에러 메세지 출력
//            bindingResult.getAllErrors().forEach(error -> {
//                System.out.println("Validation error: " + error.getDefaultMessage());
//            });
//            //프론트에 보내는 키 벨류
//            httpServletRequest.setAttribute("errors", bindingResult.getAllErrors());
//            return "front/member/profile";
//        }

        Cookie[] cookies = httpServletRequest.getCookies();
        String memberAccessToken = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    memberAccessToken = cookie.getValue();
                    break;
                }
            }
        }
        if(memberAccessToken == null){
            return "redirect:/member/login";
        }

        try{
            Map<String, Object> claims = jwtProvider.getClaims(memberAccessToken);
            Long memberId = ((Integer)claims.get("memberId")).longValue();

            this.memberService.update(memberId,
                    memberUpdateRequest.getMemberPassword(),
                    memberUpdateRequest.getMemberEmail(),
                    memberUpdateRequest.getMemberPhone(),
                    memberUpdateRequest.getMemberZipCode(),
                    memberUpdateRequest.getMemberAddress1(),
                    memberUpdateRequest.getMemberAddress2(),
                    memberUpdateRequest.isMemberSmsAgree(),
                    memberUpdateRequest.isMemberEmailAgree()
                    );

            httpServletRequest.setAttribute("message","회원님의 정보가 성공적으로 수정되었습니다.");
            return "front/member/profile";
        } catch (IllegalArgumentException e) {
            httpServletRequest.setAttribute("errors", Collections.singletonList(e.getMessage()));
            return "front/member/profile";
        } catch (Exception e) {
            httpServletRequest.setAttribute("profileError", e.getMessage());
            return "front/member/profile";
        }
    }

    //회원탈퇴상태 요청
    @PostMapping("/withdraw")
    public String withdraw(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        Cookie[] cookies = httpServletRequest.getCookies();
        String memberAccessToken = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("memberAccessToken")){
                    memberAccessToken = cookie.getValue();
                    break;
                }
            }
        }
        if(memberAccessToken == null){
            throw new RuntimeException("인증이 필요합니다.");
        }
        Map<String, Object> claims = jwtProvider.getClaims(memberAccessToken);
        Long memberId = ((Integer) claims.get("memberId")).longValue();

        memberService.withdrawMember(memberId);

        Cookie memberAccessCookie  = new Cookie("memberAccessToken", null);
        memberAccessCookie .setPath("/");
        memberAccessCookie .setMaxAge(0);
        httpServletResponse.addCookie(memberAccessCookie);

        Cookie memberRefreshCookie = new Cookie("memberRefreshToken", null);
        memberRefreshCookie .setPath("/");
        memberRefreshCookie .setMaxAge(0);
        httpServletResponse.addCookie(memberRefreshCookie);

        return "redirect:/";
    }

    //회원 복구
    @PostMapping("/restore")
    public String restore(@ModelAttribute MemberRestoreRequest memberRestoreRequest, Model model){
        try{
            memberService.restore(memberRestoreRequest.getMemberLoginId(), memberRestoreRequest.getMemberPassword());
            model.addAttribute("message", "복구가 완료되었습니다! 다시 로그인 해주세요");
            return "front/member/login";
        }catch (Exception e) {
            model.addAttribute("restoreError", e.getMessage());
            return "front/member/login";
        }
    }

    //로그인 페이지 이동
    @GetMapping("/login")
    public String loginPage(){
        return "front/member/login";
    }
    
    //로그인
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute MemberLoginRequest memberLoginRequest, BindingResult bindingResult, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            return "front/member/login";
        }

        try {
            Member member = memberService.login(memberLoginRequest.getMemberLoginId(), 
                                              memberLoginRequest.getMemberPassword());

            // JWT 액세스 토큰 생성
            String memberAccessToken = jwtProvider.generateMemberAccessToken(member);
            
            Cookie memberAccessCookie = new Cookie("memberAccessToken", memberAccessToken);
            memberAccessCookie.setHttpOnly(true);  // XSS 공격 방지를 위해 JavaScript에서 접근 불가
            memberAccessCookie.setSecure(true);    // HTTPS에서만 전송
            memberAccessCookie.setPath("/");       // 모든 경로에서 쿠키 접근 가능
            memberAccessCookie.setMaxAge(60 * 60);
            httpServletResponse.addCookie(memberAccessCookie);

            Cookie memberRefreshCookie = new Cookie("memberRefreshToken", member.getMemberRefreshToken());
            memberRefreshCookie.setHttpOnly(true);  // XSS 공격 방지를 위해 JavaScript에서 접근 불가
            memberRefreshCookie.setSecure(true);    // HTTPS에서만 전송
            memberRefreshCookie.setPath("/");       // 모든 경로에서 쿠키 접근 가능
            memberRefreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일 동안 유효
            httpServletResponse.addCookie(memberRefreshCookie);

            return "redirect:/";  // 로그인 성공 시 메인 페이지로 리다이렉트

        } catch (RuntimeException e) {
            //메세지중 탈퇴라는 단어가 있으면 밑에 메소드를 실행함
            if (e.getMessage().contains("탈퇴")) {
                httpServletRequest.setAttribute("withdrawnAccount", "true");
                httpServletRequest.setAttribute("withdrawnLoginId", memberLoginRequest.getMemberLoginId());
                httpServletRequest.setAttribute("withdrawnPassword", memberLoginRequest.getMemberPassword());
            }else{
                httpServletRequest.setAttribute("loginError",e.getMessage());
            //바인딩이 가지고 있는 메세지메소드 "${#messages.msg('loginError')}">에러 메시지 이렇게 접근
            //bindingResult.reject("loginError", e.getMessage()); 벨리데이션과의 통합 국제화지원 더 이점이있지만 더 클래식한 방법부터 접근하려고 이방법은 추후에 개선
            }
            return "front/member/login";
        }
    }

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        Cookie[] cookies = httpServletRequest.getCookies();

        String memberAccessToken = "";

        if(cookies != null){

            // 모든 쿠키를 순회하면서 "accessToken"이름을 가진 쿠키 찾기
            for(Cookie cookie : cookies){
                //쿠키.getName 쿠키클래스의 메소드 규약 "accessToken"내가 로그인할 때 이 이름으로 쿠키를 보냈음
                if(cookie.getName().equals("memberAccessToken")){
                    memberAccessToken = cookie.getValue();
                    break;
                }
            }

            if(!memberAccessToken.isEmpty()){
                    Map<String, Object> claims = jwtProvider.getClaims(memberAccessToken);
                    //단순 타입 캐스팅 한번의 캐스팅때문에 괄호가 하나
                    String loginId = (String) claims.get("memberLoginId");
                    Member member = memberService.findByLoginId(loginId);
                    memberService.logout(member);
            }
        }

        Cookie memberAccessCookie  = new Cookie("memberAccessToken", null);
        memberAccessCookie .setPath("/");
        memberAccessCookie .setMaxAge(0);
        memberAccessCookie.setHttpOnly(true);
        memberAccessCookie.setSecure(true);
        httpServletResponse.addCookie(memberAccessCookie);

        Cookie memberRefreshCookie = new Cookie("memberRefreshToken", null);
        memberRefreshCookie .setPath("/");
        memberRefreshCookie .setMaxAge(0);
        memberRefreshCookie.setHttpOnly(true);
        memberRefreshCookie.setSecure(true);
        httpServletResponse.addCookie(memberRefreshCookie);

        //현재 페이지의 URL을 저장
        String referer = httpServletRequest.getHeader("Referer");

        //Referer 담긴 URL을 변수인 referer를 리다이렉트에 포함 시킴
        return "redirect:" + referer;
    }

    //아이디 찾기 페이지 팝업창 호출
    @GetMapping("/findId")
    public String findIdPage(){
        return "front/member/findId";
    }
    //아이디 찾기
    @PostMapping("findId")
    public String findId(@RequestParam String memberName, LocalDate memberBirthDate, String memberEmail, Model model){

        try{
            Member member = memberService.memberFindId(memberName, memberBirthDate, memberEmail);

        String loginId = member.getMemberLoginId();
        String maskedId = loginId.substring(0, loginId.length() - 3) + "***";

        model.addAttribute("success",true);
        model.addAttribute("maskedId", maskedId);

        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("error", e.getMessage());
        }

        return "front/member/findId";
    }

    //비밀번호 찾기 페이지 팝업창 호출
    @GetMapping("/findPassword")
    public String findPasswordPage(){
        return "front/member/findPassword";
    }
    //비밀번호 찾기
    @PostMapping("/findPassword")
    public String findPassword(@RequestParam String memberName, @RequestParam String memberLoginId, @RequestParam String memberEmail, Model model){
        try {
            Member member = memberService.memberFindPassword(memberName, memberLoginId, memberEmail);

            model.addAttribute("success", true);
            model.addAttribute("memberLoginId", member.getMemberLoginId());
        } catch (Exception e){
            model.addAttribute("success", false);
            model.addAttribute("error", e.getMessage());
        }
        return  "front/member/findPassword";
    }

    //비밀번호 재설정
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String memberLoginId, @RequestParam String newPassword, @RequestParam String confirmPassword, Model model) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("새 비밀번호가 일치하지 않습니다.");
            }

            memberService.resetPassword(memberLoginId, newPassword);

            model.addAttribute("success", true);
            model.addAttribute("completed", true);
            model.addAttribute("message", "비밀번호가 성곡적으로 변경되었습니다.");
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("error", e.getMessage());

        }
        return "front/member/findPassword";
    }
}
