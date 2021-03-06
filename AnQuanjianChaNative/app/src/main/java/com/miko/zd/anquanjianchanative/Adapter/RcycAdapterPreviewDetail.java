package com.miko.zd.anquanjianchanative.Adapter;
/*
 * Created by zd on 2016/10/18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.miko.zd.anquanjianchanative.Bean.RealmBean.HistoryItemBean;
import com.miko.zd.anquanjianchanative.Bean.RealmBean.ItemBean3;
import com.miko.zd.anquanjianchanative.Bean.RealmBean.PathRealm;
import com.miko.zd.anquanjianchanative.R;
import com.miko.zd.anquanjianchanative.Selector.ViewPicPreview;
import com.miko.zd.anquanjianchanative.Selector.ViewPicSelector;

import java.io.File;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;

public class RcycAdapterPreviewDetail extends RecyclerView.Adapter<MyViewHolderExpendItem> {
    private int order;
    private Context context;
    public RcycAdapterGridReport []adapters;
    /**
     * 相机无法getIntent
     */
    public static String serialNum;
    public static String curPhotoName;
    public static int currPostion;
    private AbstractList<HistoryItemBean> historyBeans;

    public RcycAdapterPreviewDetail(AbstractList<HistoryItemBean>historyBeans, Context context) {
        this.context = context;
        this.historyBeans = historyBeans;
        adapters = new RcycAdapterGridReport[historyBeans.size()];
        this.order = context.getSharedPreferences("TempCheckInfo", Context.MODE_PRIVATE).getInt("order", -1);
    }

    @Override
    public MyViewHolderExpendItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolderExpendItem(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rcyc_edit_detail_check, parent, false));
    }


    @Override
    public void onBindViewHolder(MyViewHolderExpendItem holder, int position) {
        Realm realm = Realm.getDefaultInstance();
        ItemBean3 itemBean3 = realm.where(ItemBean3.class)
                .equalTo("serialNum",historyBeans.get(position).getSerialNum()).findFirst();
        holder.tvItemName.setText(itemBean3.getSerialNum()
                + "   " + itemBean3.getItemName());
        holder.edNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                realm.executeTransaction(realm15 -> historyBeans.get(position).setNote(s.toString()));
            }
        });
        if (!historyBeans.get(position).getNote().equals("")) {
            holder.edNote.setText(historyBeans.get(position).getNote());
        }
        holder.cb1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                realm.executeTransaction(realm14 -> historyBeans.get(position).setCheckBox(1));
                holder.cb2.setChecked(false);
                holder.cb3.setChecked(false);
            } else if (!holder.cb2.isChecked() && !holder.cb3.isChecked()) {
                realm.executeTransaction(realm1 -> historyBeans.get(position).setCheckBox(-1));
            }
        });
        holder.cb2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                realm.executeTransaction(realm13 -> historyBeans.get(position).setCheckBox(2));
                holder.cb1.setChecked(false);
                holder.cb3.setChecked(false);
            } else if (!holder.cb1.isChecked() && !holder.cb3.isChecked()) {
                realm.executeTransaction(realm12 -> historyBeans.get(position).setCheckBox(-1));
            }
        });

        holder.cb3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                realm.executeTransaction(realm12 -> historyBeans.get(position).setCheckBox(3));
                holder.cb1.setChecked(false);
                holder.cb2.setChecked(false);
            } else if (!holder.cb2.isChecked() && !holder.cb1.isChecked()) {
                realm.executeTransaction(realm12 ->
                        historyBeans.get(position).setCheckBox(-1));
            }
        });

        if (historyBeans.get(position).getCheckBox() != -1) {
            if (historyBeans.get(position).getCheckBox() == 1) {
                holder.cb1.setChecked(true);
            } else if (historyBeans.get(position).getCheckBox() == 2) {
                holder.cb2.setChecked(true);
            } else if (historyBeans.get(position).getCheckBox() == 3) {
                holder.cb3.setChecked(true);
            }
        }
        RealmList<PathRealm> pathList = historyBeans.get(position).getPathList();

        adapters[position] = new RcycAdapterGridReport(pathList, holder.recyclerView, context);
        adapters[position].setOnItemClickListener(new RcycAdapterGridReport.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(context, ViewPicPreview.class);
                ArrayList<String> list = new ArrayList<String>();
                list.add(pathList.get(pos).getPath());
                intent.putExtra("position", position);
                intent.putExtra("pos", pos);
                intent.putExtra("path", list);
                intent.putExtra("serialNum", historyBeans.get(position).getSerialNum());
                intent.putExtra("TYPE", ViewPicPreview.DELETE_PREVIEW);
                ((Activity) context).startActivityForResult(intent, 400);
            }

            @Override
            public void onFooterClick() {
                String[] select = {"从图库选择", "拍一张"};
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setItems(select, (dialog1, which) -> {
                            if (which == 0) {
                                Intent intent = new Intent(context, ViewPicSelector.class);
                                intent.putExtra("serialNum", historyBeans.get(position).getSerialNum());
                                intent.putExtra("position", position);

                                ((Activity) context).startActivityForResult(intent, 100);
                                dialog1.cancel();
                            } else {
                                File dir = new File(Environment.getExternalStorageDirectory() + "/" + "localTempImgDir");
                                if (!dir.exists()) dir.mkdirs();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                curPhotoName = String.valueOf(new Random().nextInt(1000000));
                                currPostion = position;
                                serialNum = historyBeans.get(position).getSerialNum();
                                File f = new File(dir, curPhotoName + ".jpg");//localTempImgDir和localTempImageFileName是自己定义的名字
                                Uri u = Uri.fromFile(f);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                                ((Activity) context).startActivityForResult(intent, 200);
                                dialog1.cancel();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        holder.recyclerView.setAdapter(adapters[position]);
        WrapContentLinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return historyBeans.size();
    }

    public interface OnItemClickListener {
        void onClick(int gp, int p, int pos);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
