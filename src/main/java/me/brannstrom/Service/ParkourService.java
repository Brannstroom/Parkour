package me.brannstrom.Service;

import me.brannstrom.Model.Parkour;
import me.brannstrom.ParkourPlugin;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ParkourService {

    private static List<Parkour> cache = Collections.synchronizedList(new ArrayList<>());

    private static final Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
    private static final String url = ParkourPlugin.instance.getConfig().getString("api.url");

    public static Parkour update(Parkour parkour) {
        cache.remove(parkour);
        Parkour response = client
                .target("http://" + url + "/parkours")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(parkour, MediaType.APPLICATION_JSON), Response.class)
                .readEntity(new GenericType<Parkour>() {});
        if (response != null && response.getId() == null) return null;
        else return response;
    }

    public static Parkour getParkour(UUID uuid) {
        return cache.stream().filter(parkour -> parkour.getId().equals(uuid)).findFirst().orElseGet(() -> {

            Parkour response = client
                    .target("http://" + url + "/parkours/search/findById")
                    .queryParam("uuid", uuid)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Response.class)
                    .readEntity(new GenericType<Parkour>() {});
            if (response != null && response.getId() == null) return null;
            else {
                if (response != null) cache.add(response);
                return response;
            }
        });
    }

    public static Parkour getParkour(String name) {
        return cache.stream().filter(parkour -> parkour.getParkourName().equals(name)).findFirst().orElseGet(() -> {

            Parkour response = client
                    .target("http://" + url + "/parkours/search/findByParkourName")
                    .queryParam("name", name)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Response.class)
                    .readEntity(new GenericType<Parkour>() {});
            if (response != null && response.getId() == null) return null;
            else {
                if (response != null) cache.add(response);
                return response;
            }
        });
    }

    public static List<Parkour> getParkours(){
        try {
            return client
                    .target("http://" + url + "/parkours/search/findAllByOrderByParkourName")
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
                .target("http://" + url + "/parkours/search/deleteById")
                .queryParam("uuid", parkour.getId())
                .request(MediaType.APPLICATION_JSON)
                .get();
    }
}