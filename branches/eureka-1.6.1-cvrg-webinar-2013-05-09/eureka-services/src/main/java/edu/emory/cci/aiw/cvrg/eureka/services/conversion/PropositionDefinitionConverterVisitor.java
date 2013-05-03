/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import org.protempa.PropositionDefinition;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PropositionDefinitionConverterVisitor implements
		DataElementEntityVisitor {

	private List<PropositionDefinition> propositionDefinitions;
	private final Set<String> propIds;
	private PropositionDefinition primaryProposition;
	private String primaryPropositionId;
	private Long userId;
	private final SystemPropositionConverter systemPropositionConverter;
	private final CategorizationConverter categorizationConverter;
	private final SequenceConverter sequenceConverter;
	private final ValueThresholdsLowLevelAbstractionConverter valueThresholdsLowLevelAbstractionConverter;
	private final ValueThresholdsCompoundLowLevelAbstractionConverter valueThresholdsCompoundLowLevelAbstractionConverter;
	private final FrequencyNonConsecutiveAbstractionConverter frequencySliceAbstractionConverter;
	private final FrequencyConsecutiveConverter frequencyHighLevelAbstractionConverter;

	@Inject
	public PropositionDefinitionConverterVisitor(SystemPropositionConverter inSystemPropositionConverter,
			CategorizationConverter inCategorizationConverter,
			SequenceConverter inSequenceConverter,
			ValueThresholdsLowLevelAbstractionConverter inValueThresholdsLowLevelAbstractionConverter,
			ValueThresholdsCompoundLowLevelAbstractionConverter inValueThresholdsCompoundLowLevelAbstractionConverter,
			FrequencyNonConsecutiveAbstractionConverter inFrequencySliceAbstractionConverter,
			FrequencyConsecutiveConverter inFrequencyHighLevelAbstractionConverter) {
		systemPropositionConverter = inSystemPropositionConverter;
		categorizationConverter = inCategorizationConverter;
		categorizationConverter.setConverterVisitor(this);
		sequenceConverter = inSequenceConverter;
		sequenceConverter.setConverterVisitor(this);
		valueThresholdsLowLevelAbstractionConverter =
				inValueThresholdsLowLevelAbstractionConverter;
		valueThresholdsLowLevelAbstractionConverter.setConverterVisitor(this);
		valueThresholdsCompoundLowLevelAbstractionConverter =
				inValueThresholdsCompoundLowLevelAbstractionConverter;
		valueThresholdsCompoundLowLevelAbstractionConverter.setConverterVisitor(this);
		frequencySliceAbstractionConverter =
				inFrequencySliceAbstractionConverter;
		frequencySliceAbstractionConverter.setVisitor(this);
		frequencyHighLevelAbstractionConverter =
				inFrequencyHighLevelAbstractionConverter;
		frequencyHighLevelAbstractionConverter.setConverterVisitor(this);
		propIds = new HashSet<String>();
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	public Collection<PropositionDefinition> getPropositionDefinitions() {
		return propositionDefinitions;
	}

	public PropositionDefinition getPrimaryProposition() {
		return primaryProposition;
	}

	public String getPrimaryPropositionId() {
		return primaryPropositionId;
	}

	public boolean addPropositionId(String propId) {
		return this.propIds.add(propId);
	}

	@Override
	public void visit(SystemProposition entity) {
		this.propositionDefinitions = Collections.emptyList();
		this.systemPropositionConverter.setUserId(this.userId);
		this.propositionDefinitions = this.systemPropositionConverter
				.convert(entity);
		this.primaryProposition = this.systemPropositionConverter
				.getPrimaryPropositionDefinition();
		this.primaryPropositionId =
				this.systemPropositionConverter.getPrimaryPropositionId();
	}

	@Override
	public void visit(CategoryEntity entity) {
		this.propositionDefinitions = this.categorizationConverter
				.convert(entity);
		this.primaryProposition = this.categorizationConverter
				.getPrimaryPropositionDefinition();
		this.primaryPropositionId =
				this.categorizationConverter.getPrimaryPropositionId();
	}

	@Override
	public void visit(SequenceEntity entity) {
		this.propositionDefinitions = this.sequenceConverter
				.convert(entity);
		this.primaryProposition = this.sequenceConverter
				.getPrimaryPropositionDefinition();
		this.primaryPropositionId =
				this.sequenceConverter.getPrimaryPropositionId();
	}

	@Override
	public void visit(ValueThresholdGroupEntity entity) {
		if (entity.getValueThresholds() != null
				&& entity.getValueThresholds().size() > 1) {
			this.propositionDefinitions = 
					this.valueThresholdsCompoundLowLevelAbstractionConverter
					.convert(entity);
			this.primaryProposition = 
					this.valueThresholdsCompoundLowLevelAbstractionConverter
					.getPrimaryPropositionDefinition();
			this.primaryPropositionId = 
					this.valueThresholdsCompoundLowLevelAbstractionConverter
					.getPrimaryPropositionId();
		} else {
			this.propositionDefinitions = this.valueThresholdsLowLevelAbstractionConverter
					.convert(entity);
			this.primaryProposition = this.valueThresholdsLowLevelAbstractionConverter
					.getPrimaryPropositionDefinition();
			this.primaryPropositionId =
					this.valueThresholdsLowLevelAbstractionConverter
					.getPrimaryPropositionId();
		}
	}

	@Override
	public void visit(FrequencyEntity entity) {
		if (entity.isConsecutive()) {
			this.propositionDefinitions = this.frequencyHighLevelAbstractionConverter.convert(entity);
			this.primaryProposition = this.frequencyHighLevelAbstractionConverter
					.getPrimaryPropositionDefinition();
			this.primaryPropositionId =
					this.frequencyHighLevelAbstractionConverter
					.getPrimaryPropositionId();
		} else {
			this.propositionDefinitions = this.frequencySliceAbstractionConverter
					.convert(entity);
			this.primaryProposition = this.frequencySliceAbstractionConverter
					.getPrimaryPropositionDefinition();
			this.primaryPropositionId =
					this.frequencySliceAbstractionConverter
					.getPrimaryPropositionId();
		}
	}
}