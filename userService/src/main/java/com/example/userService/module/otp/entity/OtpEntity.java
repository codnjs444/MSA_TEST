package com.example.userService.module.otp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Getter
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_otp")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OtpEntity {

	private static final long OTP_VALID_DURATION = 10 * 60 * 1000; // 10 minutes
	
	@Id
	@Comment("이메일")
	@NotNull
    @Column(nullable = false)
	private String email;
	
	@NotNull
	@Comment("시크릿키")
	@Column(name = "secret_key")
	private String secretKey;

	@Comment("인증번호")
	@Column(name = "one_time_password")
	private String oneTimePassword;

	@NotNull
	@Comment("발급시간")
	@Column(name = "otp_requested_time")
	private Date otpRequestedTime;
	
	@Setter
	@Comment("인증여부")
	@Column(name = "is_certificated")
	private String isCertificated;

	public boolean isOTPRequired( ) {
		if(this.oneTimePassword  == null ) {
			return false;
		}
		long currentTime = System.currentTimeMillis();
		long optRequestedTime = this.otpRequestedTime.getTime();
		if (optRequestedTime + OTP_VALID_DURATION < currentTime) {
            // OTP expires
            return false;
        }
        return true;
	}
	
	@Builder
	public OtpEntity(String email , String oneTimePassword , Date otpRequestedTime , String secretKey , String isCertificated) {
		this.email = email;
		this.oneTimePassword = oneTimePassword;
		this.otpRequestedTime = otpRequestedTime;
		this.secretKey = secretKey;
		this.isCertificated = isCertificated;
		
	}
}
