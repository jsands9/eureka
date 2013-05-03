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
package edu.emory.cci.aiw.cvrg.eureka.services.datavalidator;

import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.CPT;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Encounter;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Enrollment;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Icd9Diagnosis;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Icd9Procedure;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Lab;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Medication;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.MedicationDispense;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Patient;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Provider;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Vital;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates the incoming data before it is pushed to the Protempa database.
 * 
 * @author hrathod
 * 
 */
public class DataValidator {
	/**
	 * Holds a list of patients to validate.
	 */
	private List<Patient> patients;
	/**
	 * Holds a list of encounters to validate.
	 */
	private List<Encounter> encounters;

	private List<Enrollment> enrollments;
	/**
	 * Holds a list of providers to validate.
	 */
	private List<Provider> providers;
	/**
	 * Holds a list of CPT codes to validate.
	 */
	private List<CPT> cptCodes;
	/**
	 * Holds a list of ICD9 Procedure codes to validate.
	 */
	private List<Icd9Procedure> icd9Procedures;
	/**
	 * Holds a list of ICD9 Diagnosis codes to validate.
	 */
	private List<Icd9Diagnosis> icd9diagnoses;
	/**
	 * Holds a list of medications to validate.
	 */
	private List<Medication> medications;

	private List<MedicationDispense> dispenses;
	/**
	 * Holds a list of labs to validate.
	 */
	private List<Lab> labs;
	/**
	 * Holds a list of vitals to validate.
	 */
	private List<Vital> vitals;
	/**
	 * Holds the unique identifiers for all the encounters, useful when checking
	 * for orphaned observations.
	 */
	private Set<Long> encounterIds;
	/**
	 * Holds all the warning/error messages from the validation.
	 */
	final private List<ValidationEvent> validationEvents = 
			new ArrayList<ValidationEvent>();
	/**
	 * Holds the unique identifiers for all the observations, for duplication
	 * checking.
	 */
	final private Set<String> observationIds = new HashSet<String>();

	/**
	 * Set the list of patients to validate.
	 * 
	 * @param inPatients The patients to validate.
	 * @return this.
	 */
	public DataValidator setPatients(List<Patient> inPatients) {
		this.patients = inPatients;
		return this;
	}

	/**
	 * Set the list of encounters to validate.
	 * 
	 * @param inEncounters The list of encounters to validate.
	 * @return this.
	 */
	public DataValidator setEncounters(List<Encounter> inEncounters) {
		this.encounters = inEncounters;
		return this;
	}

	public DataValidator setEnrollments(List<Enrollment> inEnrollments) {
		this.enrollments = inEnrollments;
		return this;
	}

	/**
	 * Set the list of providers to validate
	 * 
	 * @param inProviders The list of providers to validate.
	 * @return this.
	 */
	public DataValidator setProviders(List<Provider> inProviders) {
		this.providers = inProviders;
		return this;
	}

	/**
	 * Set the list of CPT codes to validate.
	 * 
	 * @param inCptCodes The list of CPT codes to validate.
	 * @return this.
	 */
	public DataValidator setCpts(List<CPT> inCptCodes) {
		this.cptCodes = inCptCodes;
		return this;
	}

	/**
	 * Set the list of ICD9 procedure codes to validate.
	 * 
	 * @param inIcd9Procedures The list of ICD9 procedure codes to validate.
	 * @return this.
	 */
	public DataValidator setIcd9Procedures(List<Icd9Procedure> inIcd9Procedures) {
		this.icd9Procedures = inIcd9Procedures;
		return this;
	}

	/**
	 * Set the list of ICD9 Diagnosis codes to validate.
	 * 
	 * @param inIcd9Diagnoses The list of ICD9 diagnosis codes to validate.
	 * @return this.
	 */
	public DataValidator setIcd9Diagnoses(List<Icd9Diagnosis> inIcd9Diagnoses) {
		this.icd9diagnoses = inIcd9Diagnoses;
		return this;
	}

	/**
	 * Set the list of medications to validate.
	 * 
	 * @param inMedications The list of medications to validate.
	 * @return this.
	 */
	public DataValidator setMedications(List<Medication> inMedications) {
		this.medications = inMedications;
		return this;
	}

	public DataValidator setDispenses(List<MedicationDispense> inDispenses) {
		this.dispenses = inDispenses;
		return this;
	}

	/**
	 * Set the list of labs to validate.
	 * 
	 * @param inLabs The list of labs to validate.
	 * @return this.
	 */
	public DataValidator setLabs(List<Lab> inLabs) {
		this.labs = inLabs;
		return this;
	}

	/**
	 * Set the list of vitals to validate.
	 * 
	 * @param inVitals The list of vitals to validate.
	 * @return this.
	 */
	public DataValidator setVitals(List<Vital> inVitals) {
		this.vitals = inVitals;
		return this;
	}

	/**
	 * Run all the validation routines for the data we have.
	 */
	public void validate() {
		this.validatePatients();
		this.validateEncounters();
		this.validateEnrollments();
		this.validateProviders();
		this.validateCptCodes();
		this.validateIcd9Procedures();
		this.validateIcd9Diagnoses();
		this.validateMedications();
		this.validateDispenses();
		this.validateLabs();
		this.validateVitals();
	}

	/**
	 * Validate the list of patients.
	 */
	private void validatePatients() {
		long counter = 1L;
		String type = "patient";
		Set<Long> idSet = new HashSet<Long>();
		for (Patient patient : this.patients) {
			this.validateId(patient.getId(), counter, type, "bad patient id");
			this.validateName(patient.getFirstName(), counter, type,
					"first name");
			this.validateName(patient.getLastName(), counter, type, "last name");
			this.checkDuplicateLongIds(counter, type, idSet, patient.getId());
			counter++;
		}
	}

	/**
	 * Validate the list of encounters.
	 */
	private void validateEncounters() {
		long counter = 1;
		String type = "encounter";
		Set<Long> idSet = new HashSet<Long>();
		for (Encounter encounter : this.encounters) {
			this.validateId(encounter.getId(), counter, type,
					"bad encounter id");
			this.checkDuplicateLongIds(counter, type, idSet, encounter.getId());
			counter++;
		}
	}

	private void validateEnrollments() {
		long counter = 1;
		String type = "enrollment";
		Set<Long> idSet = new HashSet<Long>();
		for (Enrollment enrollment : this.enrollments) {
			this.validateId(enrollment.getId(), counter, type,
					"bad enrollment id");
			this.checkDuplicateLongIds(counter, type, idSet, enrollment.getId());
			counter++;
		}
	}

	/**
	 * Validate the list of providers passed to the object.
	 */
	private void validateProviders() {
		long counter = 1;
		String type = "provider";
		Set<Long> idSet = new HashSet<Long>();
		for (Provider provider : this.providers) {
			this.validateId(provider.getId(), counter, type, "bad provider id");
			this.checkDuplicateLongIds(counter, type, idSet, provider.getId());
			counter++;
		}
	}



	/**
	 * Validate the list of CPT codes.
	 */
	private void validateCptCodes() {
		long counter = 1;
		String type = "cptcode";
		for (CPT cpt : this.cptCodes) {
			this.validateId(cpt.getId(), counter, type, "bad cpt id");
			this.validateId(cpt.getEncounterId(), counter, type,
					"bad encounter id");
			this.checkDuplicateStringIds(counter, type, this.observationIds,
					cpt.getId());
			this.checkOrphan(cpt.getEncounterId(), type, counter);
			counter++;
		}
	}

	/**
	 * Validate the list of ICD9 procedure codes.
	 */
	private void validateIcd9Procedures() {
		long counter = 1;
		String type = "icd9procedure";
		for (Icd9Procedure procedure : this.icd9Procedures) {
			this.validateId(procedure.getId(), counter, type,
					"bad procedure id");
			this.validateId(procedure.getEncounterId(), counter, type,
					"bad encounter id");
			this.checkDuplicateStringIds(counter, type, this.observationIds,
					procedure.getId());
			this.checkOrphan(procedure.getEncounterId(), type, counter);
			counter++;
		}
	}

	/**
	 * Validate the list of ICD9 diagnosis codes.
	 */
	private void validateIcd9Diagnoses() {
		long counter = 1;
		String type = "icd9diangosis";
		for (Icd9Diagnosis diagnosis : this.icd9diagnoses) {
			this.validateId(diagnosis.getId(), counter, type,
					"bad diagnoses id");
			this.validateId(diagnosis.getEncounterId(), counter, type,
					"bad encounter id");
			this.checkDuplicateStringIds(counter, type, this.observationIds,
					diagnosis.getId());
			this.checkOrphan(diagnosis.getEncounterId(), type, counter);
			counter++;
		}
	}

	/**
	 * Validate the list of medications.
	 */
	private void validateMedications() {
		long counter = 1;
		String type = "medication";
		for (Medication medication : this.medications) {
			this.validateId(medication.getId(), counter, type,
					"bad medication id");
			this.validateId(medication.getEncounterId(), counter, type,
					"bad encounter id");
			this.checkDuplicateStringIds(counter, type, this.observationIds,
					medication.getId());
			this.checkOrphan(medication.getEncounterId(), type, counter);
			counter++;
		}
	}

	private void validateDispenses() {
		long counter = 1;
		String type = "dispense";
		for (MedicationDispense dispense : this.dispenses) {
			this.validateId(dispense.getId(), counter, type,
					"bad dispense id");
			this.validateId(dispense.getEncounterId(), counter, type,
					"bad encounter id");
			this.checkDuplicateStringIds(counter, type, this.observationIds,
					dispense.getId());
			this.checkOrphan(dispense.getEncounterId(), type, counter);
			counter++;
		}
	}

	/**
	 * Validate the list of labs.
	 */
	private void validateLabs() {
		long counter = 1;
		String type = "lab";
		for (Lab lab : this.labs) {
			this.validateId(lab.getId(), counter, type, "bad lab id");
			this.validateId(lab.getEncounterId(), counter, type,
					"bad encounter id");
			this.checkDuplicateStringIds(counter, type, this.observationIds,
					lab.getId());
			this.checkOrphan(lab.getEncounterId(), type, counter);
			counter++;
		}
	}

	/**
	 * Validate the list of vitals.
	 */
	private void validateVitals() {
		long counter = 1;
		String type = "vital";
		for (Vital vital : this.vitals) {
			this.validateId(vital.getId(), counter, type, "bad vital id");
			this.validateId(vital.getEncounterId(), counter, type,
					"bad encounter id");
			this.checkDuplicateStringIds(counter, type, this.observationIds,
					vital.getId());
			this.checkOrphan(vital.getEncounterId(), type, counter);
			counter++;
		}
	}

	/**
	 * Validate the given unique identifier; and add a warning to the message
	 * list if invalid.
	 * 
	 * @param id The unique identifier to check
	 * @param line The line number where the unique identifier was found.
	 * @param type The type of the message to create if there is a validation
	 *            error.
	 * @param message The message to set as the event text.
	 */
	private void validateId(Object id, long line, String type, String message) {
		if (id == null) {
			ValidationEvent event = new ValidationEvent();
			event.setLine(Long.valueOf(line));
			event.setType(type);
			event.setMessage(message);
			event.setFatal(true);
			this.getValidationEvents().add(event);
		}
	}

	/**
	 * Validate a string containing a name; and add a warning event if the
	 * validation fails.
	 * 
	 * @param name A string containing the name to validate.
	 * @param line The line number where the name was found.
	 * @param type The type of the message to create if validation fails.
	 * @param message The message to set as the event text.
	 */
	private void validateName(String name, long line, String type,
			String message) {
		if (name.length() > 32) {
			ValidationEvent event = new ValidationEvent();
			event.setLine(Long.valueOf(line));
			event.setType(type);
			event.setMessage(message + " too long (> 32 characters)");
			event.setFatal(false);
			this.getValidationEvents().add(event);
		}
	}

	/**
	 * Check if the given unique identifier already is a member of the given
	 * set. If so, a validation error event is generated. If no, the id is added
	 * to the given set.
	 * 
	 * @param line The line number where the unique identifier is found.
	 * @param type The type of the event to generate.
	 * @param idSet The set which contains the unique identifiers so far.
	 * @param id The id to check.
	 */
	private void checkDuplicateLongIds(long line, String type, Set<Long> idSet,
			Long id) {
		if (!idSet.add(id)) {
			ValidationEvent event = new ValidationEvent();
			event.setFatal(true);
			event.setLine(Long.valueOf(line));
			event.setType(type);
			event.setMessage("duplicate id");
			this.getValidationEvents().add(event);
		}
	}

	/**
	 * Check if the given unique identifier already is a member of the given
	 * set. If so, a validation error event is generated. If no, the id is added
	 * to the given set.
	 * 
	 * @param line The line number where the unique identifier is found.
	 * @param type The type of the event to generate.
	 * @param idSet The set which contains the unique identifiers so far.
	 * @param id The id to check.
	 */
	private void checkDuplicateStringIds(long line, String type,
			Set<String> idSet, String id) {
		if (!idSet.add(id)) {
			ValidationEvent event = new ValidationEvent();
			event.setFatal(true);
			event.setLine(Long.valueOf(line));
			event.setType(type);
			event.setMessage("duplicate id");
			this.getValidationEvents().add(event);
		}
	}

	/**
	 * Return a set of encounter unique identifiers.
	 * 
	 * @return A set of encounter unique identifiers.
	 */
	private Set<Long> getEncounterIds() {
		if (this.encounterIds == null) {
			Set<Long> idSet = new HashSet<Long>();
			for (Encounter encounter : this.encounters) {
				idSet.add(encounter.getId());
			}
			this.encounterIds = idSet;
		}
		return this.encounterIds;
	}

	/**
	 * Check if a given id is part of the encounter ID set. If not, the
	 * observation is orphaned because no encounter points to the observation.
	 * 
	 * @param id The id of the observation.
	 * @param type The type of the observation.
	 * @param line The line number where the unique identifier was found.
	 */
	private void checkOrphan(Long id, String type, long line) {
		Set<Long> idSet = this.getEncounterIds();
		if (!idSet.contains(id)) {
			ValidationEvent event = new ValidationEvent();
			event.setFatal(true);
			event.setLine(Long.valueOf(line));
			event.setType(type);
			event.setMessage("orphaned record");
			this.getValidationEvents().add(event);
		}
	}

	/**
	 * Get the validation events generated after validate() is called.
	 * 
	 * @return the validationEvents
	 */
	public List<ValidationEvent> getValidationEvents() {
		return this.validationEvents;
	}
}