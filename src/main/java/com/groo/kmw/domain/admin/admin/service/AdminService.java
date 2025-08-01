package com.groo.kmw.domain.admin.admin.service;

import com.groo.kmw.domain.admin.admin.entity.Admin;
import com.groo.kmw.domain.admin.admin.repository.AdminRepository;
import com.groo.kmw.global.jwt.JwtProvider;
import com.groo.kmw.global.security.AdminSecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    //관리자 회원가입
    public Admin signup(String adminLoginId, String adminPassword, String adminName){
        if(adminRepository.existsByAdminLoginId(adminLoginId)){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Admin admin = new Admin();

        admin.setAdminLoginId(adminLoginId);
        admin.setAdminPassword(passwordEncoder.encode(adminPassword));
        admin.setAdminName(adminName);
        
        return adminRepository.save(admin);
    }
    //관리자 회원수정
    @Transactional
    public Admin update(Long adminId, String adminPassword){
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if(optionalAdmin.isEmpty()){
            throw new RuntimeException("해당 ID의 관리자를 찾을 수 없습니다.");
        }

        Admin admin = optionalAdmin.get();

        if(adminPassword != null && !adminPassword.isBlank()){
            admin.setAdminPassword(passwordEncoder.encode(adminPassword));
        }
        return adminRepository.save(admin);
    }

    //관리자 로그인
    @Transactional
    public Admin login(String adminLoginId, String adminPassword){

        Optional<Admin> optionalAdmin = this.adminRepository.findByAdminLoginId(adminLoginId);

        if (optionalAdmin.isEmpty()){
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }

        Admin admin = optionalAdmin.get();

        if(!passwordEncoder.matches(adminPassword, admin.getAdminPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if(admin.getAdminStatus() == 1){
            throw new RuntimeException("계정은 현재 승인 대기 중입니다.");
        }

        String adminRefreshToken = jwtProvider.generateAdminRefreshToken(admin);
        admin.setAdminRefreshToken(adminRefreshToken);
        adminRepository.save(admin);

        return admin;
    }

    //관리자 로그아웃
    @Transactional
    public void logout(Admin admin){
        admin.setAdminRefreshToken(null);
        adminRepository.save(admin);
    }
    //관리자 유저정보
    public Admin findById(Long adminId){
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if(optionalAdmin.isEmpty()){
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }
        return optionalAdmin.get();
    }

    //토큰 유효성 검증
    public boolean validateToken(String token){
        return jwtProvider.verify(token);
    }
    //토큰 갱신
    public String refreshAccessToken(String adminRefreshToken){
        Optional<Admin> optionalAdmin = adminRepository.findByAdminRefreshToken(adminRefreshToken);
        if(optionalAdmin.isEmpty()){
            throw new RuntimeException("유효하지 않는 토큰입니다.");
        }
        Admin admin = optionalAdmin.get();

        String accessToken = jwtProvider.generateAdminAccessToken(admin);

        return accessToken;
    }

    //쿠키로 회원 정보를 가져와 어떤 회원인지 확인
    @Transactional(readOnly = true)
    public AdminSecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = jwtProvider.getClaims(accessToken);

        // 토큰 타입 확인
        String type = (String) payloadBody.get("type");
        if (!"ADMIN".equals(type)) {
            throw new RuntimeException("관리자 토큰이 아닙니다.");
        }

        long adminId = Long.valueOf(payloadBody.get("adminId").toString());
        String adminLoginId = (String) payloadBody.get("adminLoginId");
        String adminName = (String) payloadBody.get("adminName");

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new AdminSecurityUser(adminId, adminLoginId, "", authorities);
    }
}
