package com.sam.photoeditor.filters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.photoeditor.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoFilter;

import static ja.burhanrashid52.photoeditor.PhotoFilter.NONE;

/**
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.2
 * @since 5/23/2018
 */
public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {

    private FilterListener mFilterListener;
    private List<Pair<String, PhotoFilter>> mPairList = new ArrayList<>();

    public FilterViewAdapter(FilterListener filterListener) {
        mFilterListener = filterListener;
        setupFilters();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, PhotoFilter> filterPair = mPairList.get(position);
        Bitmap fromAsset = getBitmapFromAsset(holder.itemView.getContext(), filterPair.first);
        holder.mImageFilterView.setImageBitmap(fromAsset);
//        holder.mTxtFilterName.setText(filterPair.second.name().replace("_", " "));
        switch (filterPair.second) {
            case NONE:
                holder.mTxtFilterName.setText("Gốc");
                break;
            case AUTO_FIX:
                holder.mTxtFilterName.setText("Tự động");
                break;
            case BRIGHTNESS:
                holder.mTxtFilterName.setText("Sáng");
                break;
            case CONTRAST:
                holder.mTxtFilterName.setText("Tương phản");
                break;
            case DOCUMENTARY:
                holder.mTxtFilterName.setText("Tài liệu cũ");
                break;
            case DUE_TONE:
                holder.mTxtFilterName.setText("Tone xanh");
                break;
            case FILL_LIGHT:
                holder.mTxtFilterName.setText("Nhẹ nhàng");
                break;
            case FISH_EYE:
                holder.mTxtFilterName.setText("Mắt cá");
                break;
            case GRAIN:
                holder.mTxtFilterName.setText("Hạt");
                break;
            case GRAY_SCALE:
                holder.mTxtFilterName.setText("Đen trắng");
                break;
            case LOMISH:
                holder.mTxtFilterName.setText("Hoài cổ");
                break;
            case NEGATIVE:
                holder.mTxtFilterName.setText("Âm bản");
                break;
            case POSTERIZE:
                holder.mTxtFilterName.setText("Tranh vẽ");
                break;
            case SATURATE:
                holder.mTxtFilterName.setText("Rự rỡ");
                break;
            case SEPIA:
                holder.mTxtFilterName.setText("Nâu cổ điển");
                break;
            case SHARPEN:
                holder.mTxtFilterName.setText("Sắc nét");
                break;
            case TEMPERATURE:
                holder.mTxtFilterName.setText("Ấm áp");
                break;
            case TINT:
                holder.mTxtFilterName.setText("Tím");
                break;
            case VIGNETTE:
                holder.mTxtFilterName.setText("Vintage");
                break;
            case CROSS_PROCESS:
                holder.mTxtFilterName.setText("Giả lập film");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mPairList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageFilterView;
        TextView mTxtFilterName;

        ViewHolder(View itemView) {
            super(itemView);
            mImageFilterView = itemView.findViewById(R.id.imgFilterView);
            mTxtFilterName = itemView.findViewById(R.id.txtFilterName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterListener.onFilterSelected(mPairList.get(getLayoutPosition()).second);
                }
            });
        }
    }

    private Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
            return BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupFilters() {
        mPairList.add(new Pair<>("filters/original.jpg", NONE));
        mPairList.add(new Pair<>("filters/auto_fix.png", PhotoFilter.AUTO_FIX));
        mPairList.add(new Pair<>("filters/brightness.png", PhotoFilter.BRIGHTNESS));
        mPairList.add(new Pair<>("filters/contrast.png", PhotoFilter.CONTRAST));
        mPairList.add(new Pair<>("filters/documentary.png", PhotoFilter.DOCUMENTARY));
        mPairList.add(new Pair<>("filters/dual_tone.png", PhotoFilter.DUE_TONE));
        mPairList.add(new Pair<>("filters/fill_light.png", PhotoFilter.FILL_LIGHT));
        mPairList.add(new Pair<>("filters/fish_eye.png", PhotoFilter.FISH_EYE));
        mPairList.add(new Pair<>("filters/grain.png", PhotoFilter.GRAIN));
        mPairList.add(new Pair<>("filters/gray_scale.png", PhotoFilter.GRAY_SCALE));
        mPairList.add(new Pair<>("filters/lomish.png", PhotoFilter.LOMISH));
        mPairList.add(new Pair<>("filters/negative.png", PhotoFilter.NEGATIVE));
        mPairList.add(new Pair<>("filters/posterize.png", PhotoFilter.POSTERIZE));
        mPairList.add(new Pair<>("filters/saturate.png", PhotoFilter.SATURATE));
        mPairList.add(new Pair<>("filters/sepia.png", PhotoFilter.SEPIA));
        mPairList.add(new Pair<>("filters/sharpen.png", PhotoFilter.SHARPEN));
        mPairList.add(new Pair<>("filters/temprature.png", PhotoFilter.TEMPERATURE));
        mPairList.add(new Pair<>("filters/tint.png", PhotoFilter.TINT));
        mPairList.add(new Pair<>("filters/vignette.png", PhotoFilter.VIGNETTE));
        mPairList.add(new Pair<>("filters/cross_process.png", PhotoFilter.CROSS_PROCESS));
    }
}
