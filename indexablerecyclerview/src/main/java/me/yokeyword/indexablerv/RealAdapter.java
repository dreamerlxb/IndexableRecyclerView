package me.yokeyword.indexablerv;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by YoKey on 16/10/6.
 */
@SuppressWarnings("unchecked")
class RealAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<EntityWrapper<T>> mDatasList = new ArrayList<>();
    private ArrayList<EntityWrapper<T>> mHeaderDatasList = new ArrayList<>();
    private IndexableAdapter<T> mAdapter;

    private SparseArray<IndexableHeaderAdapter> mHeaderAdapterMap = new SparseArray<>();

    private IndexableAdapter.OnItemIndexClickListener mIndexClickListener;
    private IndexableAdapter.OnItemContentClickListener<T> mContentClickListener;

    void setIndexableAdapter(IndexableAdapter<T> adapter) {
        this.mAdapter = adapter;
    }

    void addIndexableHeaderAdapter(IndexableHeaderAdapter adapter) {
        mHeaderDatasList.addAll(0, adapter.getDatas());
        mHeaderAdapterMap.put(adapter.getItemViewType(), adapter);
        notifyDataSetChanged();
    }

    void addDatas(ArrayList<EntityWrapper<T>> datas) {
        mDatasList.clear();
        mDatasList.addAll(mHeaderDatasList);
        mDatasList.addAll(datas);
        notifyDataSetChanged();
    }

    ArrayList<EntityWrapper<T>> getItems() {
        return mDatasList;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatasList.get(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final RecyclerView.ViewHolder holder;

        if (viewType == EntityWrapper.TYPE_INDEX) {
            holder = mAdapter.onCreateIndexViewHolder(parent);
        } else if (viewType == EntityWrapper.TYPE_CONTENT) {
            holder = mAdapter.onCreateContentViewHolder(parent);
        } else {
            IndexableHeaderAdapter adapter = mHeaderAdapterMap.get(viewType);
            holder = adapter.onCreateContentViewHolder(parent);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                EntityWrapper<T> wrapper = mDatasList.get(position);
                if (viewType == EntityWrapper.TYPE_INDEX && mIndexClickListener != null) {
                    mIndexClickListener.onItemClick(v, position, wrapper.getIndexTitle());
                } else if (viewType == EntityWrapper.TYPE_CONTENT && mContentClickListener != null) {
                    mContentClickListener.onItemClick(v, wrapper.getOriginalPosition(), position, wrapper.getData());
                } else {

                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EntityWrapper<T> item = mDatasList.get(position);

        int viewType = getItemViewType(position);
        if (viewType == EntityWrapper.TYPE_INDEX) {
            mAdapter.onBindIndexViewHolder(holder, item.getIndexTitle());
        } else if (viewType == EntityWrapper.TYPE_CONTENT) {
            mAdapter.onBindContentViewHolder(holder, item.getData());
        } else {
            IndexableHeaderAdapter adapter = mHeaderAdapterMap.get(viewType);
            adapter.onBindContentViewHolder(holder, item.getData());
        }
    }

    @Override
    public int getItemCount() {
        return mDatasList.size();
    }

    void setOnItemIndexClickListener(IndexableAdapter.OnItemIndexClickListener listener) {
        this.mIndexClickListener = listener;
    }

    void setOnItemContentClickListener(IndexableAdapter.OnItemContentClickListener<T> listener) {
        this.mContentClickListener = listener;
    }
}