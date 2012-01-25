package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.UserRequest;

/**
 * RESTful end-point for {@link User} related methods.
 * 
 * @author hrathod
 * 
 */
@Path("/user")
public class UserResource {

	/**
	 * Data access object to work with User objects.
	 */
	private final UserDao userDao;

	/**
	 * Data access object to work with Role objects.
	 */
	private final RoleDao roleDao;

	/**
	 * Create a UserResource object with a User DAO and a Role DAO.
	 * 
	 * @param inUserDao DAO used to access {@link User} related functionality.
	 * @param inRoleDao DAO used to access {@link Role} related functionality.
	 */
	@Inject
	public UserResource(UserDao inUserDao, RoleDao inRoleDao) {
		this.userDao = inUserDao;
		this.roleDao = inRoleDao;
	}

	/**
	 * Get a list of all users in the system.
	 * 
	 * @return A list of {@link User} objects.
	 */
	@Path("/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		return this.userDao.getUsers();
	}

	/**
	 * Get a user by the user's identification number.
	 * 
	 * @param inId The identification number for the user to fetch.
	 * @return The user referenced by the identification number.
	 * @throws ServletException Thrown if the identification number string can
	 *             not be properly converted to a {@link Long}.
	 */
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") String inId) throws ServletException {
		User user;
		try {
			Long id = Long.valueOf(inId);
			user = this.userDao.get(id);
		} catch (NumberFormatException nfe) {
			throw new ServletException(nfe);
		}
		return user;
	}

	/**
	 * Add a new user to the system.
	 * 
	 * @param userRequest Object containing all the information about the user
	 *            to add.
	 * @return A "Bad Request" error if the user does not pass validation, a
	 *         "Created" response with a link to the user page if successful.
	 */
	@Path("/add")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response addUser(final UserRequest userRequest) {
		Response response = null;
		if (validateUserRequest(userRequest)) {
			User user = new User();
			user.setEmail(userRequest.getEmail());
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			user.setOrganization(userRequest.getOrganization());
			user.setPassword(userRequest.getPassword());
			user.setRoles(this.getDefaultRoles());
			this.userDao.save(user);
			response = Response.created(URI.create("/" + user.getId())).build();
		} else {
			response = Response.status(Status.BAD_REQUEST)
					.entity("Invalid user request.").build();
		}
		return response;
	}

	/**
	 * Validate a {@link UserRequest} object. Two rules are implemented: 1) The
	 * email addresses in the two email fields must match, and 2) The passwords
	 * in the two password fields must match.
	 * 
	 * @param userRequest The {@link UserRequest} object to validate.
	 * @return True if the request is valid, and false otherwise.
	 */
	private static boolean validateUserRequest(UserRequest userRequest) {
		boolean result = true;

		// make sure the email addresses are not null, and match each other
		if ((userRequest.getEmail() == null)
				|| (userRequest.getVerifyEmail() == null)
				|| (!userRequest.getEmail()
						.equals(userRequest.getVerifyEmail()))) {
			result = false;
		}

		// make sure the passwords are not null, and match each other
		if ((userRequest.getPassword() == null)
				|| (userRequest.getVerifyPassword() == null)
				|| (!userRequest.getPassword().equals(
						userRequest.getVerifyPassword()))) {
			result = false;
		}

		return result;
	}

	/**
	 * Get a set of default roles to be added to a newly created user.
	 * 
	 * @return A list of default roles.
	 */
	private List<Role> getDefaultRoles() {
		List<Role> defaultRoles = new ArrayList<Role>();
		for (Role role : this.roleDao.getRoles()) {
			if (role.isDefaultRole() == Boolean.TRUE) {
				defaultRoles.add(role);
			}
		}
		return defaultRoles;
	}
}