package com.groo.kmw.domain.front.member.service;

import com.groo.kmw.domain.front.member.entity.Member;
import com.groo.kmw.domain.front.member.entity.enums.Gender;
import com.groo.kmw.domain.front.member.repository.MemberRepository;
import com.groo.kmw.global.jwt.JwtProvider;
import com.groo.kmw.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    //회원가입
    @Transactional
    public Member signup(String memberLoginId, String memberPassword, String memberName,
                         String memberPhone, String memberEmail, LocalDate memberBirthDate,
                         Gender memberGender, String memberZipCode, String memberAddress1,
                         String memberAddress2, boolean memberEmailAgree, boolean memberSmsAgree){

        if(memberRepository.existsByMemberLoginId(memberLoginId)){
            //Illegal잘못된Argument 매개변수Exception예외처리
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if(memberRepository.existsByMemberEmail(memberEmail)){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = new Member();

        member.setMemberLoginId(memberLoginId);
        member.setMemberPassword(passwordEncoder.encode(memberPassword));
        member.setMemberName(memberName);
        member.setMemberPhone(memberPhone);
        member.setMemberEmail(memberEmail);
        member.setMemberBirthDate(memberBirthDate);
        member.setMemberGender(memberGender);
        member.setMemberZipCode(memberZipCode);
        member.setMemberAddress1(memberAddress1);
        member.setMemberAddress2(memberAddress2);
        member.setMemberEmailAgree(memberEmailAgree);
        member.setMemberSmsAgree(memberSmsAgree);

        if(memberEmailAgree){
            member.setMemberEmailAgreeTime(LocalDateTime.now());
        }
        if (memberSmsAgree){
            member.setMemberSmsAgreeTime(LocalDateTime.now());
        }
        return memberRepository.save(member);
    }

    //회원정보 수정
    @Transactional
    public Member update(Long id, String memberPassword, String memberEmail,
                         String memberPhone, String memberZipCode, String memberAddress1,
                         String memberAddress2, boolean memberSmsAgree, boolean memberEmailAgree){
        Optional<Member> optionalMember = memberRepository.findById(id);

        if(optionalMember.isEmpty()){
            throw  new RuntimeException("해당 ID의 회원을 찾을 수 없습니다.");
        }
        Member member = optionalMember.get();

        // 비밀번호 유효성 검사
        if (memberPassword != null && !memberPassword.isBlank()) {
            validatePassword(memberPassword);
            member.setMemberPassword(passwordEncoder.encode(memberPassword));
        }

        if(memberEmail != null && !memberEmail.isBlank()) {
            member.setMemberEmail(memberEmail);
        }
        if(memberPhone != null && !memberPhone.isBlank()) {
            member.setMemberPhone(memberPhone);
        }
        if(memberZipCode != null && !memberZipCode.isBlank()) {
            member.setMemberZipCode(memberZipCode);
        }
        if(memberAddress1 != null && !memberAddress1.isBlank()) {
            member.setMemberAddress1(memberAddress1);
        }
        if(memberAddress2 != null) {  // 상세주소는 빈 값 허용
            member.setMemberAddress2(memberAddress2);
        }
        member.setMemberSmsAgree(memberSmsAgree);
        member.setMemberEmailAgree(memberEmailAgree);

        // 동의 시간 설정 수정
        if(memberEmailAgree && member.getMemberEmailAgreeTime() == null){
            member.setMemberEmailAgreeTime(LocalDateTime.now());
        } else if(!memberEmailAgree) {
            member.setMemberEmailAgreeTime(null);
        }
        
        if(memberSmsAgree && member.getMemberSmsAgreeTime() == null){
            member.setMemberSmsAgreeTime(LocalDateTime.now());
        } else if(!memberSmsAgree) {
            member.setMemberSmsAgreeTime(null);
        }

        return memberRepository.save(member);
    }

    //회원탈퇴상태 요청
    public void withdrawMember(Long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()){
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        member.setMemberStatus(8);
        member.setWithdrawDateTime(LocalDateTime.now());
        memberRepository.save(member);

        logout(member);
    }
    //회원 탈퇴
    public void deleteWithdrawMembers(){
        LocalDateTime fourteenDays = LocalDateTime.now().minusDays(14);
        List<Member> withdrawMembers = memberRepository.findByMemberStatusAndWithdrawDateTimeBefore(8, fourteenDays);
        for(Member member: withdrawMembers) {
            memberRepository.delete(member);
        }
    }

    //회원 복구
    public void restore(String memberLoginId, String rawPassword){
        Optional<Member> optionalMember = memberRepository.findByMemberLoginId(memberLoginId);
        if(optionalMember.isEmpty()){
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }
        Member member = optionalMember.get();

        if(!passwordEncoder.matches(rawPassword, member.getMemberPassword())){
            throw  new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if(member.getMemberStatus() != 8){
            throw new RuntimeException("탈퇴 상태가 아닙니다.");
        }
        //실제로는 14일 이후 데이터 완전 소멸이라 작동하지는 않지만 혹시라도 스케줄러가 정상 작동하지않거나 데이터가 꼬일 경우를 대비해 방어적으로 남겨두는게 좋다해서 추가
        if(member.getWithdrawDateTime() == null || member.getWithdrawDateTime().plusDays(14).isBefore(LocalDateTime.now())){
            throw new RuntimeException("복구 가능 기간(14일)이 지났습니다. 고객센터에 문의 바랍니다.");
        }

        member.setMemberStatus(0);
        member.setWithdrawDateTime(null);
        memberRepository.save(member);
    }

    //로그인
    @Transactional
    public Member login(String memberLoginId, String memberPassword){

        Optional<Member> optionalMember = this.memberRepository.findByMemberLoginId(memberLoginId);
        //isEmpty와 isPresent는 의미가 반대 isEmpty는 값이 없다면 isPresent는 값이 있다면
        if(optionalMember.isEmpty()){
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        if(!passwordEncoder.matches(memberPassword, member.getMemberPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if(member.getMemberStatus() == 9){
            throw new RuntimeException("이용이 정지된 계정입니다.");

        }else if(member.getMemberStatus() == 8){
            throw new RuntimeException("탈퇴된 계정입니다. 복구는 14일 이내 가능하며, 이후에는 계정 정보가 삭제됩니다. ");
        }

        String memberRefreshToken = jwtProvider.generateMemberRefreshToken(member);
        member.setMemberRefreshToken(memberRefreshToken);
        memberRepository.save(member);

        return member;
    }
    //로그아웃
    @Transactional
    public void logout(Member member){
        member.setMemberRefreshToken(null);
        memberRepository.save(member);
    }

    //유저정보 pk
    @Transactional(readOnly = true)
    public Member findById(Long memberId){
//        //orElseThrow를 사용안하고싶어서 타입을 옵셔널로했더니 컨트롤러도 옵셔널로 맞춰줘야함 orElseThrow로 하면 맴버로 반환됨 레파지토리는 옵셔널 인데 이걸 사용하면 맴버로
//        //뿌려지는 이유를 찾아봐야겠음
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 회원을 찾을 수 없습니다."));
    }

    //유저 loginId 정보
    @Transactional(readOnly = true)
    public Member findByLoginId(String memberLoginId){
        //orElseThrow를 사용안하고싶어서 타입을 옵셔널로했더니 컨트롤러도 옵셔널로 맞춰줘야함 orElseThrow로 하면 맴버로 반환됨 레파지토리는 옵셔널 인데 이걸 사용하면 맴버로
        //뿌려지는 이유를 찾아봐야겠음
        return memberRepository.findByMemberLoginId(memberLoginId).orElseThrow(() -> new RuntimeException("해당 ID의 회원을 찾을 수 없습니다."));
    }

    //토큰 유효성 검증
    public boolean validateToken(String token){
        return jwtProvider.verify(token);
    }

    //토큰 갱신
    public String refreshAccessToken(String memberRefreshToken){

        Optional<Member> optionalMember = memberRepository.findByMemberRefreshToken(memberRefreshToken);
        if(optionalMember.isEmpty()){
            throw  new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        Member member = optionalMember.get();

        String accessToken = jwtProvider.generateMemberAccessToken(member);

        return accessToken;
    }


    // JwtAuthorizationFilter에서 인가처리때 호출해서 사용
    // 토큰으로 securityUser로 가공된 객체 정보 가져오기
    @Transactional(readOnly = true)
    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = jwtProvider.getClaims(accessToken);
        //페이로드에서 맴버기본키 가져오기
        long memberId = (int) payloadBody.get("memberId");
        //페이로드에서 맴버로그인값 가져오기
        String memberLoginId = (String) payloadBody.get("memberLoginId");
        
        // 회원 정보 조회
        Member member = findByLoginId(memberLoginId);
        
        //권한 리스트 재설정
        List<GrantedAuthority> authorities = new ArrayList<>();
        //가공한 SecurityUser에 담아 반환
        return new SecurityUser(memberId, memberLoginId, "", authorities);
    }

    //비밀번호 유효성 검증 로직 추가
    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            throw new IllegalArgumentException("비밀번호는 영문과 숫자를 포함해야 합니다.");
        }
    }

    //아이디 찾기
    public Member memberFindId(String memberName, LocalDate memberBirthDate, String memberEmail){
        Optional<Member> optionalMember = memberRepository.findByMemberNameAndMemberBirthDateAndMemberEmail(
                memberName,
                memberBirthDate,
                memberEmail
        );
        if(optionalMember.isEmpty()){
            throw new RuntimeException("일치하는 회원 정보가 없습니다.");
        }
        Member member = optionalMember.get();

        if (member.getMemberStatus() == 8) {
            throw new RuntimeException("탈퇴한 회원입니다.");
        }
        return member;
    }

    //비밀번호 찾기
    public Member memberFindPassword(String memberName, String memberLoginId, String memberEmail){
        Optional<Member> optionalMember = memberRepository.findByMemberNameAndMemberLoginIdAndMemberEmail(
                memberName,
                memberLoginId,
                memberEmail
        );
        if(optionalMember.isEmpty()){
            throw new RuntimeException("일치하는 회원 정보가 없습니다.");
        }

        Member member = optionalMember.get();

        if(member.getMemberStatus() == 8){
            throw new RuntimeException("탈퇴한 회원입니다.");
        }
        return member;
    }

    //비밀번호 재설정
    @Transactional
    public void resetPassword(String memberLoginId, String newPassword) {
        Optional<Member> optionalMember = memberRepository.findByMemberLoginId(memberLoginId);
        if(optionalMember.isEmpty()){
            throw new RuntimeException("회원 정보를 찾을 수 없습니다.");
        }

        Member member = optionalMember.get();

        validatePassword(newPassword);

        member.setMemberPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }

    @Transactional
    public void updateMemberDescription(Long memberId, String description) {
        Member member = findById(memberId);
        member.setMemberDescription(description);
        memberRepository.save(member);
    }

    /// /////////////////////////////////////////////////어드민용///////////////////////////////

    //모든 맴버 리스트
    public Page<Member> findAllMembers (Pageable pageable){
        return memberRepository.findAll(pageable);
    }
}
