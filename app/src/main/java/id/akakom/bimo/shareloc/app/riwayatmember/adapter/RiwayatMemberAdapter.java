package id.akakom.bimo.shareloc.app.riwayatmember.adapter;

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
import id.akakom.bimo.shareloc.data.models.SharelocMember;
import id.akakom.bimo.shareloc.utils.SharelocFunction;
import id.akakom.bimo.shareloc.utils.avatarview.AvatarPlaceholder;
import id.akakom.bimo.shareloc.utils.avatarview.loader.PicassoLoader;
import id.akakom.bimo.shareloc.utils.avatarview.views.AvatarView;

public class RiwayatMemberAdapter extends RecyclerView.Adapter<RiwayatMemberAdapter.ModelHolder> {

    List<SharelocMember> list;
    Context context;

    OnClick onClick;

    public RiwayatMemberAdapter(Context context, OnClick onClick) {
        list = new ArrayList<>();
        this.onClick = onClick;
        this.context = context;
    }

    @NonNull
    @Override
    public RiwayatMemberAdapter.ModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_riwayat, parent, false);
        return new RiwayatMemberAdapter.ModelHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatMemberAdapter.ModelHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClicked(list.get(position));
            }
        });

        SharelocMember member = list.get(position);

        PicassoLoader imageLoader = new PicassoLoader();
        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(member.getName());
        imageLoader.loadImage(holder.avatar, refreshableAvatarPlaceholder, member.getName());

        try {
            String date = list.get(position).getCreated_at().split(" ")[0];
            String time = list.get(position).getCreated_at().split(" ")[1];
            holder.tv_data.setText(list.get(position).getName());
            holder.tv_time.setText(SharelocFunction.parseDateToRealDate(date)+" "+time);
        } catch (Exception e){
            holder.tv_data.setText(list.get(position).getCreated_at());
        }
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
        public void onClicked(SharelocMember shareloc);
    }

}
