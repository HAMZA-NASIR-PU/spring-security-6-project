package com.security.project.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder // Build objects using design pattern builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // jakarta.persistence
@Table(name = "_user")
//persistent entity should have a primary key
public class User implements UserDetails {

	@Id
	@GeneratedValue // Work by checking the database engine e.g postgresql, mysql
	private Integer id;
	private String firstname;
	private String lastname;
	private String email;
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role; // User can have only one role at a time.

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { // returns list of roles

		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
