package com.github.StevenDesroches.HorseSkill.datatype;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RpgHorseSerializationManager {

    private Gson gson;

    public RpgHorseSerializationManager() {
        this.gson = createGsonInstance();
    }

    private Gson createGsonInstance() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gsonBuilder.disableHtmlEscaping();
        return gsonBuilder.create();
    }

    public String serialize(RpgHorse rpgHorse) {
        return this.gson.toJson(rpgHorse);
    }

    public RpgHorse deserialize(String json) {
        return this.gson.fromJson(json, RpgHorse.class);
    }
}
