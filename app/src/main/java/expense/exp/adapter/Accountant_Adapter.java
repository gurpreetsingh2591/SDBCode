package expense.exp.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import expense.exp.R;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.model.Acc;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 14-05-2018.
 */

public class Accountant_Adapter extends RecyclerView.Adapter<Accountant_Adapter.MyViewHolder> {


    private List<Acc> datumList;
    private ClickListener clickListener;
    private Context context;


    public Accountant_Adapter(List<Acc> datumList, Context context, ClickListener clickListener) {
        this.datumList = datumList;
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_acountant, parent, false);

        return new MyViewHolder(itemView);
    }

    public void deleteItem(int position) {
        datumList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, datumList.size());

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        //      Music_Model music_Model = Music_ModelsList.get(position);
//            holder.title.setText(music_Model.getMusic_titel());


        final Acc data = datumList.get(position);

        holder.folder_txt.setText(data.getName());

         final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);


        if (sharedPrefManager.getacc_id() == Integer.parseInt(data.getId())){
            holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.remove_acc_icon));


        }else {
            holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.add_acc_icon));


        }


        holder.toggleButton.setOnClickListener(view -> {


            if (sharedPrefManager.getacc_id() == -1)
            {

                holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.remove_acc_icon));

                clickListener.onPositionClicked(data, 1, position);

            }else
            {
                if (sharedPrefManager.getacc_id()==Integer.parseInt(data.getId()))
                {

                    holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.add_acc_icon));


                    clickListener.onPositionClicked(data, 2, position);

                }else
                {

                    Toast.makeText(context, "Already selected", Toast.LENGTH_SHORT).show();
                }

            }

        });




        Picasso.get()
                .load("http://expenses.topnotchhub.com/assets/uploads/avatar/" + data.getImage())
                .placeholder(R.drawable.user_profile_icon)
                .error(R.drawable.user_profile_icon)
                .into(holder.acc_img);


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

        void onPositionClicked(Acc datum, int id, int position);

        void onLongClicked(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView folder_txt, subtitel;
        public ImageView acc_img, img_option_menu;
        public ImageView toggleButton;

        public MyViewHolder(View view) {
            super(view);
            folder_txt = view.findViewById(R.id.foldername);
            toggleButton = view.findViewById(R.id.add_remove);
            acc_img = view.findViewById(R.id.acc_img);

        }
    }


}
