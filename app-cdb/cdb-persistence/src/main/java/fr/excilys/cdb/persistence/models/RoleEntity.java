package fr.excilys.cdb.persistence.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class RoleEntity {
  
    @Id
    private Long id;
    private String role;

    public RoleEntity() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + role + "]";
	}
    
}