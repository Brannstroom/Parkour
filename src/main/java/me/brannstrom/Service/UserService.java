package me.brannstrom.Service;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.brannstrom.Model.User;
import me.brannstrom.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

public class UserService {

    private static final Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
    private static final String url = ParkourPlugin.instance.getConfig().getString("api.url");

    public static List<User> cache = Collections.synchronizedList(new ArrayList<>());

    public static void update(User user) {

        User response = client
                .target("http://" + url + "/users")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class)
                .readEntity(new GenericType<User>() {
                });
        if (response != null && response.getUuid() == null) {
            throw new Error("Failed to save User '" + user.toString() + "' to database!");
        } else {
            unCache(response);
            unCacheBungee(user);
        }
    }

    public static User getUser(UUID uuid) {
        return cache.stream().filter(user -> user.getUuid().equals(uuid)).findFirst().orElseGet(() -> {

            User response = client
                    .target("http://" + url + "/users/search/findByUuid")
                    .queryParam("uuid", uuid)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Response.class)
                    .readEntity(new GenericType<User>() {
                    });
            if (response != null && response.getUuid() == null) return null;
            else {
                if (response != null) {
                    cache.add(response);
                }
                return response;
            }
        });
    }

    public static User getUser(String username) {
        return cache.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst().orElseGet(() -> {
            try {
                return client
                        .target("http://" + url + "/users/search/findByUsername")
                        .queryParam("username", username)
                        .request(MediaType.APPLICATION_JSON)
                        .get(Response.class)
                        .readEntity(new GenericType<List<User>>() {
                        }).get(0);
            } catch (Exception ex) {
                return null;
            }
        });
    }

    public static void unCache(UUID uniqueId) {
        unCache(getUser(uniqueId));
    }

    public static void unCache(User user) {
        cache.remove(user);
    }

    private static void unCacheBungee(User user) {
        Player p = Bukkit.getPlayer(user.getUuid());
        if (p != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("user-uncache");
            out.writeUTF(user.getUuid().toString());
            p.sendPluginMessage(ParkourPlugin.instance, "spillere:bungee", out.toByteArray());
        }
    }
}
