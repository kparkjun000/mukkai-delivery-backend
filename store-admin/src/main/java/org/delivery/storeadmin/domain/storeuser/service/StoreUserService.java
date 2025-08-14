package org.delivery.storeadmin.domain.storeuser.service;

import lombok.RequiredArgsConstructor;
import org.delivery.db.storeuser.StoreUserEntity;
import org.delivery.db.storeuser.StoreUserRepository;
import org.delivery.db.storeuser.enums.StoreUserStatus;
// import org.springframework.security.crypto.password.PasswordEncoder;  // Security 제거
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreUserService {

    private final StoreUserRepository storeUserRepository;
    // private final PasswordEncoder passwordEncoder;  // Security 제거

    public StoreUserEntity register(
        StoreUserEntity storeUserEntity
    ){
        storeUserEntity.setStatus(StoreUserStatus.REGISTERED);
        // storeUserEntity.setPassword(passwordEncoder.encode(storeUserEntity.getPassword()));  // Security 제거
        // 간단한 패스워드 처리 (실제 운영에서는 보안이 필요함)
        storeUserEntity.setPassword(storeUserEntity.getPassword());  // 평문 저장 (테스트용)
        storeUserEntity.setRegisteredAt(LocalDateTime.now());
        return storeUserRepository.save(storeUserEntity);
    }

    public Optional<StoreUserEntity> getRegisterUser(String email){
        return storeUserRepository.findFirstByEmailAndStatusOrderByIdDesc(email, StoreUserStatus.REGISTERED);
    }
}
