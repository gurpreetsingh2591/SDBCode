package expense.exp.toast;



import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



import expense.exp.R;
import expense.exp.databinding.CustomToastBinding;


public class CustomToast {

	// Custom Toast Method
	public void Show_Toast(Context context, View view, String error) {

		// Layout Inflater for inflating custom view
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		CustomToastBinding binding = CustomToastBinding.inflate(LayoutInflater.from(context));
		//binding.setViewModel(new CustomToastViewModel(error));
		// inflate the layout over view
		//View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) view.findViewById(R.id.toast_root));

		// Get TextView id and set error
		//TextView text = layout.findViewById(R.id.toast_error);


		binding.toastError.setText(error);

		Toast toast = new Toast(context);// Get Toast Context
		toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
																		// Toast
																		// gravity
																		// and
																		// Fill
																		// Horizoontal

		toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
		toast.setView(binding.getRoot());// Set Custom View over toast

		toast.show();// Finally show toast
	}

	// Custom Toast Method
	public void Show_Toast_successfully(Context context, View view, String error) {

		// Layout Inflater for inflating custom view
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate the layout over view
		View layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) view.findViewById(R.id.toast_root));

		// Get TextView id and set error
		TextView text = layout.findViewById(R.id.toast_error);
		text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.successfully_icon, 0, 0, 0);
		text.setTextColor(context.getResources().getColor(R.color.green));


		text.setText(error);

		Toast toast = new Toast(context);// Get Toast Context
		toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
		// Toast
		// gravity
		// and
		// Fill
		// Horizoontal

		toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
		toast.setView(layout); // Set Custom View over toast

		toast.show();// Finally show toast
	}


}
