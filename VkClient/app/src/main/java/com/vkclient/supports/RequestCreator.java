package com.vkclient.supports;

import android.graphics.Bitmap;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

public class RequestCreator {

    private static final String FRIENDS_COUNT = "500";
    private static final String PHOTOS_COUNT = "100";
    private static final String PHOTO_SIZE = "photo_200";
    private static final String SORT_BY = "hints";
    private static final String ID = "id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String BDATE = "bdate";
    private static final String PHOTO_MAX = "photo_max";
    private static final String ONLINE = "online";
    private static final String PHOTO_MAX_ORIGINAL = "photo_max_orig";
    private static final String CITY = "city";
    private static final String ONLINE_MOBILE = "online_mobile";
    private static final String LISTS = "lists";
    private static final String DOMAIN = "domain";
    private static final String HAS_MOBILE = "has_mobile";
    private static final String CONTACTS = "contacts";
    private static final String CONNECTIONS = "connections";
    private static final String SITE = "site";
    private static final String EDUCATION = "education";
    private static final String UNIVERSITIES = "universities";
    private static final String SCHOOLS = "schools";
    private static final String CAN_POST = "can_post";
    private static final String CAN_SEE_ALL_POSTS = "can_see_all_posts";
    private static final String CAN_SEE_AUDIO = "can_see_audio";
    private static final String CAN_WRITE_PRIVATE_MESSAGE = "can_write_private_message";
    private static final String STATUS = "status";
    private static final String LAST_SEEN = "last_seen";
    private static final String COMMON_COUNT = "common_count";
    private static final String RELATION = "relation";
    private static final String RELATIVES = "relatives";
    private static final String COUNTERS = "counters";
    private static final String LANGUAGES = "langs";
    private static final String PERSONAL_INFO = "personal";
    private static final String NEWS_COUNT = "100";
    private static final String DIALOGS_COUNT = "30";
    private static final String DIALOGS_PRIVIEW_LENGTH = "50";
    private static final String LIKES_ADD = "likes.add";
    private static final String LIKES_DELETE = "likes.delete";
    private static final String TYPE = "type";
    private static final String POST = "post";
    private static final String OWNER_ID = "owner_" + ID;
    private static final String ITEM_ID = "item_" + ID;
    private static final String FILTERS = "filters";
    private static final String NEWSFEED_GET = "newsfeed.get";
    private static final String MESSAGES_GET_DIALOGS = "messages.getDialogs";
    private static final String MESSAGES_GET_HISTORY = "messages.getHistory";
    private static final String LIKES_IS_LIKED = "likes.isLiked";
    private static final String PREVIEW_LENGTH = "preview_length";
    private static final String ORDER = "order";
    private static final String PHOTOS_GET_ALL = "photos.getAll";
    private static final String EXTENDED = "extended";
    private static final String EXTRA_PHOTO_SIZES = "photo_sizes";
    private static final String VK_TRUE = "1";
    private static final String VK_FALSE = "0";
    private static final String FULL_USER_PARAMS[] = {ID, FIRST_NAME, LAST_NAME, BDATE, CITY, PHOTO_SIZE, PHOTO_MAX_ORIGINAL, ONLINE,
            ONLINE_MOBILE, LISTS, DOMAIN, HAS_MOBILE, CONTACTS, CONNECTIONS, SITE, EDUCATION,
            UNIVERSITIES, SCHOOLS, CAN_POST, CAN_SEE_ALL_POSTS, CAN_SEE_AUDIO, CAN_WRITE_PRIVATE_MESSAGE,
            STATUS, LAST_SEEN, COMMON_COUNT, RELATION, RELATIVES, COUNTERS, LANGUAGES, PERSONAL_INFO};


    private static final String FRIENDS_REQUEST_PARAMS[] = {ID, FIRST_NAME, LAST_NAME, BDATE, PHOTO_SIZE, PHOTO_MAX, CITY};

    private static final String USER_BY_ID_PARAMS[] = {FIRST_NAME, LAST_NAME, PHOTO_SIZE, PHOTO_MAX_ORIGINAL};

    private static String getParamsFromArray(String array[]) {
        String result = "";
        for (int i = 0; i < array.length; i++) {
            result += array[i] + ",";
        }
        return result;
    }

    public static VKRequest getUserById(String userId) {
        return VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, userId, VKApiConst.FIELDS,
                getParamsFromArray(USER_BY_ID_PARAMS)));
    }

    public static VKRequest getFullUserById(String userId) {
        return VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, userId, VKApiConst.FIELDS,
                getParamsFromArray(FULL_USER_PARAMS)));
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
        return VKApi.friends().get(VKParameters.from(VKApiConst.USER_ID, userId, ORDER, SORT_BY, VKApiConst.COUNT, FRIENDS_COUNT, VKApiConst.FIELDS, getParamsFromArray(FRIENDS_REQUEST_PARAMS)));
    }

    public static VKRequest uploadPhotoToUser(String userId, Bitmap photo) {
        return VKApi.uploadWallPhotoRequest(new VKUploadImage(photo, VKImageParameters.jpgImage(0.9f)), Integer.parseInt(userId), 0);
    }

    public static VKRequest getPhotosOfUser(String userId) {
        return new VKRequest(PHOTOS_GET_ALL, VKParameters.from(OWNER_ID, userId, EXTENDED, VK_TRUE, VKApiConst.COUNT, PHOTOS_COUNT, EXTRA_PHOTO_SIZES, VK_FALSE), VKRequest.HttpMethod.GET);
    }
}
