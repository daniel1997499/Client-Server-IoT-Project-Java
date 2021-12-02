package com.example.demo.Model;

import com.example.demo.Constants.Colors;

public class AppContext {
    private Colors globalColor;

    public AppContext() {
    }

    public AppContext(Colors globalColor) {
        this.globalColor = globalColor;
    }

    public Colors getGlobalColor() {
        return globalColor;
    }

    public void setGlobalColor(Colors globalColor) {
        this.globalColor = globalColor;
    }
}
