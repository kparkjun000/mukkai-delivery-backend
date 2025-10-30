package org.delivery.api.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.api.Api;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.UserErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.db.user.UserEntity;
import org.delivery.db.user.UserRepository;
import org.delivery.db.user.enums.UserStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * User 도메인 로직을 처리 하는 서비스
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;


    public UserEntity register(UserEntity userEntity){
        return Optional.ofNullable(userEntity)
            .map(it -> {
                // 동일한 이메일로 REGISTERED 상태인 계정이 있는지 확인
                var existingRegisteredUser = userRepository.findFirstByEmailAndStatusOrderByIdDesc(
                    userEntity.getEmail(),
                    UserStatus.REGISTERED
                );
                
                if (existingRegisteredUser.isPresent()) {
                    // 이미 등록된 계정이 있으면 에러
                    throw new ApiException(UserErrorCode.USER_NOT_FOUND, "이미 등록된 이메일입니다.");
                }
                
                // 완전히 새로운 레코드 생성 (UNREGISTERED 계정이 있어도 새 레코드 생성)
                userEntity.setStatus(UserStatus.REGISTERED);
                userEntity.setRegisteredAt(LocalDateTime.now());
                return userRepository.save(userEntity);
            })
            .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "User Entity Null"));
    }

    public UserEntity login(
        String email,
        String password
    ){
        var entity = getUserWithThrow(email, password);
        return entity;
    }

    public UserEntity getUserWithThrow(
        String email,
        String password
    ){
        return userRepository.findFirstByEmailAndPasswordAndStatusOrderByIdDesc(
            email,
            password,
            UserStatus.REGISTERED
        ).orElseThrow(()-> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserEntity getUserWithThrow(
        Long userId
    ){
        return userRepository.findFirstByIdAndStatusOrderByIdDesc(
            userId,
            UserStatus.REGISTERED
        ).orElseThrow(()-> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    /**
     * 계정 삭제 (소프트 삭제)
     * Apple App Store 가이드라인 5.1.1(v) 준수
     * 
     * 사용자 계정을 UNREGISTERED 상태로 변경하고 unregisteredAt 시간을 기록합니다.
     * 실제 데이터는 삭제하지 않고 상태만 변경합니다 (법적 요구사항 준수).
     * 
     * @param userId 삭제할 사용자 ID
     * @throws ApiException 사용자를 찾을 수 없는 경우
     */
    public void deleteAccount(Long userId) {
        var userEntity = getUserWithThrow(userId);
        
        // 사용자 상태를 UNREGISTERED로 변경
        userEntity.setStatus(UserStatus.UNREGISTERED);
        userEntity.setUnregisteredAt(LocalDateTime.now());
        
        // 변경사항 저장
        userRepository.save(userEntity);
        
        // TODO: 추가로 삭제가 필요한 데이터가 있다면 여기서 처리
        // 예: 알림 설정, 캐시된 데이터 등
        // 주의: 주문 내역은 법적 요구사항에 따라 보관해야 할 수 있음
    }
}
