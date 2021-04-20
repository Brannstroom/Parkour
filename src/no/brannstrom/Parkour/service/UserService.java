package no.brannstrom.Parkour.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import no.brannstrom.Parkour.model.User;

public class UserService {

	private static Client client = ClientBuilder.newClient();
	private static String url = "127.0.0.1";

	public static User update(User user) {
		return client
				.target("http://" + url + "/users")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class)
				.readEntity(new GenericType<User>() {});
	}

	public static List<User> getAllUsers(){
		try {
			return client
					.target("http://" + url + "/users")
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<List<User>>() {});
		} catch (Exception ex) {
			return new ArrayList<>();
		}
	}

	public static User getUser(UUID uuid) {
		return client
				.target("http://" + url + "/users/search/findByUuid")
				.queryParam("uuid", uuid)
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<User>() {});
	}

	public static User getUser(String username) {
		return client
				.target("http://" + url + "/users/search/findByUsername")
				.queryParam("username", username)
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<User>() {});
	}

	public static List<User> getUserBySearch(String search) {
		try {
			return client
					.target("http://" + url + "/users/search/findBySearch")
					.queryParam("search", search)
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<List<User>>() {});
		} catch (Exception ex) {
			return new ArrayList<>();
		}
	}

	public static User getUser(long id) {
		return client
				.target("http://" + url + "/users/search/findByUuid")
				.queryParam("id", id)
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<User>() {});
	}

}