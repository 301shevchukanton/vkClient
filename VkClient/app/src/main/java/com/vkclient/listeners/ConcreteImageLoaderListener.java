package com.vkclient.listeners;

import com.vkclient.supports.Logger;

public class ConcreteImageLoaderListener implements ImageLoaderListener {

    @Override
    public void onLoad() {
        Logger.logDebug("ImageLoader:", "successful");
    }

    @Override
    public void onError() {
        Logger.logDebug("ImageLoader:", "error");
    }
}
