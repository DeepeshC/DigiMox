package com.digimox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.digimox.R
import com.digimox.models.response.DMFeedback
import java.util.*

/**
 * Created by Deepesh on 21-Dec-15.
 */
class DMFeedbackAdapter(private val context: Context, private val dmFeedbacks: ArrayList<DMFeedback>) : BaseAdapter() {
    private var holder: ViewHolder? = null
    var rates: ArrayList<String> = ArrayList(dmFeedbacks.size)

    init {
        for (i in dmFeedbacks.indices) {
            rates.add(i, 0.toString())
        }

    }

    override fun getItem(position: Int): Any {
        return dmFeedbacks[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        var convertView = convertView
        val mInflater = LayoutInflater.from(context)

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_feedback_item, viewGroup,
                    false)
            holder = ViewHolder()
            holder!!.text = convertView!!.findViewById<View>(R.id.feedback_question) as TextView
            holder!!.ratingCount = convertView.findViewById<View>(R.id.star_count) as TextView
            holder!!.progress = convertView
                    .findViewById<View>(R.id.feedback_rating) as RatingBar
            holder!!.progress!!.tag = position
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder!!.text!!.text = dmFeedbacks[position].feedbackLangQuestion
        holder!!.progress!!.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val myPosition = ratingBar.tag as Int
            rates[myPosition] = rating.toInt().toString()
            val parent = ratingBar.parent as LinearLayout
            val label = parent.findViewById<View>(R.id.star_count) as TextView
            label.text = rating.toInt().toString()
        }
        return convertView
    }


    override fun getCount(): Int {
        return dmFeedbacks.size
    }

    internal class ViewHolder {
        var text: TextView? = null
        var ratingCount: TextView? = null
        var progress: RatingBar? = null
    }

    companion object {

        private val CURSOR_TEXT_COLUMN = 0
    }

}