package com.groo.kmw.domain.admin.admin.repository;

import com.groo.kmw.domain.admin.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByAdminLoginId(String adminLoginId);

    Optional<Admin> findByAdminLoginId(String adminLoginId);

    Optional<Admin> findByAdminRefreshToken(String adminRefreshToken);
}
