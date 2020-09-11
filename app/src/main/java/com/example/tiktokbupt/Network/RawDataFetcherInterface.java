package com.example.tiktokbupt.Network;

public interface RawDataFetcherInterface {
    void onFetchSuccess(String rawJson);
    void onFetchFailure();
}
