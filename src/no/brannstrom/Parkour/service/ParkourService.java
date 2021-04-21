package no.brannstrom.Parkour.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import no.brannstrom.Parkour.model.Parkour;

public class ParkourService {
	
	private static Client client = ClientBuilder.newClient();
	private static String url = "127.0.0.1:8080";
	
	public static Parkour update(Parkour parkour) {
		Parkour response = client
				.target("http://" + url + "/parkours")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(parkour, MediaType.APPLICATION_JSON), Response.class)
				.readEntity(new GenericType<Parkour>() {});
		if (response != null && response.getId() == null) return null;
		else return response;
	}
	
	public static Parkour getParkour(String name) {
		return client
				.target("http://" + url + "/parkours/search/findByName")
				.queryParam("name", name)
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<Parkour>() {});
	}
	
	public static List<Parkour> getParkours(){
		try {
			return client
					.target("http://" + url + "/parkours/search/findAllByOrderByName")
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<List<Parkour>>() {});
		} catch (Exception ex) {
			return new ArrayList<>();
		}
	}

	public static void deleteParkour(Parkour parkour) {
		client
		.target("http://" + url + "/parkours/search/deleteByName")
		.queryParam("name", parkour.getName())
		.request(MediaType.APPLICATION_JSON)
		.get();
	}
}
