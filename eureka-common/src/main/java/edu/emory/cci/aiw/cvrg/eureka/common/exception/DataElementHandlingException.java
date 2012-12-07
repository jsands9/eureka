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
package edu.emory.cci.aiw.cvrg.eureka.common.exception;

/**
 *
 * @author Andrew Post
 */
public class DataElementHandlingException extends Exception {

	public DataElementHandlingException() {
	}

	public DataElementHandlingException(String string) {
		super(string);
	}

	public DataElementHandlingException(String string, Throwable thrwbl) {
		super(string, thrwbl);
	}

	public DataElementHandlingException(Throwable thrwbl) {
		super(thrwbl);
	}
	
}
