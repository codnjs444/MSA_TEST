package com.example.userService.module.user.entity;

import com.example.userService.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_user")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@Comment("사용자UID")
	@Column(name = "uid", length=36)    
    private String uid;
	
	@NotNull
    @Comment("유저ID")
    @Column(name = "user_id", unique = true)
    private String userId;

    @Setter
    @Comment("이메일")
    private String email;

    @NotNull
    @Setter
    @Comment("패스워드")
    private String password;

    @Setter
    @Comment("이름")
    private String name;

    @Setter
    @Comment("연락처")
    private String phone;

    @Setter
    @Comment("기본주소")
    @Column(name = "addr_basic")    
    private String addrBasic;

    @Setter
    @Comment("상세주소")
    @Column(name = "addr_detail")    
    private String addrDetail;

    @Setter
    @Comment("우편번호")
    @Column(name = "post_no")
    private String postNo;

    @Setter
    @Comment("역할")
    @Enumerated(EnumType.STRING)    
    private Role role;
    
    @Setter
    @Comment("사용여부")
    @Column(name = "use_yn")
    private String useYn;
    
    // Password Encode
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
