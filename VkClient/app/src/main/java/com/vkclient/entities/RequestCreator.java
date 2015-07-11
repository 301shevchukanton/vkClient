package com.vkclient.entities;

import android.graphics.Bitmap;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

public class RequestCreator {

    static final String FRIENDS_COUNT = "222";
    static final String SORT_BY = "hints";
    static final String FRIENDS_REQUEST_PARAMS = "id,first_name,last_name,bdate,photo_200,photo_max";

    public static VKRequest getUserById(String userId) {
        return VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, userId, VKApiConst.FIELDS,
                "first_name,last_name,photo_200,photo_max_orig"));
    }

    public static VKRequest getFullUserById(String userId) {
        return VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, userId, VKApiConst.FIELDS,
                "id,first_name,last_name,bdate,city,photo_200,photo_max_orig,online," +
                        "online_mobile,lists,domain,has_mobile,contacts,connections,site,education," +
                        "universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message," +
                        "status,last_seen,common_count,relation,relatives,counters,langs,personal"));
    }

    public static VKRequest getNewsFeed() {
        return new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.COUNT, "26", "filters", "post", VKApiConst.FIELDS, "photo_200"), VKRequest.HttpMethod.GET);
    }

    public static VKRequest getDialogs() {
        return new VKRequest("messages.getDialogs", VKParameters.from(VKApiConst.COUNT, "30", "preview_length", "50"), VKRequest.HttpMethod.GET);
    }

    public static VKRequest getHistory(String count, String profileId) {
        return new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.COUNT, count, VKApiConst.USER_ID, profileId), VKRequest.HttpMethod.GET);
    }

    public static VKRequest likePost(String ownerId, String itemId) {
        return new VKRequest("likes.add", VKParameters.from("type", "post", "owner_id", ownerId, "item_id", itemId), VKRequest.HttpMethod.GET);
    }

    public static VKRequest dislikePost(String ownerId, String itemId) {
        return new VKRequest("likes.delete", VKParameters.from("type", "post", "owner_id", ownerId, "item_id", itemId), VKRequest.HttpMethod.GET);
    }

    public static VKRequest postIsLiked(String ownerId, String itemId) {
        return new VKRequest("likes.isLiked", VKParameters.from("type", "post", "owner_id", ownerId, "item_id", itemId), VKRequest.HttpMethod.GET);
    }

    public static VKRequest getFriends(String userId) {
        return VKApi.friends().get(VKParameters.from(VKApiConst.USER_ID, userId, "order", SORT_BY, VKApiConst.COUNT, FRIENDS_COUNT, VKApiConst.FIELDS, FRIENDS_REQUEST_PARAMS));
    }

    public static VKRequest uploadPhotoToUser(String userId, Bitmap photo) {
        return VKApi.uploadWallPhotoRequest(new VKUploadImage(photo, VKImageParameters.jpgImage(0.9f)), Integer.parseInt(userId), 0);
    }

    public static VKRequest getPhotosOfUser(String userId) {
        return new VKRequest("photos.getAll", VKParameters.from("owner_id", userId, "extended", "1", "photo_sizes", "0"), VKRequest.HttpMethod.GET);
    }
}
