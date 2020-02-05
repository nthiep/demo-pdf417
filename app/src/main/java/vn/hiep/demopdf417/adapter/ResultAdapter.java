package vn.hiep.demopdf417.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.hiep.demopdf417.R;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ItemVH> {
    private ArrayList<String> listResult;
    private OnItemClickListener onItemClickListener;

    public ResultAdapter(ArrayList<String> listResult) {
        this.listResult = listResult;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemVH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_layout, parent, false));
    }
    @Override
    public void onBindViewHolder(ItemVH holder, int position) {
        String result = listResult.get(position);
        holder.bind(result);
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(result);
            }
        });
    }
    @Override
    public int getItemCount() {
        return listResult.size();
    }
    static class ItemVH extends RecyclerView.ViewHolder {
        private TextView tvName;
        ItemVH(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvResult);
        }
        void bind(String result) {
            tvName.setText(result);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(String result);
    }
}
