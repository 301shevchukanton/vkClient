package com.vkclient.entities;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vkclient.supports.Loger;

public abstract class AbstractRequestListener extends VKRequest.VKRequestListener{
    @Override
    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
        super.attemptFailed(request, attemptNumber, totalAttempts);
        Loger.logError("VkDemoApp", "attemptFailed " + request + " " + attemptNumber + " " + totalAttempts);
    }

    @Override
    public void onError(VKError error) {
        super.onError(error);
        Loger.logError("VkDemoApp", "onError: " + error);
    }

    @Override
    public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
        super.onProgress(progressType, bytesLoaded, bytesTotal);
        Loger.logDebug("VkDemoApp", "onProgress " + progressType + " " + bytesLoaded + " " + bytesTotal);
    }
}
