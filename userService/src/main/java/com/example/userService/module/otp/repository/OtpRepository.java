package com.example.userService.module.otp.repository;

import com.example.userService.module.otp.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, String> {

	OtpEntity findByEmailAndOneTimePassword(String email , String password);
	
	OtpEntity findBySecretKeyAndIsCertificated(String secretKey , String isCertificated);
}
