package com.example.doanthuexe.service;

import com.example.doanthuexe.models.Car;
import com.example.doanthuexe.models.TripLocation;

import java.util.List;

public interface CallBackListLocation {
    void onCallBackListLocation(List<TripLocation> locations);
}
