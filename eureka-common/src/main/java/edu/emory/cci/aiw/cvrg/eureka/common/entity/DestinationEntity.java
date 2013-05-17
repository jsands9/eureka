package edu.emory.cci.aiw.cvrg.eureka.common.entity;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "destinations")
public class DestinationEntity extends ConfigEntity {
	@Id
	@SequenceGenerator(name = "DEST_SEQ_GENERATOR", sequenceName = "DEST_SEQ",
	allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "DEST_SEQ_GENERATOR")
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@ManyToOne
	private EtlUser owner;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="destination")
	private List<DestinationGroupMembership> groups;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	public EtlUser getOwner() {
		return owner;
	}

	public void setOwner(EtlUser owner) {
		this.owner = owner;
	}

	public List<DestinationGroupMembership> getGroups() {
		return groups;
	}

	public void setGroups(List<DestinationGroupMembership> groups) {
		this.groups = groups;
	}
}
