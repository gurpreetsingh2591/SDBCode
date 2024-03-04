package expense.exp.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import expense.exp.R;
import expense.exp.model_class.PlanList;

/**
 * Created by admin on 14-05-2018.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {


    private List<ChatResponse> datumList;
    private Context context;
    private String type;


    public ChatListAdapter(List<ChatResponse> datumList, Context context, String type) {
        this.datumList = datumList;
        this.context = context;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_layout, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String message = datumList.get(position).getMesssage();
        String userName = datumList.get(position).getUsername();
        if(type == "1") {

            holder.sender_text_time.setText(message);
            holder.sender_messsage_text.setText(userName);
            holder.receiver_message_text_linearLayout.setVisibility(View.GONE);
            holder.sender_messsage_text_linearLayout.setVisibility(View.VISIBLE);

        }
        else{

            holder.receiver_text_time.setText(message);
            holder.receiver_message_text.setText(userName);
            holder.receiver_message_text_linearLayout.setVisibility(View.VISIBLE);
            holder.sender_messsage_text_linearLayout.setVisibility(View.GONE);
        }
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

        void onPositionClicked(List<PlanList> datumList, int id, int position);
        
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sender_messsage_text_linearLayout,receiver_message_text_linearLayout;
        TextView sender_text_time,receiver_text_time,receiver_message_text,sender_messsage_text;

        public MyViewHolder(View view) {
            super(view);
            sender_messsage_text_linearLayout = view.findViewById(R.id.sender_messsage_text_linearLayout);
            receiver_message_text_linearLayout =  view.findViewById(R.id.receiver_message_text_linearLayout);
            sender_text_time =  view.findViewById(R.id.sender_text_time);
            receiver_text_time =  view.findViewById(R.id.receiver_text_time);
            receiver_message_text =  view.findViewById(R.id.receiver_message_text);
            sender_messsage_text =  view.findViewById(R.id.sender_messsage_text);

        }
        
        
        
    }


}
