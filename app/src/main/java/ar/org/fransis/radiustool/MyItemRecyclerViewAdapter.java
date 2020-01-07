package ar.org.fransis.radiustool;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ar.org.fransis.radiustool.ItemFragment.OnListFragmentInteractionListener;
import ar.org.fransis.radiustool.dummy.DummyContent.DummyItem;
import ar.org.fransis.radiustool.model.Result;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Result> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<Result> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getId() + ".");
        holder.mReplyMessageView.setText(mValues.get(position).getResponseType());
        holder.mTestNameView.setText(mValues.get(position).getTestName());
        holder.mTimeOutView.setText(mValues.get(position).getResponseTime() + " ms");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mReplyMessageView;
        public final TextView mTimeOutView;
        public final TextView mTestNameView;
        public Result mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mReplyMessageView = (TextView) view.findViewById(R.id.item_reply_message);
            mTimeOutView = (TextView) view.findViewById(R.id.item_timeout);
            mTestNameView = (TextView) view.findViewById(R.id.item_test_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mReplyMessageView.getText() + "'";
        }
    }
}
