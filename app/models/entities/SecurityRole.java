/*
 * Copyright 2012 Steve Chaloner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models.entities;

import be.objectify.deadbolt.java.models.Role;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;
import play.libs.F;

import javax.inject.Inject;
import javax.persistence.*;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
@Table(name = "security_roles")
public class SecurityRole implements Role {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long id;

	public String roleName;

	@Override
	public String getName() {
		return roleName;
	}

	public static SecurityRole findByRoleName(String roleName) {
		Session session = (Session)JPA.em().getCriteriaBuilder();
		return (SecurityRole)session.createCriteria(SecurityRole.class).add(Restrictions.eq("role_name", roleName)).list().get(0);
	}
}
