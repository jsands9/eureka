package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.KnowledgeSource;
import org.protempa.KnowledgeSourceReadException;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PairDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.PropositionDefinitionVisitor;
import org.protempa.SliceDefinition;
import org.protempa.SourceFactory;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendNewInstanceException;
import org.protempa.backend.BackendProviderSpecLoaderException;
import org.protempa.backend.Configurations;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.InvalidConfigurationException;
import org.protempa.bconfigs.commons.INICommonsConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;

/**
 * @author hrathod
 */
@Path("/proposition")
public class PropositionResource {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(PropositionResource.class);
	private static final String CONF_DIR = "/opt/cvrg_users";
	private static final String USER_NAME = "user1";

	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public PropositionWrapper getProposition(@PathParam("key") String inKey) {
		SourceFactory sf;
		PropositionWrapper wrapper = null;
		try {
			File userDir = new File(CONF_DIR, USER_NAME);
			File confDir = new File(userDir, ".protempa-configs");
			Configurations configurations =
					new INICommonsConfigurations(confDir);
			sf = new SourceFactory(configurations, "erat-diagnoses-direct");
			KnowledgeSource knowledgeSource = sf.newKnowledgeSourceInstance();
			PropositionDefinition definition =
					knowledgeSource.readPropositionDefinition(inKey);
			if (definition != null) {
				WrapperVisitor visitor = new WrapperVisitor();
				definition.accept(visitor);
				wrapper = visitor.getWrapper();
			}
		} catch (BackendProviderSpecLoaderException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InvalidConfigurationException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (ConfigurationsLoadException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (BackendNewInstanceException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (BackendInitializationException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (KnowledgeSourceReadException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return wrapper;
	}

	private static class WrapperVisitor
			implements PropositionDefinitionVisitor {

		PropositionWrapper wrapper = new PropositionWrapper();

		public PropositionWrapper getWrapper() {
			return this.wrapper;
		}

		private void getCommonInfo(PropositionDefinition inDefinition) {
			this.wrapper.setKey(inDefinition.getId());
			this.wrapper.setInSystem(true);
			this.wrapper.setAbbrevDisplayName(
					inDefinition.getAbbreviatedDisplayName());
			this.wrapper.setDisplayName(inDefinition.getDisplayName());
		}

		@Override
		public void visit(
				Collection<? extends PropositionDefinition>
						propositionDefinitions) {
			for (PropositionDefinition definition : propositionDefinitions) {
				definition.accept(this);
			}
		}

		@Override
		public void visit(LowLevelAbstractionDefinition def) {
			//To change body of implemented methods use File | Settings |
			// File Templates.
		}

		@Override
		public void visit(HighLevelAbstractionDefinition def) {
			//To change body of implemented methods use File | Settings |
			// File Templates.
		}

		@Override
		public void visit(SliceDefinition def) {
			//To change body of implemented methods use File | Settings |
			// File Templates.
		}

		@Override
		public void visit(EventDefinition def) {
			getCommonInfo(def);
			this.wrapper.setType(PropositionWrapper.Type.AND);
			this.wrapper.setSystemTargets(Arrays.asList(def.getChildren()));
		}

		@Override
		public void visit(PrimitiveParameterDefinition def) {
			//To change body of implemented methods use File | Settings |
			// File Templates.
		}

		@Override
		public void visit(ConstantDefinition def) {
			//To change body of implemented methods use File | Settings |
			// File Templates.
		}

		@Override
		public void visit(PairDefinition def) {
			//To change body of implemented methods use File | Settings |
			// File Templates.
		}
	}
}