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
public class ParkourStats {

    private UUID id;

    private UUID uuid;

    private String parkourName;

    long parkourTime;

    private Date updatedAt;

    private Date createdAt;

}