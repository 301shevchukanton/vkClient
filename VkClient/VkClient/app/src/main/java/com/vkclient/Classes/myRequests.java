package com.vkclient.Classes;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;

/**
 * Created by pod kaifom on 04.06.2015.
 */
public class myRequests {

    public static VKRequest getUserById(String user_id)
    {
        return VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, user_id, VKApiConst.FIELDS,
                "first_name,last_name,photo_200"));
    }

    public static VKRequest getFullUserById(String user_id)
    {
        return VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, user_id, VKApiConst.FIELDS,
                "id,first_name,last_name,bdate,city,photo_200,online," +
                        "online_mobile,lists,domain,has_mobile,contacts,connections,site,education," +
                        "universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message," +
                        "status,last_seen,common_count,relation,relatives,counters,langs,personal"));
    }

    public static VKRequest getDialogs()
    {
        return new VKRequest("messages.getDialogs", VKParameters.from(VKApiConst.COUNT, "30", "preview_length", "50"), VKRequest.HttpMethod.GET);
    }
    public static VKRequest getFriends(String user_id)
    {
        return VKApi.friends().get(VKParameters.from(VKApiConst.USER_ID, user_id, "order", "hints", VKApiConst.COUNT, "222",  VKApiConst.FIELDS, "id,first_name,last_name,bdate,photo_200"));
    }
}
