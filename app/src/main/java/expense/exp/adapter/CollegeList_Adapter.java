package expense.exp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import expense.exp.R;
import expense.exp.helper.SharedPrefManager;
import expense.exp.model_class.CollegeResponse.CollProgramList;

/**
 * Created by admin on 14-05-2018.
 */

public class CollegeList_Adapter extends RecyclerView.Adapter<CollegeList_Adapter.MyViewHolder> {



    private List<CollProgramList> datumList;
    private ClickListener clickListener;
    private Context context;
    SharedPrefManager sharedPrefManager;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_text;
        public ImageView play_list,img_option_menu;
        public ToggleButton toggleButton;

        public MyViewHolder(View view) {
            super(view);
            tv_text= view.findViewById(R.id.tv_text);

        }
    }


    public CollegeList_Adapter(List<CollProgramList> datumList, Context context, ClickListener clickListener) {
        this.datumList = datumList;
        this.clickListener=clickListener;
        this.context=context;
        sharedPrefManager=new SharedPrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.collge_list_items, parent, false);

                return new MyViewHolder(itemView);
    }

    public void deleteItem(int position) {
        datumList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, datumList.size());

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        CollProgramList data = datumList.get(position);

        holder.tv_text.setText(data.getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onPositionClicked(datumList.get(position), 1,position);
                clickListener.onLongClicked(position);
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

        void onPositionClicked(CollProgramList datum, int id, int position);

        void onLongClicked(int position);
    }


}
