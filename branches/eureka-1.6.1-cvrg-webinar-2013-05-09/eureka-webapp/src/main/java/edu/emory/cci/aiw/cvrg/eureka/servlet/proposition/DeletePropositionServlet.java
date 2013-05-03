/*
 * #%L
 * Eureka WebApp
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
package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

import com.sun.jersey.api.client.ClientResponse;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.ws.rs.core.MediaType;

public class DeletePropositionServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeletePropositionServlet.class);


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		LOGGER.debug("DeletePropositionServlet");
		String propKey = req.getParameter("key");

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		Principal principal = req.getUserPrincipal();
		String userName = principal.getName();
		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);
		User user = servicesClient.getUserByName(userName);

		// user/delete/{userId}/{prodId}
		try {
			servicesClient.deleteUserElement(user.getId(), propKey);
		} catch (ClientException e) {
			resp.setContentType(MediaType.TEXT_PLAIN);
			switch (e.getResponseStatus()) {
				case INTERNAL_SERVER_ERROR:
					resp.setStatus(
							HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					LOGGER.error("Error deleting data element " + propKey, e);
					ResourceBundle messages = 
							(ResourceBundle) req.getAttribute("messages");
					String msgTemplate = 
							messages.getString("deleteDataElement.error.internalServerError");
					String msg = 
							MessageFormat.format(msgTemplate, "aiwhelp@emory.edu");
					resp.getWriter().write(msg);
					break;
				default:
					LOGGER.debug("Deleting data element {} failed", propKey, e);
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					resp.getWriter().write(e.getMessage());
			}
		}
	}
}