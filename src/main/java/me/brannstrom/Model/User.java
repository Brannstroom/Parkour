package me.brannstrom.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = false, allowGetters = false)
@Data
@EqualsAndHashCode(of = {"uuid"})
public class User {

    private int id;

    private UUID uuid;

    private String username;

    private int urank;

    private int drank;

    private int srank;

    private boolean vanishEnabled;

    private boolean socialAgentEnabled;

    private Date logoutTime;

    private Date updatedAt;

    private Date createdAt;

    @JsonIgnore
    public boolean isBygger() { return getUrank() >= 3; }

    @JsonIgnore
    public boolean isStab() {
        return getUrank() >= 5;
    }

    @JsonIgnore
    public boolean isSrMod() {
        return getUrank() >= 6;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return getUrank() == 10;
    }

    @JsonIgnore
    public boolean isSponsor() { return getDrank() >= System.currentTimeMillis() / 1000; }

    @JsonIgnore
    public boolean isStjerne() {
        return getSrank() >= System.currentTimeMillis() / 1000;
    }

}
