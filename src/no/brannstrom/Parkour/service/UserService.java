package no.brannstrom.Parkour.service;

import java.util.ArrayList;
import java.util.Collections;
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

	private static List<User> cache = Collections.synchronizedList(new ArrayList<>());

	private static Client client = ClientBuilder.newClient();
	private static String url = "127.0.0.1:8080";

	public static User update(User user) {
		cache.remove(user);
		User response = client
				.target("http://" + url + "/users")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class)
				.readEntity(new GenericType<User>() {});
		if (response != null && response.getUuid() == null) return null;
		else return response;
	}

	public static User getUser(UUID uuid) {
		return cache.stream().filter(user -> user.getUuid().equals(uuid)).findFirst().orElseGet(() -> {

			User response = client
					.target("http://" + url + "/users/search/findByUuid")
					.queryParam("uuid", uuid)
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<User>() {});
			if (response != null && response.getUuid() == null) return null;
			else {
				if (response != null) cache.add(response);
				return response;
			}
		});
	}

	public static User getUser(String username) {
		return cache.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst().orElseGet(() -> {

			User response = client
					.target("http://" + url + "/users/search/findByUsername")
					.queryParam("username", username)
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<User>() {});
			if (response != null && response.getUuid() == null) return null;
			else {
				if (response != null) cache.add(response);
				return response;
			}
		});
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

	public static void unCache(UUID uniqueId) {
		unCache(getUser(uniqueId));
	}

	public static void unCache(User user) {
		cache.remove(user);
	}

}