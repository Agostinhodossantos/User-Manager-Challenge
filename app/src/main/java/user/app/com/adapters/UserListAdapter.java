package user.app.com.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import user.app.com.R;
import user.app.com.network.DataProvider;
import user.app.com.ui.activities.UserProfileActivity;
import user.app.com.models.User;
import user.app.com.utils.StringUtils;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> userList;

    public UserListAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText(userList.get(position).getName());
        holder.tv_bio.setText(StringUtils.slice(userList.get(position).getBiography(), 0, 40));
        String imgUrl = userList.get(position).getImgUrl();

        if (!StringUtils.isEmpty(imgUrl)) {
            Picasso.get().load(imgUrl).into(holder.img_profile);
        } else {
           holder.img_profile.setImageResource(R.drawable.emptyimg);
        }

        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("user", (User)userList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataProvider provider = new DataProvider();
                provider.removeUser(userList.get(position).getId(), new DataProvider.ResponseListener() {
                    @Override
                    public void onSuccess(Object response) {
                        Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView item_card;
        private RoundedImageView img_profile;
        private TextView tv_name, tv_bio;
        private ImageButton img_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_bio = itemView.findViewById(R.id.tv_bio);
            tv_name = itemView.findViewById(R.id.tv_name);
            item_card = itemView.findViewById(R.id.item_card);
            img_btn = itemView.findViewById(R.id.img_btn);
            img_profile = itemView.findViewById(R.id.img_profile);

        }
    }
}