package zastraitsolutions.B5Calendar.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zastraitsolutions.B5Calendar.Form.UserFormADapter;
import zastraitsolutions.B5Calendar.Form.UserFormModel;
import zastraitsolutions.B5Calendar.Utils.AppConstants;
import zastraitsolutions.B5Calendar.Utils.CustomTextViewNormal;
import zastraitsolutions.B5Calendar.R;
import zastraitsolutions.B5Calendar.Utils.PrefManager;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.SingleItemRowHolder> {

    private ArrayList<EventModel> itemsList;
    private Context mContext;
    RecyclerViewListener listener;
    PrefManager prefManager;

    protected CustomTextViewNormal tv_event,eventDate;
    RequestQueue requestQueue;
    TextView no_packages_available;
    ArrayList<UserFormModel> userFormModelArrayList;
    UserFormModel userFormModel;
    UserFormADapter userFormADapter;
    String eventnamedate;

    public EventAdapter(Context context, ArrayList<EventModel> itemsList, RecyclerViewListener listener) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.eventname_layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        prefManager = new PrefManager(mContext);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        EventModel singleItem = itemsList.get(i);
        String event = (itemsList.get(i).getEventName());
        String eventDate = (itemsList.get(i).getEventDate());

        String upToNCharacters = event.substring(0, Math.min(event.length(), 4));

        tv_event.setText(upToNCharacters);

        String color = singleItem.getEventColor();
        Log.e("colorevt", color);
        if (color.equals("#0000FF")) {
            tv_event.setBackgroundResource(R.color.Blue);

        }
        if (color.equals("#FF0000")) {
            tv_event.setBackgroundResource(R.color.red);

        }
        if (color.equals("#00FF00")) {
            tv_event.setBackgroundResource(R.color.green);

        }
        if (color.equals("#FFA500")) {
            tv_event.setBackgroundResource(R.color.orange);

        }
        if (color.equals("#9B870C")) {
            tv_event.setBackgroundResource(R.color.yellow);
        }
        if (color.equals("#006699")) {
            tv_event.setBackgroundResource(R.color.navBlue);
        }

    }

    @Override
    public int getItemCount() {
        return (itemsList.size());
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        public SingleItemRowHolder(View view) {
            super(view);

            tv_event = view.findViewById(R.id.eventNme);
            eventDate = view.findViewById(R.id.eventDate);

            tv_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.activity_user_form);
                     requestQueue = Volley.newRequestQueue(mContext);
                    final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view_list);
                    TextView eventDate = (TextView) dialog.findViewById(R.id.eventDate);
                    no_packages_available = dialog.findViewById(R.id.no_packages_available);
                    requestQueue = Volley.newRequestQueue(mContext);
                   eventDate.setText(prefManager.getEventdate());
                  //  Log.i("dateevenet","f"+prefManager.getEventdate());
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                            LinearLayoutManager.VERTICAL, false));
                    String output="07/01/2020";
                    String url_formation = AppConstants.BASE_URL + AppConstants.GETCALENDER + "date=" + prefManager+ "&type=day";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url_formation, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Eventresponse", "response" + response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equalsIgnoreCase("true")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    String monthname = jsonObject1.getString("monthname");
                                    String year = jsonObject1.getString("year");
                                    JSONArray jsonArray = jsonObject1.getJSONArray("data");

                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                        String eventDate = jsonObject2.getString("event_date");
                                        JSONArray jsonArray1 = jsonObject2.getJSONArray("event_names");
                                        userFormModelArrayList = new ArrayList<>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {

                                            JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                            String evenname = jsonObject3.getString("event_name");
                                            String eventDesc = jsonObject3.getString("event_description");

                                            userFormModel = new UserFormModel();
                                            userFormModel.setEventName(evenname);
                                            userFormModel.setEventDesc(eventDesc);
                                            userFormModel.setEventDate(eventDate);
                                            userFormModelArrayList.add(userFormModel);
                                        }

                                    }
                                    if (userFormModelArrayList.size() > 0) {
                                        userFormADapter = new UserFormADapter(mContext, userFormModelArrayList);
                                        userFormADapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(userFormADapter);
                                        no_packages_available.setVisibility(View.GONE);
                                    } else {
                                        no_packages_available.setText("No events in this date");
                                        no_packages_available.setVisibility(View.VISIBLE);

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mContext, "Couldn't Find Network", Toast.LENGTH_LONG).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<>();
                            return param;
                        }
                    };
                    requestQueue.add(stringRequest);
                    ImageView dialogButton = (ImageView) dialog.findViewById(R.id.btnClose);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }
    }
}