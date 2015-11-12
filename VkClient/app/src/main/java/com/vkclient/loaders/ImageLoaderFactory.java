package com.vkclient.loaders;

public class ImageLoaderFactory {
    public ImageLoader create() {
        return new CacheImageLoader(new NetworkImageLoader());
    }
}
