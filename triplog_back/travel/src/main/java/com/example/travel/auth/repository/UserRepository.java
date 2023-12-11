package com.example.travel.auth.repository;

import com.example.travel.auth.dto.request.RegisterDto;
import com.example.travel.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    // 직접 가입한 회원에 대한 로그인 처리
    @Query(value = "select uid, uniqueId, pw, email, nickname, roleType, providerType, " +
           "travelCount, level from User where email= :email")
    Optional<User> login(String email) throws SQLException;

    // 직접 가입한 회원 저장
    void save(RegisterDto registerDto) throws SQLException;

    // 직접 가입한 회원의 이메일 인증 => unique_id 자동 발급 => 업데이트
    @Query(value = "update User set modifiedAt = :modifiedAt, uniqueId = :uniqueId where email = :email")
    Optional<String> emailVerification(String email, LocalDateTime modifiedAt, String uniqueId) throws SQLException;

    Optional<User> findUserByUid(Long uid) throws SQLException;

    // 가입여부 조회
    Optional<User> findUserByUniqueId(String uniqueId) throws SQLException;

    Optional<User> findUserByEmail(String email) throws SQLException;

    Optional<User> updateUserByPw(String pw) throws SQLException;

    Optional<User> updateUserByNickname(String nickname);
}