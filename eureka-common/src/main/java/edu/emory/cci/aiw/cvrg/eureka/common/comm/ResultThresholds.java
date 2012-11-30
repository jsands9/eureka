/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

public class ResultThresholds extends DataElement {

	private static enum ThresholdsOperator {
		ALL, ANY
	}

	private String name;
	private ThresholdsOperator thresholdsOperator;
	private List<ValueThreshold> valueThresholds;

	public ResultThresholds() {
		super(Type.VALUE_THRESHOLD);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ThresholdsOperator getThresholdsOperator() {
		return thresholdsOperator;
	}

	public void setThresholdsOperator(ThresholdsOperator thresholdsOperator) {
		this.thresholdsOperator = thresholdsOperator;
	}

	public List<ValueThreshold> getValueThresholds() {
		return valueThresholds;
	}

	public void setValueThresholds(List<ValueThreshold> valueThresholds) {
		this.valueThresholds = valueThresholds;
	}

	@Override
	public void accept(DataElementVisitor visitor) {
		visitor.visit(this);
	}

}