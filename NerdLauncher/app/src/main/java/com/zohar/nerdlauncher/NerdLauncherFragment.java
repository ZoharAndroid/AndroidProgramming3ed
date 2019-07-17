package com.zohar.nerdlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NerdLauncherFragment extends Fragment {

    private static final String TAG = "NerdLauncherFragment";

    private RecyclerView mRecyclerView;


    public static Fragment newInstance(){
        return new NerdLauncherFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);


        setAdapter();

        return view;
    }



     private void setAdapter(){
         Intent startIntent = new Intent(Intent.ACTION_MAIN);
         startIntent.addCategory(Intent.CATEGORY_LAUNCHER);

         PackageManager packageManager = getActivity().getPackageManager();
         List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(startIntent, 0);

         Log.d(TAG, "发现 " + resolveInfos.size() + " activity"); // 35

         // 将进行排序
         Collections.sort(resolveInfos, new Comparator<ResolveInfo>() {
             @Override
             public int compare(ResolveInfo o1, ResolveInfo o2) {
                 PackageManager pm = getActivity().getPackageManager();
                 return String.CASE_INSENSITIVE_ORDER.compare(o1.loadLabel(pm).toString(), o2.loadLabel(pm).toString());
             }
         });

         mRecyclerView.setAdapter(new ActivityAdapter(resolveInfos));
     }


     private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private ResolveInfo mResolveInfo;

         public ActivityHolder(@NonNull View itemView) {
             super(itemView);
             mTextView = (TextView)itemView;
             mTextView.setOnClickListener(this);
         }

         public void bind(ResolveInfo resolveInfo){
             mResolveInfo = resolveInfo;
             PackageManager packageManager = getActivity().getPackageManager();
             String appName = resolveInfo.loadLabel(packageManager).toString();
             mTextView.setText(appName);
         }

         @Override
         public void onClick(View v) {
             // 点击相应的item来启动相应的程序
             ActivityInfo activityInfo = mResolveInfo.activityInfo;
             Intent intent = new Intent(Intent.ACTION_MAIN);
             Log.d(TAG,"activityInfo.applicationInfo.packageName:" + activityInfo.applicationInfo.packageName);
             Log.d(TAG,"activityInfo.packageName:" + activityInfo.packageName);
             intent.setClassName(activityInfo.packageName, activityInfo.name);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 为打开的任务创建新的任务栈
             startActivity(intent);
         }
     }

     private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{

        private List<ResolveInfo> mResolveInfos;

         public ActivityAdapter(List<ResolveInfo> resolveInfos) {
             this.mResolveInfos = resolveInfos;
         }

         @NonNull
         @Override
         public ActivityHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
             View view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
             ActivityHolder holder = new ActivityHolder(view);
             return holder;
         }

         @Override
         public void onBindViewHolder(@NonNull ActivityHolder activityHolder, int i) {
            ResolveInfo resolveInfo = mResolveInfos.get(i);
            activityHolder.bind(resolveInfo);
         }

         @Override
         public int getItemCount() {
             return mResolveInfos.size();
         }
     }

}
