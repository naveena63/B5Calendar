package zastraitsolutions.B5Calendar.Form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zastraitsolutions.B5Calendar.R;
import zastraitsolutions.B5Calendar.Utils.AppConstants;
import zastraitsolutions.B5Calendar.Utils.CustomTextViewNormal;
import zastraitsolutions.B5Calendar.Utils.PrefManager;

public class UserFormADapter extends RecyclerView.Adapter<UserFormADapter.ViewHolder> {

    private Context context;
    private ArrayList<UserFormModel> userFormModelArrayList;
    ViewGroup viewGroup;
    PrefManager prefManager;

    public UserFormADapter(Context userFormActivity, ArrayList<UserFormModel> userFormModels) {
        this.userFormModelArrayList = userFormModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        prefManager = new PrefManager(context);
        View view = LayoutInflater.from(context).inflate(R.layout.user_form_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.evntname.setText(userFormModelArrayList.get(i).getEventName());
        viewHolder.eventDesc.setText("\u2713" + userFormModelArrayList.get(i).getEventDesc());

    }

    @Override
    public int getItemCount() {
        return userFormModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        CustomTextViewNormal evntname, eventDesc;

        ViewHolder(View itemView) {
            super(itemView);
            evntname = itemView.findViewById(R.id.eventNme);
            eventDesc = itemView.findViewById(R.id.eventDesc);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    final String name = evntname.getText().toString();
                    final String desc = eventDesc.getText().toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.BASE_URL + AppConstants.checkbox, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           Log.i("checkbox","checkbox"+response);
                            if (isChecked){
                                checkBox.setEnabled(false);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("event_name", name);
                            map.put("event_description", desc);
                            map.put("id_user", prefManager.getUserid());
                            return map;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                }

            });
        }
    }
}

