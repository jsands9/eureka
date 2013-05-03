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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.net.URI;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;

/**
 * Operations related to a data file (upload, status, etc)
 *
 * @author hrathod
 *
 */
@Path("/file")
public class FileResource {

	/**
	 * The data access object used to work with file upload objects in the data
	 * store.
	 */
	private final FileDao fileDao;

	/**
	 * Create an object with the give data access object.
	 *
	 * @param inFileDao The data access object used to communicate with
	 *            {@link FileDao} objects in the data store.
	 */
	@Inject
	public FileResource(FileDao inFileDao) {
		this.fileDao = inFileDao;
	}

	/**
	 * Add a new uploaded file.
	 *
	 * @param fileUpload The file upload to add.
	 * @return A {@link Status#CREATED} if the file is successfully added,
	 *         {@link Status#BAD_REQUEST} if there are errors.
	 */
	@Path("/upload")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadFile(FileUpload fileUpload) {
		this.fileDao.create(fileUpload);
        return Response.created(
                URI.create("/" + fileUpload.getId())).build();
	}

	/**
	 * Get an uploaded file referred to by the given unique identifier.
	 *
	 * @param inId The unique identifier for the file to be fetched.
	 * @return The uploaded file information
	 * @throws ServletException Thrown if the given unique identifier is
	 *             invalid.
	 */
	@Path("/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public FileUpload getFile(@PathParam("id") final Long inId)
			throws ServletException {
		FileUpload fileUpload = this.fileDao.retrieve(inId);
		if (fileUpload == null) {
			throw new ServletException("Invalid ID");
		}
		return fileUpload;
	}
}