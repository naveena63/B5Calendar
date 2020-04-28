package zastraitsolutions.B5Calendar.Form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zastraitsolutions.B5Calendar.R;
import zastraitsolutions.B5Calendar.Utils.AppConstants;
import zastraitsolutions.B5Calendar.Utils.CustomTextViewNormal;
import zastraitsolutions.B5Calendar.Utils.PrefManager;

import static android.content.Context.MODE_PRIVATE;

public class UserFormADapter extends RecyclerView.Adapter<UserFormADapter.ViewHolder> {

    private Context context;
    private ArrayList<Popupdata_modelclass> userFormModelArrayList;
    private ArrayList<UserFormModel> popupdata;
    ViewGroup viewGroup;
    PrefManager prefManager;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyUserChoice" ;
    ArrayList<String> selectedItems = new ArrayList<String>();

    public UserFormADapter(Context userFormActivity, ArrayList<Popupdata_modelclass> userFormModels)
    {
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
        viewHolder.evntname.setText(userFormModelArrayList.get(i).getEvent_name());
        viewHolder.eventDesc.setText("\u2713" + userFormModelArrayList.get(i).getEvent_description());
        SharedPreferences sharedPreferences = context.getSharedPreferences("popupdata", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("popupdata", null);
        Type type = new TypeToken<ArrayList<UserFormModel>>() {
        }.getType();
        popupdata = gson.fromJson(json, type);
        int eventId = userFormModelArrayList.get(i).getChecked();
        if (eventId==0 ){
            viewHolder.checkBox.setChecked(false);

        }
        else {
            viewHolder.checkBox.setChecked(true);

        }



//        String eventId1 = popupdata.get(i).getCheckboxValue();
        Log.e("checkbox",eventId+" ");

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventId==0 ){
                    viewHolder.checkBox.setChecked(true);
                    userFormModelArrayList.get(i).setChecked(1);
                }
                else {
                    viewHolder.checkBox.setChecked(false);
                    userFormModelArrayList.get(i).setChecked(0);
                }



            }
        });



        Log.e("ihatemyselfie", String.valueOf(userFormModelArrayList.get(i).checked));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();



    }


    @Override
    public int getItemCount() {
        return userFormModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        Button buttonsave;
        CustomTextViewNormal evntname, eventDesc;

        ViewHolder(View itemView) {
            super(itemView);
            evntname = itemView.findViewById(R.id.eventNme);
            eventDesc = itemView.findViewById(R.id.eventDesc);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }


}

