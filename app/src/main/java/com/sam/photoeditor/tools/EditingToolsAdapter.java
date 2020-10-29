package com.sam.photoeditor.tools;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.photoeditor.R;

import java.util.ArrayList;
import java.util.List;


public class EditingToolsAdapter extends RecyclerView.Adapter<EditingToolsAdapter.ViewHolder> {

    private List<ToolModel> mToolList = new ArrayList<>();
    private OnItemSelected mOnItemSelected;

    public EditingToolsAdapter(OnItemSelected onItemSelected) {
        mOnItemSelected = onItemSelected;
        mToolList.add(new ToolModel("Brush", R.drawable.mate_icon_brush, ToolType.BRUSH));
        mToolList.add(new ToolModel("Eraser", R.drawable.mate_icon_eraser, ToolType.ERASER));
        mToolList.add(new ToolModel("Text", R.drawable.mate_icon_text, ToolType.TEXT));
        mToolList.add(new ToolModel("Filter", R.drawable.mate_icon_filter, ToolType.FILTER));
        mToolList.add(new ToolModel("Emoji", R.drawable.mate_icon_emoji, ToolType.EMOJI));
        mToolList.add(new ToolModel("Sticker", R.drawable.mate_icon_sticker, ToolType.STICKER));
        mToolList.add(new ToolModel("GPS Tag", R.drawable.mate_icon_geotag_off, ToolType.GPSTAG));
        mToolList.add(new ToolModel("Owner", R.drawable.mate_icon_owner_off, ToolType.OWNER));
    }

    public void setLocationTagIcon(boolean status) {
        if (status) {
            mToolList.set(6, new ToolModel("GPS Tag", R.drawable.mate_icon_geotag_on, ToolType.GPSTAG));
            notifyDataSetChanged();
        } else {
            mToolList.set(6, new ToolModel("GPS Tag", R.drawable.mate_icon_geotag_off, ToolType.GPSTAG));
            notifyDataSetChanged();
        }
    }

    public void setOwnerIcon(boolean status) {
        if (status) {
            mToolList.set(7, new ToolModel("Owner", R.drawable.mate_icon_owner_on, ToolType.OWNER));
            notifyDataSetChanged();
        } else {
            mToolList.set(7, new ToolModel("Owner", R.drawable.mate_icon_owner_off, ToolType.OWNER));
            notifyDataSetChanged();
        }
    }

    public interface OnItemSelected {
        void onToolSelected(ToolType toolType);

        void onToolLongClick(ToolType toolType);
    }

    class ToolModel {
        private String mToolName;
        private int mToolIcon;
        private ToolType mToolType;

        ToolModel(String toolName, int toolIcon, ToolType toolType) {
            mToolName = toolName;
            mToolIcon = toolIcon;
            mToolType = toolType;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_editing_tools, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolModel item = mToolList.get(position);
        holder.txtTool.setText(item.mToolName);
        holder.imgToolIcon.setImageResource(item.mToolIcon);
    }

    @Override
    public int getItemCount() {
        return mToolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(View itemView) {
            super(itemView);
            imgToolIcon = itemView.findViewById(R.id.imgToolIcon);
            txtTool = itemView.findViewById(R.id.txtTool);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelected.onToolSelected(mToolList.get(getLayoutPosition()).mToolType);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemSelected.onToolLongClick(mToolList.get(getLayoutPosition()).mToolType);
                    return false;
                }
            });
        }
    }
}
