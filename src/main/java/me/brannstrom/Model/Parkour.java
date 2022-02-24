package me.brannstrom.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(of = {"uuid"})
public class Parkour {

    private UUID uuid;

    private String name;

    private String joinLocation;

    private String startLocation;

    private String finishLocation;

    private String holoLocation;

    private Date updatedAt;

    private Date createdAt;
}
