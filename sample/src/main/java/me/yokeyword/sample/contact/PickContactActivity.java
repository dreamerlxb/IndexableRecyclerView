package me.yokeyword.sample.contact;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.yokeyword.indexablerv.IndexableHeaderAdapter;
import me.yokeyword.indexablerv.IndexableLayout;
import me.yokeyword.indexablerv.SimpleFooterAdapter;
import me.yokeyword.indexablerv.SimpleHeaderAdapter;
import me.yokeyword.sample.R;
import me.yokeyword.sample.ToastUtil;

/**
 * Created by YoKey on 16/10/8.
 */
public class PickContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);
        getSupportActionBar().setTitle("联系人");
        IndexableLayout indexableLayout = findViewById(R.id.indexableLayout);

        indexableLayout.setLayoutManager(new LinearLayoutManager(this));
//        indexableLayout.setLayoutManager(new GridLayoutManager(this, 3));

        // setAdapter
        ContactAdapter mAdapter = new ContactAdapter(this);
        indexableLayout.setAdapter(mAdapter);
        // set Datas
        mAdapter.setDatas(initDatas());
        // set Material Design OverlayView
        indexableLayout.setOverlayStyle_MaterialDesign(Color.RED);

        // 全字母排序。  排序规则设置为：每个字母都会进行比较排序；速度较慢
        indexableLayout.setCompareMode(IndexableLayout.MODE_ALL_LETTERS);

        // set Listener
        mAdapter.setOnItemContentClickListener((v, originalPosition, currentPosition, entity) -> {
            if (originalPosition >= 0) {
                ToastUtil.showShort(PickContactActivity.this, "选中:" + entity.getNick() + "  当前位置:" + currentPosition + "  原始所在数组位置:" + originalPosition);
            } else {
                ToastUtil.showShort(PickContactActivity.this, "选中Header/Footer:" + entity.getNick() + "  当前位置:" + currentPosition);
            }
        });

        mAdapter.setOnItemTitleClickListener((v, currentPosition, indexTitle) -> ToastUtil.showShort(PickContactActivity.this, "选中:" + indexTitle + "  当前位置:" + currentPosition));

        // 添加我关心的人
        indexableLayout.addHeaderAdapter(new SimpleHeaderAdapter<>(mAdapter, "☆", "我关心的", initFavDatas()));

        // 构造函数里3个参数,分别对应 (IndexBar的字母索引, IndexTitle, 数据源), 不想显示哪个就传null, 数据源传null时,代表add一个普通的View
        MenuHeaderAdapter mMenuHeaderAdapter = new MenuHeaderAdapter("↑", null, initMenuDatas());
        // 添加菜单
        indexableLayout.addHeaderAdapter(mMenuHeaderAdapter);
        mMenuHeaderAdapter.setOnItemHeaderClickListener((v, currentPosition, entity) -> ToastUtil.showShort(PickContactActivity.this, entity.getMenuTitle()));

        // 这里BannerView只有一个Item, 添加一个长度为1的任意List作为第三个参数
        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
        BannerHeaderAdapter mBannerHeaderAdapter = new BannerHeaderAdapter(null, null, bannerList);
        // 添加 Banner
        indexableLayout.addHeaderAdapter(mBannerHeaderAdapter);

        // FooterView
        indexableLayout.addFooterAdapter(new SimpleFooterAdapter<>(mAdapter, "尾", "我是FooterView", initFavDatas()));
    }

    /**
     * 自定义的MenuHeader
     */
    class MenuHeaderAdapter extends IndexableHeaderAdapter<MenuEntity> {
        private static final int TYPE = 1;

        MenuHeaderAdapter(String index, String indexTitle, List<MenuEntity> datas) {
            super(index, indexTitle, datas);
        }

        @Override
        public int getItemViewType() {
            return TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            return new VH(LayoutInflater.from(PickContactActivity.this).inflate(R.layout.header_contact_menu, parent, false));
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, MenuEntity entity) {
            VH vh = (VH) holder;
            vh.tv.setText(entity.getMenuTitle());
            vh.img.setImageResource(entity.getMenuIconRes());
        }

        private class VH extends RecyclerView.ViewHolder {
            private TextView tv;
            private ImageView img;

            VH(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv_title);
                img = itemView.findViewById(R.id.img);
            }
        }
    }

    /**
     * 自定义的Banner Header
     */
    class BannerHeaderAdapter extends IndexableHeaderAdapter<String> {
        private static final int TYPE = 2;

        BannerHeaderAdapter(String index, String indexTitle, List<String> datas) {
            super(index, indexTitle, datas);
        }

        @Override
        public int getItemViewType() {
            return TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(PickContactActivity.this).inflate(R.layout.header_contact_banner, parent, false);
            VH holder = new VH(view);
            holder.img.setOnClickListener(v -> ToastUtil.showShort(PickContactActivity.this, "---点击了Banner---"));
            return holder;
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, String entity) {
            // 数据源为null时, 该方法不用实现
        }

        private class VH extends RecyclerView.ViewHolder {
            ImageView img;

            VH(View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
            }
        }
    }

    private List<UserEntity> initDatas() {
        List<UserEntity> list = new ArrayList<>();
        // 初始化数据
        List<String> contactStrings = Arrays.asList(getResources().getStringArray(R.array.contact_array));
        List<String> mobileStrings = Arrays.asList(getResources().getStringArray(R.array.mobile_array));
        for (int i = 0; i < contactStrings.size(); i++) {
            UserEntity contactEntity = new UserEntity(contactStrings.get(i), mobileStrings.get(i));
            list.add(contactEntity);
        }
        return list;
    }

    private List<UserEntity> initFavDatas() {
        List<UserEntity> list = new ArrayList<>();
        list.add(new UserEntity("张三", "10000"));
        list.add(new UserEntity("李四", "10001"));
        return list;
    }

    private List<MenuEntity> initMenuDatas() {
        List<MenuEntity> list = new ArrayList<>();
        list.add(new MenuEntity("新的朋友", R.mipmap.icon_1));
        list.add(new MenuEntity("群聊", R.mipmap.icon_2));
        list.add(new MenuEntity("标签", R.mipmap.icon_3));
        list.add(new MenuEntity("公众号", R.mipmap.icon_4));
        return list;
    }
}
