package com.vkclient.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.example.podkaifom.vkclient.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkclient.adapters.NewsListAdapter;
import com.vkclient.entities.AbstractRequestListener;
import com.vkclient.entities.News;
import com.vkclient.entities.RequestCreator;
import com.vkclient.supports.JSONParser;
import com.vkclient.supports.Loger;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends VkSdkActivity {
    private VKRequest currentRequest;
    private ListView listView;
    private List<News> news = new ArrayList<>();
    private NewsListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_news);
        super.onCreateDrawer();
        listView = (ListView)findViewById(R.id.lvNews);
        Object items = getLastNonConfigurationInstance();
        if (items != null) {
            this.news =  ((List<News>) items);
            this.listAdapter = new NewsListAdapter(this,this.news);
            this.listView.setAdapter(this.listAdapter);
            this.listAdapter.notifyDataSetChanged();
        }
        else if (VKSdk.wakeUpSession()) {
            startLoading();
        }
       // this.listView.setOnItemClickListener(this.dialogClickListener);
        this.listAdapter = new NewsListAdapter(this,this.news);
        this.listView.setAdapter(this.listAdapter);
    }

    private void startLoading() {
        if (this.currentRequest != null) {
            this.currentRequest.cancel();
        }
        this.currentRequest = RequestCreator.getNewsFeed();
        this.currentRequest.executeWithListener(this.getHistoryRequestListener);
    }
    private final AbstractRequestListener getHistoryRequestListener = new AbstractRequestListener() {
        @Override
        public void onComplete(final VKResponse response) {
            super.onComplete(response);
            Loger.logDebug("profid", response.responseString);
            news.clear();
            try {
                JSONParser newsFeed = new JSONParser(response.json);
                newsFeed.parsePosts();
                VKRequest[] myrequests = new VKRequest[newsFeed.feedLength()];
                for (int i = 0; i < newsFeed.feedLength(); i++ ) {
                    news.add(News.parse(newsFeed.getPost(i)));
                    news.get(i).getPostSourceDate(response.json);
                }
            }
            catch (Exception e){
                Loger.logDebug("profid", e.toString());
            }
            listAdapter.notifyDataSetChanged();
        }
    };
}
