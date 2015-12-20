package org.stream.split.voicenotification.Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.stream.split.voicenotification.Enities.AppInfoEntity;
import org.stream.split.voicenotification.DataAccessLayer.DBHelper;
import org.stream.split.voicenotification.Enities.NotificationEntity;
import org.stream.split.voicenotification.Fragments.AppDetailsFragment;
import org.stream.split.voicenotification.R;

import java.util.List;

/**
 * Created by split on 2015-10-20.
 */
public class NotificationsHistoryAdapter extends RecyclerView.Adapter<NotificationsHistoryAdapter.ViewHolder> {

    static final public String TAG = "NotificationsHistoryAdapter";
    private Context mContext;
    private List<NotificationEntity> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public ImageButton mImgBtn;
        public NotificationEntity mNotificationEntity;


        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.history_app_name);
            mImgBtn = (ImageButton) v.findViewById(R.id.history_app_add);
            mImgBtn.setOnClickListener(this);
            mTextView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(mContext instanceof Activity)
            {
                Fragment fragment = AppDetailsFragment.newInstance(new Gson().toJson(mNotificationEntity));
                FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_content, fragment)
                        .addToBackStack("notificationdetails_"+ mNotificationEntity.getID())
                .commit();
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotificationsHistoryAdapter(List<NotificationEntity> notificationHistory, Context context) {
        mDataset = notificationHistory;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationsHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        NotificationEntity entity = mDataset.get(position);
        holder.mNotificationEntity = entity;

        holder.mTextView.setText(entity.getApplicationLabel());
        holder.mTextView.setTag(entity);

        holder.mImgBtn.setTag(entity);
        if(entity.isFollowed())
            holder.mImgBtn.setImageResource(R.drawable.ic_delete_app);
        else
            holder.mImgBtn.setImageResource(R.drawable.ic_add_applications);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addItem(NotificationEntity entity)
    {
        mDataset.add(entity);
    }

    public void refresh()
    {
        mDataset.clear();
        DBHelper db = new DBHelper(mContext);
        mDataset.addAll(db.getAllNotification());
        db.close();
        this.notifyDataSetChanged();
    }


}
