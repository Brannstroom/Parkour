package me.brannstrom.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(of = {"uuid"})
public class ParkourPlayer {

    private UUID uuid;

    private Long startTime;

    private Parkour parkour;

    private int taskId;

    public ParkourPlayer() {
    }
}
