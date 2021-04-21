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

import no.brannstrom.Parkour.model.Parkour;
import no.brannstrom.Parkour.model.ParkourStats;

public class ParkourStatsService {
	
	private static List<ParkourStats> cache = Collections.synchronizedList(new ArrayList<>());
	
	private static Client client = ClientBuilder.newClient();
	private static String url = "127.0.0.1";
	
	public static ParkourStats update(ParkourStats parkourStats) {
		ParkourStats response = client
				.target("http://" + url + "/parkours")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(parkourStats, MediaType.APPLICATION_JSON), Response.class)
				.readEntity(new GenericType<ParkourStats>() {});
		if (response != null && response.getId() == null) return null;
		else return response;
	}

	public static ParkourStats getParkourStats(UUID uuid) {
		return cache.stream().filter(parkourStats -> parkourStats.getUuid().equals(uuid)).findFirst().orElseGet(() -> {

			ParkourStats response = client
					.target("http://" + url + "/parkourStatses/search/findByUuid")
					.queryParam("uuid", uuid)
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<ParkourStats>() {});
			if (response != null && response.getUuid() == null) return null;
			else {
				if (response != null) cache.add(response);
				return response;
			}
		});
	}
	
	public static ParkourStats getBestTimeOnParkour(UUID uuid, String name) {
		ParkourStats response = client
				.target("http://" + url + "/parkourStatses/search/getBestTimeOnParkour")
				.queryParam("uuid", uuid)
				.queryParam("name", name)
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<ParkourStats>() {});
		if (response != null && response.getId() == null) return null;
		else return response;
	}
	
	public static ParkourStats getParkourRecord(Parkour parkour) {
		ParkourStats response = client
				.target("http://" + url + "/parkourStatses/search/getParkourRecord")
				.queryParam("name", parkour.getName())
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<ParkourStats>() {});
		if (response != null && response.getId() == null) return null;
		else return response;
	}
	
	public static List<ParkourStats> getParkourRecordTop10(Parkour parkour){
		try {
			return client
					.target("http://" + url + "/parkourStatses/search/getParkourRecordTopTen")
					.queryParam("name", parkour.getName())
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<List<ParkourStats>>() {});
		} catch (Exception ex) {
			return new ArrayList<>();
		}
	}
	
	public static Integer getParkourPlacement(UUID uuid, String name) {
		try {
			return client
					.target("http://" + url + "/parkourStatses/search/findParkourPlacement")
					.queryParam("uuid", uuid.toString())
					.queryParam("name", name)
					.request(MediaType.APPLICATION_JSON)
					.get(Response.class)
					.readEntity(new GenericType<Integer>() {});
		} catch(Exception e) {
			return 0;
		}
	}

	public static void unCache(UUID uuid) {
		unCache(getParkourStats(uuid));
	}

	public static void unCache(ParkourStats parkour) {
		cache.remove(parkour);
	}

	public static void deleteParkourStats(ParkourStats parkourStats) {
		client
		.target("http://" + url + "/parkourStatses/search/deleteByUuid")
		.queryParam("name", parkourStats.getUuid())
		.request(MediaType.APPLICATION_JSON)
		.get();
	}
	
	public static void deleteParkourStatsOnParkour(Parkour parkour) {
		client
		.target("https://" + url + "/parkourStatses/search/deleteParkourStatsOnParkour")
		.queryParam("name", parkour.getName())
		.request(MediaType.APPLICATION_JSON)
		.get();
	}
}