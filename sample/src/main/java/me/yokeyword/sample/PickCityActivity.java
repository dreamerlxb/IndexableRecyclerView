package me.yokeyword.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.yokeyword.indexablerv.DefaultHeaderAdapter;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * 选择城市
 * Created by YoKey on 16/10/7.
 */
public class PickCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_city);
        getSupportActionBar().setTitle("选择城市");
        IndexableLayout indexableLayout = (IndexableLayout) findViewById(R.id.indexableLayout);

        // 绑定Adapter
        indexableLayout.setAdapter(adapter);
        adapter.setDatas(initDatas());
        adapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<CityEntity>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, CityEntity entity) {
                Toast.makeText(PickCityActivity.this, "选中:" + entity.getName() + "  当前位置:" + currentPosition + "  原始所在数组位置:" + originalPosition, Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemIndexClickListener(new IndexableAdapter.OnItemIndexClickListener() {
            @Override
            public void onItemClick(View v, int currentPosition, String indexTitle) {
                Toast.makeText(PickCityActivity.this, "选中:" + indexTitle + "  当前位置:" + currentPosition, Toast.LENGTH_SHORT).show();
            }
        });

        // 添加 HeaderView   DefaultHeaderAdapter接收一个IndexableAdapter, 使其布局以及点击事件和IndexableAdapter一致
        // 如果想自定义布局,点击事件, 可传入 new IndexableHeaderAdapter
        indexableLayout.addHeaderAdapter(new DefaultHeaderAdapter<>(adapter, "热", "热门城市", iniyHotCityDatas()));
        indexableLayout.addHeaderAdapter(new DefaultHeaderAdapter<>(adapter, "定", "当前城市", iniyGPSCityDatas()));
    }

    private IndexableAdapter<CityEntity> adapter = new IndexableAdapter<CityEntity>() {
        @Override
        public String getIndexField(CityEntity data) {
            return data.getName();
        }

        @Override
        public void setIndexField(CityEntity data, String indexField) {
            data.setName(indexField);
        }

        @Override
        public RecyclerView.ViewHolder onCreateIndexViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(PickCityActivity.this).inflate(R.layout.item_index_city, parent, false);
            return new IndexVH(view);
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(PickCityActivity.this).inflate(R.layout.item_city, parent, false);
            return new ContentVH(view);
        }

        @Override
        public void onBindIndexViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
            IndexVH vh = (IndexVH) holder;
            vh.tv.setText(indexTitle);
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, CityEntity entity) {
            ContentVH vh = (ContentVH) holder;
            vh.tv.setText(entity.getName());
        }
    };

    class IndexVH extends RecyclerView.ViewHolder {
        TextView tv;

        public IndexVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }

    class ContentVH extends RecyclerView.ViewHolder {
        TextView tv;

        public ContentVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    private List<CityEntity> initDatas() {
        List<CityEntity> list = new ArrayList<>();
        List<String> cityStrings = Arrays.asList(getResources().getStringArray(R.array.city_array));
        for (String item : cityStrings) {
            CityEntity cityEntity = new CityEntity();
            cityEntity.setName(item);
            list.add(cityEntity);
        }
        return list;
    }

    private List<CityEntity> iniyHotCityDatas() {
        List<CityEntity> list = new ArrayList<>();
        list.add(new CityEntity("杭州市"));
        list.add(new CityEntity("北京市"));
        list.add(new CityEntity("上海市"));
        list.add(new CityEntity("广州市"));
        return list;
    }

    private List<CityEntity> iniyGPSCityDatas() {
        List<CityEntity> list = new ArrayList<>();
        list.add(new CityEntity("杭州市"));
        return list;
    }
}