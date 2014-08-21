package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.arp.javautil.collections.Collections;
import org.protempa.proposition.Proposition;

/**
 *
 * @author Andrew Post
 */
public class Cohort {

	private Long id;

	private Node node;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public boolean evaluate(List<Proposition> propositions) {
		Map<String, List<Proposition>> propMap = new HashMap<>();
		for (Map.Entry<String, List<Proposition>> me : propMap.entrySet()) {
			for (Proposition prop : me.getValue()) {
				Collections.putList(propMap, prop.getId(), prop);
			}
		}
		return this.node.evaluate(propMap);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}