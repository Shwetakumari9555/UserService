package com.UserService.entities;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Table(name="users")
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name="user_id")
	private UUID userId;
	
	private String name;
	
	@Email
	@Column(unique = true,nullable = false)
	private String email;
	
	private String password;
	
	private String profilePic;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
	private String phone;
	
	private String location;
	
	@Builder.Default
	private boolean enabled =true;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private Provider provider = Provider.LOCAL;
	 
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@PrePersist
	protected void onCreate() {
		Instant now = Instant.now();
		 createdAt= now;
		updatedAt = now;
		
	}
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt= Instant.now();
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(()-> "ROLE_"+ role.name());
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
