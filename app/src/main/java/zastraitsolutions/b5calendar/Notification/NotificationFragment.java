package zastraitsolutions.b5calendar.Notification;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zastraitsolutions.b5calendar.R;
import zastraitsolutions.b5calendar.Utils.AppConstants;
import zastraitsolutions.b5calendar.Utils.PrefManager;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    ArrayList<NotifictaionModel> notifictaionModelArrayList;
    NotifictaionModel notifictaionModel;
    RequestQueue requestQueue;
    NotifictaionAdapter adapter;
    RecyclerView recyclerView;
    TextView no_packages_available;
    View view;
    PrefManager prefManager;
    SharedPreferences useridpref;

    public NotificationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the calendar_item for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        useridpref = getActivity().getSharedPreferences("USerid", MODE_PRIVATE);
        prefManager = new PrefManager(view.getContext());
        recyclerView = view.findViewById(R.id.recycler_view_list);
        no_packages_available = view.findViewById(R.id.no_packages_available);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        requestQueue = Volley.newRequestQueue(getContext());
        getUserData();
        return view;
    }

    private void getUserData() {
        String url_formation = AppConstants.notifictaion;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_formation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("notificationresposne", "response" + response);
                notifictaionModelArrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("notifications");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String title = jsonObject2.getString("title");
                            String message = jsonObject2.getString("message");
                            String statusmessafe = jsonObject2.getString("status");
                            String id = jsonObject2.getString("id");
//                            prefManager.storeValue(AppConstants.NotificationStatus, id);
//                            prefManager.setNotifictionsttaus(id);
                            String user_id = jsonObject2.getString("user_id");

                            notifictaionModel = new NotifictaionModel();
                            notifictaionModel.setTitle(title);
                            notifictaionModel.setMessage(message);
                            notifictaionModel.setStatus(statusmessafe);
                            notifictaionModelArrayList.add(notifictaionModel);
                        }

                    }
                    if (notifictaionModelArrayList.size() > 0) {
                        adapter = new NotifictaionAdapter(getActivity(), notifictaionModelArrayList);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        no_packages_available.setVisibility(View.GONE);
                    } else {
                        no_packages_available.setText("No notifications");
                        no_packages_available.setVisibility(View.VISIBLE);
                    }
                    String notifictaionStatus=jsonObject.getString("notification_status");
                    Log.i("notifictaionSttus","notifi"+notifictaionStatus);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Couldn't Find Network", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                String ussser=useridpref.getString("useridnotifications","0");
                param.put("user_id", ussser);
                Log.i("user_id","useR_id"+ussser);
                return param;
            }
        };

        requestQueue.add(stringRequest);
    }
}
