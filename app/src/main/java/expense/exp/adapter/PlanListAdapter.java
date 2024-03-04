package expense.exp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import expense.exp.R;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.model.Acc;
import expense.exp.model_class.PlanList;

/**
 * Created by admin on 14-05-2018.
 */

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.MyViewHolder> {


    private List<PlanList> datumList;
    private ClickListener clickListener;
    private Context context;
    private String myPlan;


    public PlanListAdapter(List<PlanList> datumList, Context context,String myPlan, ClickListener clickListener) {
        this.datumList = datumList;
        this.context = context;
        this.myPlan = myPlan;
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan_list, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.planName.setText(datumList.get(position).getPack_name());
        holder.planPrice.setText("Cad"+datumList.get(position).getPack_cost());
        holder.planTime.setText(datumList.get(position).getSpace()+"GB");

        if (datumList.get(position).getId().equals(myPlan)){
            holder.checkoxPlans.setChecked(true);
        }
        else{
            holder.checkoxPlans.setChecked(false);
        }
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

        void onPositionClicked(List<PlanList> datumList, int id, int position);
        
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView planName, planPrice,planTime;
        public CheckBox checkoxPlans;

        public MyViewHolder(View view) {
            super(view);
            planName = view.findViewById(R.id.planName);
            planPrice = view.findViewById(R.id.planPrice);
            planTime = view.findViewById(R.id.planTime);
            checkoxPlans = view.findViewById(R.id.checkoxPlans);

        }
        
        
        
    }


}
