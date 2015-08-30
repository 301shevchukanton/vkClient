package com.vkclient.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.podkaifom.vkclient.R;
import com.vkclient.supports.PhotoLoader;

public class PhotoViewFragment extends Fragment {
    private static String photoUrl = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewHierarchy = inflater.inflate(R.layout.fragment_photo_view, container, false);
        this.photoUrl = getActivity().getIntent().getStringExtra("photo");
        PhotoLoader.loadPhoto(getActivity(), photoUrl, (ImageView) viewHierarchy.findViewById(R.id.ivPhoto));
        return viewHierarchy;
    }


}
