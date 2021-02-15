package id.akakom.bimo.shareloc.app.riwayat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.data.models.Shareloc;
import id.akakom.bimo.shareloc.utils.SharelocFunction;
import id.akakom.bimo.shareloc.utils.SharelocUtils;
import id.akakom.bimo.shareloc.utils.avatarview.AvatarPlaceholder;
import id.akakom.bimo.shareloc.utils.avatarview.loader.PicassoLoader;
import id.akakom.bimo.shareloc.utils.avatarview.views.AvatarView;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ModelHolder> {

    List<Shareloc> list;
    Context context;

    OnClick onClick;

    public RiwayatAdapter(Context context, OnClick onClick) {
        list = new ArrayList<>();
        this.onClick = onClick;
        this.context = context;
    }

    @NonNull
    @Override
    public RiwayatAdapter.ModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_riwayat, parent, false);
        return new RiwayatAdapter.ModelHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.ModelHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClicked(list.get(position));
            }
        });

        PicassoLoader imageLoader = new PicassoLoader();
        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(""+(position+1));
        imageLoader.loadImage(holder.avatar, refreshableAvatarPlaceholder, ""+(position+1));

        holder.avatar.setVisibility(View.GONE);

        try {
            String date = list.get(position).getCreated_at().split(" ")[0];
            String time = list.get(position).getCreated_at().split(" ")[1];
            holder.tv_data.setText(SharelocFunction.parseDateToRealDate(date));
            holder.tv_time.setText(time);
        } catch (Exception e){
            holder.tv_data.setText(list.get(position).getCreated_at());
        }
    }

    public void addList(ArrayList<Shareloc> l){
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
        TextView tv_time;
        AvatarView avatar;


        public ModelHolder(@NonNull View itemView) {
            super(itemView);
            tv_data = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }

    public interface OnClick{
        public void onClicked(Shareloc shareloc);
    }

}
