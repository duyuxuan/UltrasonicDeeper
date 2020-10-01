package com.golovkobalak.sonarapp.service;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.golovkobalak.sonarapp.model.SonarData;
import com.golovkobalak.sonarapp.repository.SonarDataRepository;
import com.google.gson.Gson;

import java.util.Arrays;

public class TrackingInterface {
    private Context context;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static Gson gson = new Gson();
    private static SonarDataRepository repo = new SonarDataRepository();

    public TrackingInterface(Context context) {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.context = context;
    }

    @JavascriptInterface
    public void saveTrackingList(String data) {
        try {
            final SonarData[] sonarDataArray = gson.fromJson(data, SonarData[].class);
            repo.saveList(Arrays.asList(sonarDataArray));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
