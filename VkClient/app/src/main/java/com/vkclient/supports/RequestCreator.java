package com.vkclient.supports;

import android.graphics.Bitmap;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

public class RequestCreator {

    static final String FRIENDS_COUNT = "500";
    static final String PHOTOS_COUNT = "100";
    static final String SORT_BY = "hints";
    static final String FRIENDS_REQUEST_PARAMS = "id,first_name,last_name,bdate,photo_200,photo_max,city";
    public static final String USER_BY_ID_PARAMS = "first_name,last_name,photo_200,photo_max_orig";
    public static final String FULL_USER_PARAMS = "id,first_name,last_name,bdate,city,photo_200,photo_max_orig,online," +
            "online_mobile,lists,domain,has_mobile,contacts,connections,site,education," +
            "universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message," +
            "status,last_seen,common_count,relation,relatives,counters,langs,personal";
    public static final String NEWS_COUNT = "100";
    public static final String DIALOGS_COUNT = "30";
    public static final String DIALOGS_PRIVIEW_LENGTH = "50";
    public static final String LIKES_ADD = "likes.add";
    public static final String LIKES_DELETE = "likes.delete";
    public static final String TYPE = "type";
    public static final String POST = "post";
    public static final String OWNER_ID = "owner_id";
    public static final String ITEM_ID = "item_id";
    public static final String FILTERS = "filters";
    public static final String PHOTO_SIZE = "photo_200";
    public static final String NEWSFEED_GET = "newsfeed.get";
    public static final String MESSAGES_GET_DIALOGS = "messages.getDialogs";
    public static final String MESSAGES_GET_HISTORY = "messages.getHistory";
    public static final String LIKES_IS_LIKED = "likes.isLiked";
    public static final String PREVIEW_LENGTH = "preview_length";
    public static final String ORDER = "order";
    public static final String PHOTOS_GET_ALL = "photos.getAll";
    public static final String EXTENDED = "extended";
    public static final String VK_TRUE = "1";
    public static final String EXTRA_PHOTO_SIZES = "photo_sizes";
    public static final String VK_FALSE = "0";

    public static VKRequest getUserById(String userId) {
        return VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, userId, VKApiConst.FIELDS,
                USER_BY_ID_PARAMS));
    }

    public static VKRequest getFullUserById(String userId) {
        return VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, userId, VKApiConst.FIELDS,
                FULL_USER_PARAMS));
    }

    public static VKRequest getNewsFeed() {
        return new VKRequest(NEWSFEED_GET, VKParameters.from(VKApiConst.COUNT, NEWS_COUNT, FILTERS, POST, VKApiConst.FIELDS, PHOTO_SIZE), VKRequest.HttpMethod.GET);
    }

    public static VKRequest getDialogs() {
        return new VKRequest(MESSAGES_GET_DIALOGS, VKParameters.from(VKApiConst.COUNT, DIALOGS_COUNT, PREVIEW_LENGTH, DIALOGS_PRIVIEW_LENGTH), VKRequest.HttpMethod.GET);
    }

    public static VKRequest getHistory(String count, String profileId) {
        return new VKRequest(MESSAGES_GET_HISTORY, VKParameters.from(VKApiConst.COUNT, count, VKApiConst.USER_ID, profileId), VKRequest.HttpMethod.GET);
    }

    public static VKRequest likePost(String ownerId, String itemId, Boolean like) {
        return new VKRequest(like ? LIKES_ADD : LIKES_DELETE, VKParameters.from(TYPE, POST, OWNER_ID, ownerId, ITEM_ID, itemId), VKRequest.HttpMethod.GET);
    }

    public static VKRequest postIsLiked(String ownerId, String itemId) {
        return new VKRequest(LIKES_IS_LIKED, VKParameters.from(TYPE, POST, OWNER_ID, ownerId, ITEM_ID, itemId), VKRequest.HttpMethod.GET);
    }

    public static VKRequest getFriends(String userId) {
        return VKApi.friends().get(VKParameters.from(VKApiConst.USER_ID, userId, ORDER, SORT_BY, VKApiConst.COUNT, FRIENDS_COUNT, VKApiConst.FIELDS, FRIENDS_REQUEST_PARAMS));
    }

    public static VKRequest uploadPhotoToUser(String userId, Bitmap photo) {
        return VKApi.uploadWallPhotoRequest(new VKUploadImage(photo, VKImageParameters.jpgImage(0.9f)), Integer.parseInt(userId), 0);
    }

    public static VKRequest getPhotosOfUser(String userId) {
        return new VKRequest(PHOTOS_GET_ALL, VKParameters.from(OWNER_ID, userId, EXTENDED, VK_TRUE, VKApiConst.COUNT, PHOTOS_COUNT, EXTRA_PHOTO_SIZES, VK_FALSE), VKRequest.HttpMethod.GET);
    }
}
