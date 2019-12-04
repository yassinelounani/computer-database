package fr.excilys.cdb.api.dto;

import java.util.HashSet;
import java.util.Set;

public class UserDto {
	private String username;
	private String password;
	private Set<String> roles;
	
	
	public UserDto() {
		super();
	}

	public UserDto(Builder builder) {
		super();
		this.username = builder.username;
		this.password = builder.password;
		this.roles = builder.roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserDto [username=" + username + ", password=" + password + ", role=" + roles + "]";
	}

	public static class Builder {
		private String username;
		private String password;
		private Set<String> roles = new HashSet<String>();
		
		public static Builder newInstance() {
			return new Builder();
		}

		public Builder setUsername(String username) {
			this.username = username;
			return this;
		}

		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder setRoles(Set<String> roles) {
			this.roles = roles;
			return this;
		}
		
		public  UserDto build() {
			return new UserDto(this);
		}
		
	}
}
