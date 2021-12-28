package com.jexinox.handlers;

import com.google.gson.JsonObject;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.List;

public interface ApiHandler {
    List<JsonObject> getUsers() throws ClientException, ApiException, InterruptedException;
}
