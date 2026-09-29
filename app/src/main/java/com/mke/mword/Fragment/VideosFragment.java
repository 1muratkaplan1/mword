package com.mke.mword.Fragment;

import static com.mke.mword.Database.Constants.DB_VIDEOS_PAGES;
import static com.mke.mword.MainActivity.mkdb;

import android.content.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.*;

import com.bumptech.glide.*;
import com.bumptech.glide.request.*;
import com.mke.mword.Utils.*;

import java.util.*;

import com.mke.mword.R;


public class VideosFragment extends androidx.fragment.app.Fragment {

    public static Context gVideosFragmentContext;

    public static ExoPlayerRecyclerView mRecyclerView=null;

    private ArrayList<MediaObject> mediaObjectList = new ArrayList<MediaObject>();
    private MediaRecyclerAdapter mAdapter;
    private boolean firstTime = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.videos_fragment, container, false);

        gVideosFragmentContext = this.getContext();

        mRecyclerView = v.findViewById(R.id.exoPlayerRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(gVideosFragmentContext));
        // Prepare demo content
        prepareVideoList();

        //set data object
        mRecyclerView.setMediaObjects(mediaObjectList);
        mAdapter = new MediaRecyclerAdapter(mediaObjectList , initGlide());

        //Set Adapter
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.smoothScrollToPosition(1);
        //mRecyclerView.smoothScrollToPosition(1);
 /*   if (firstTime) {
      new Handler(Looper.getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
          mRecyclerView.playVideo(false);
        }
      });
      firstTime = false;
    }*/

        return v;
    }

    public static androidx.fragment.app.Fragment newInstance(String text) {

        VideosFragment  f = new VideosFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();
       return Glide.with(gVideosFragmentContext)
                .setDefaultRequestOptions(options);
    }

    public void stopPlay(){
        if (mRecyclerView != null) {
            mRecyclerView.releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        stopPlay();
        super.onDestroy();
    }

    private void prepareVideoList()
    {
        for (int i = 0; i < mkdb.GetWVSSize(DB_VIDEOS_PAGES,0); i++) {
            String szFileName=mkdb.GetEngWVS(DB_VIDEOS_PAGES,0,i);
            String filename=szFileName.substring(szFileName.lastIndexOf("/")+1);
            MediaObject mediaObject = new MediaObject();
            mediaObject.setId(i);
//            mediaObject.setUserHandle("User" + i);
            mediaObject.setTitle(filename);
            mediaObject.setCoverUrl(szFileName);
            mediaObject.setUrl(szFileName);
            mediaObjectList.add(mediaObject);
        }
    }

}





