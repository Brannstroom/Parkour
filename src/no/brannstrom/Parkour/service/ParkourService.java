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

import org.bukkit.Bukkit;

import no.brannstrom.Parkour.model.Parkour;

public class ParkourService {
	
	private static List<Parkour> cache = Collections.synchronizedList(new ArrayList<>());
	
	private static Client client = ClientBuilder.newClient();
	private static String url = "127.0.0.1:8080";
	
	public static Parkour update(Parkour parkour) {
		Bukkit.broadcastMessage("F LOC: " + parkour.getFinishLocation());
		Bukkit.broadcastMessage("KL.1");
		Bukkit.broadcastMessage("-----------------------");
		Bukkit.broadcastMessage(parkour.getName());
		Bukkit.broadcastMessage(parkour.getJoinLocation());
		Bukkit.broadcastMessage(parkour.getStartLocation());
		Bukkit.broadcastMessage(parkour.getFinishLocation());
		Bukkit.broadcastMessage("-----------------------");
		Parkour response = client
				.target("http://" + url + "/parkours")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(parkour, MediaType.APPLICATION_JSON), Response.class)
				.readEntity(new GenericType<Parkour>() {});
		Bukkit.broadcastMessage("KL.2");
		if (response != null && response.getUuid() == null) return null;
		else return response;
	}

	public static Parkour getParkour(UUID uuid) {
		return cache.stream().filter(parkour -> parkour.getUuid().equals(uuid)).findFirst().orElseGet(() -> {

			Parkour response = client
					.target("http://" + url + "/parkours/search/findByUuid")
					.queryParam("uuid", uuid)
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<Parkour>() {});
			if (response != null && response.getUuid() == null) return null;
			else {
				if (response != null) cache.add(response);
				return response;
			}
		});
	}
	
	public static Parkour getParkour(String name) {
		return cache.stream().filter(parkour -> parkour.getName().equals(name)).findFirst().orElseGet(() -> {

			Parkour response = client
					.target("http://" + url + "/parkours/search/findByName")
					.queryParam("name", name)
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<Parkour>() {});
			if (response != null && response.getUuid() == null) return null;
			else {
				if (response != null) cache.add(response);
				return response;
			}
		});
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

	public static void unCache(UUID id) {
		unCache(getParkour(id));
	}

	public static void unCache(Parkour parkour) {
		cache.remove(parkour);
	}

	public static void deleteParkour(Parkour parkour) {
		client
		.target("http://" + url + "/parkours/search/deleteByUuid")
		.queryParam("uuid", parkour.getUuid())
		.request(MediaType.APPLICATION_JSON)
		.get();
	}
}