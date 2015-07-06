package com.vkclient.entities;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vkclient.supports.Logger;

public abstract class AbstractRequestListener extends VKRequest.VKRequestListener {
    @Override
    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
        super.attemptFailed(request, attemptNumber, totalAttempts);
        Logger.logError("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
    }

    @Override
    public void onError(VKError error) {
        super.onError(error);
        Logger.logError("VkDemoApp", "onError: " + error);
    }

    @Override
    public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
        super.onProgress(progressType, bytesLoaded, bytesTotal);
        Logger.logDebug("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
    }
}
