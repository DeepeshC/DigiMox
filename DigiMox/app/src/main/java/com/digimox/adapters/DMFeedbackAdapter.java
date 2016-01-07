package com.digimox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.digimox.R;
import com.digimox.models.response.DMFeedback;

import java.util.ArrayList;

/**
 * Created by Deepesh on 21-Dec-15.
 */
public class DMFeedbackAdapter extends BaseAdapter {

    private static final int CURSOR_TEXT_COLUMN = 0;
    private ArrayList<DMFeedback> dmFeedbacks;
    private Context context;
    private ViewHolder holder = null;
    public ArrayList<String> rates;

    public DMFeedbackAdapter(Context context, ArrayList<DMFeedback> dmFeedbacks) {
        this.dmFeedbacks = dmFeedbacks;
        this.context = context;
        rates = new ArrayList<>(dmFeedbacks.size());
        for (int i = 0; i < dmFeedbacks.size(); i++) {
            rates.add(i, String.valueOf(0));
        }

    }

    @Override
    public Object getItem(int position) {
        return dmFeedbacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater mInflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_feedback_item, viewGroup,
                    false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.feedback_question);
            holder.ratingCount = (TextView) convertView.findViewById(R.id.star_count);
            holder.progress = (RatingBar) convertView
                    .findViewById(R.id.feedback_rating);
            holder.progress.setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(dmFeedbacks.get(position).getFeedbackLangQuestion());
        holder.progress
                .setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float rating, boolean fromUser) {
                        Integer myPosition = (Integer) ratingBar.getTag();
                        rates.set(myPosition, String.valueOf((int) rating));
                        LinearLayout parent = (LinearLayout) ratingBar.getParent();
                        TextView label = (TextView) parent.findViewById(R.id.star_count);
                        label.setText(String.valueOf((int) rating));
                    }
                });
        return convertView;
    }


    @Override
    public int getCount() {
        return dmFeedbacks.size();
    }

    static class ViewHolder {
        TextView text;
        TextView ratingCount;
        RatingBar progress;
    }

}