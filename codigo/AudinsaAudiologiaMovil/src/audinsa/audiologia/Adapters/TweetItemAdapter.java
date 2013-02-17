package audinsa.audiologia.Adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import audinsa.audiologia.R;
import audinsa.audiologia.businessdomain.Tweet;

public class TweetItemAdapter extends ArrayAdapter<Tweet> {
	private Context context;
	int layoutResourceId;
	private ArrayList<Tweet> tweets;

	public TweetItemAdapter(Context context, int layoutResourceId, ArrayList<Tweet> tweets) {
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

		Tweet tweet = tweets.get(position);
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