package id.akakom.bimo.shareloc.app.detailriwayat.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.riwayat.adapter.RiwayatAdapter;
import id.akakom.bimo.shareloc.data.models.Shareloc;
import id.akakom.bimo.shareloc.data.models.SharelocMember;
import id.akakom.bimo.shareloc.utils.SharelocFunction;
import id.akakom.bimo.shareloc.utils.avatarview.AvatarPlaceholder;
import id.akakom.bimo.shareloc.utils.avatarview.loader.PicassoLoader;
import id.akakom.bimo.shareloc.utils.avatarview.views.AvatarView;

public class DetailRiwayatAdapter extends RecyclerView.Adapter<DetailRiwayatAdapter.ModelHolder> {

    List<SharelocMember> list;
    Context context;

    DetailRiwayatAdapter.OnClick onClick;

    public DetailRiwayatAdapter(Context context, DetailRiwayatAdapter.OnClick onClick) {
        list = new ArrayList<>();
        this.onClick = onClick;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailRiwayatAdapter.ModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_detail_riwayat, parent, false);
        return new DetailRiwayatAdapter.ModelHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailRiwayatAdapter.ModelHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClicked(list.get(position));
            }
        });
        PicassoLoader imageLoader = new PicassoLoader();
        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(list.get(position).getName());
        imageLoader.loadImage(holder.avatar, refreshableAvatarPlaceholder, list.get(position).getName());
        holder.tv_data.setText(list.get(position).getName());
    }

    public void addList(ArrayList<SharelocMember> l){
        list.clear();
        list.addAll(l);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ModelHolder extends RecyclerView.ViewHolder {
        TextView tv_data;
        AvatarView avatar;


        public ModelHolder(@NonNull View itemView) {
            super(itemView);
            tv_data = itemView.findViewById(R.id.tv_name);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }

    public interface OnClick{
        public void onClicked(SharelocMember shareloc);
    }

}