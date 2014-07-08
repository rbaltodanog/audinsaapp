package clinicaaudinsa.audiologia.Adapters;

import java.util.ArrayList;

import clinicaaudinsa.audiologia.businessdomain.Blog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import clinicaaudinsa.audiologia.R;

public class BlogItemAdapter extends ArrayAdapter<Blog> {
	private Context context;
	int layoutResourceId;
	private ArrayList<Blog> tweets;

	public BlogItemAdapter(Context context, int layoutResourceId, ArrayList<Blog> tweets) {
		super(context, layoutResourceId, tweets);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.tweets = tweets;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TweetHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new TweetHolder();
			holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
			holder.txtContent = (TextView)row.findViewById(R.id.txtContent);
			row.setTag(holder);
		}
		else
		{
			holder = (TweetHolder)row.getTag();
		}

		Blog tweet = tweets.get(position);
		holder.txtTitle.setText("El " + tweet.getTweetedDateAsString() + " a las " + tweet.getTweetedHourAsString() + ":");
		holder.txtContent.setText(tweet.getMessage());

		return row;
	}
	
	static class TweetHolder
    {
        TextView txtTitle;
        TextView txtContent;
    }
}