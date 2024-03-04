package expense.exp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import expense.exp.R;
import expense.exp.model_class.NotificationList;
import expense.exp.model_class.PlanList;

/**
 * Created by admin on 14-05-2018.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {


    private List<NotificationList> datumList;
    private ClickListener clickListener;
    private Context context;


    public NotificationListAdapter(List<NotificationList> datumList, Context context, ClickListener clickListener) {
        this.datumList = datumList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_layout, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        holder.tvTime.setText(datumList.get(position).getCreated());
        holder.tvName.setText(datumList.get(position).getSender()+" "+"sent new message on"+" "+datumList.get(position).getDoc_name() +" document.");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickListener.onPositionClicked(datumList,Integer.parseInt(datumList.get(position).getId()),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface ClickListener {

        void onPositionClicked(List<NotificationList> datumList, int id, int position);
        
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  tvTime,tvName;
        public MyViewHolder(View view) {
            super(view);
            tvTime = view.findViewById(R.id.tvTime);
            tvName = view.findViewById(R.id.tvName);


        }
        
        
        
    }


}
