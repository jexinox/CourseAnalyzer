package com.jexinox.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;

import java.util.ArrayList;
import java.util.List;

public class VkApiHandler implements ApiHandler {
    private final VkApiClient vk;
    private final UserActor actor;
    private final String groupId;

    public VkApiHandler(String groupToSearch) {
        this.groupId = groupToSearch;

        var transportClient = HttpTransportClient.getInstance();

        vk = new VkApiClient(transportClient);

        var APP_ID = 8032541;
        var ACCESS_TOKEN = "9fb5df665f0f5c19bf31ba047d37f11802bd5c0dac86d7b88d43333cfd460246041850da6aa444260979d";
        actor = new UserActor(APP_ID, ACCESS_TOKEN);
    }

    @Override
    public List<JsonObject> getUsers() throws ClientException, ApiException, InterruptedException {
        var ids = getMembersIds();
        var members = new ArrayList<JsonObject>();
        var jsonParser = new JsonParser();
        for (var i = 0; i < ids.size(); i += 1000) {
            var vkAnswer = jsonParser
                    .parse(vk
                            .users()
                            .get(actor)
                            .userIds(ids.subList(i, i + Math.min(ids.size() - i, 1000)))
                            .fields(Fields.BDATE, Fields.CITY, Fields.SEX)
                            .execute().toString()).getAsJsonArray();
            vkAnswer.forEach(a -> members.add(a.getAsJsonObject()));
            Thread.sleep(300);
        }

        return members;
    }

    private List<String> getMembersIds() throws ApiException, ClientException, InterruptedException {
        var request = vk
                .groups()
                .getMembers(actor)
                .groupId(groupId);

        var jsonParser = new JsonParser();

        var vkAnswer = jsonParser
                .parse(request
                        .execute()
                        .toString()).getAsJsonObject();

        var elementsCount = vkAnswer.get("count").getAsInt();
        var members = new ArrayList<String>();
        for (var i = 0; i < elementsCount; i += 1000) {
            vkAnswer = jsonParser
                    .parse(request
                            .offset(i)
                            .count(Math.min(elementsCount - i, 1000))
                            .execute()
                            .toString()).getAsJsonObject();

            var ids = vkAnswer.get("items").getAsJsonArray();
            ids.forEach(el -> members.add(el.getAsString()));

            Thread.sleep(300);
        }

        return members;
    }
}
