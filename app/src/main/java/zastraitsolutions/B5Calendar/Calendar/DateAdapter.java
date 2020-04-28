package zastraitsolutions.B5Calendar.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import zastraitsolutions.B5Calendar.Form.Popupdata_modelclass;
import zastraitsolutions.B5Calendar.Form.UserFormADapter;
import zastraitsolutions.B5Calendar.Form.UserFormModel;
import zastraitsolutions.B5Calendar.Utils.AppConstants;
import zastraitsolutions.B5Calendar.Utils.PrefManager;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import zastraitsolutions.B5Calendar.Utils.CustomTextViewNormal;
import zastraitsolutions.B5Calendar.FormActivity;
import zastraitsolutions.B5Calendar.R;

import static android.content.Context.MODE_PRIVATE;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ItemRowHolder> {
    private RecyclerViewListener mListener;
    public ArrayList<DateModel> dataList;
    int type;
    public static CustomTextViewNormal date;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String PREFERENCE = "AGENT";
    String logintype;
    private Context mContext;
    String output = "";
    RequestQueue requestQueue;
    ArrayList<Popupdata_modelclass> userFormModelArrayList;
    Popupdata_modelclass popupdata_modelclass;
    UserFormModel userFormModel;
    UserFormADapter userFormADapter;
    TextView no_packages_available;
    LinearLayout parentlayout;
    PrefManager prefManager;
    Context ctx;
    RequestQueue sch_RequestQueue;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DateAdapter(Context context, ArrayList<DateModel> dataList, int type) {
        this.dataList = dataList;
        this.type = type;
        this.mContext = context;
        //  mListener=listener;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calendar_item, null);
        ItemRowHolder mh = new ItemRowHolder(v, mListener);
        prefManager = new PrefManager(mContext);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        itemRowHolder.setIsRecyclable(false);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String output = df.format(c);
        System.out.println("dategggggggg" + output);


        final String calendarDate = dataList.get(i).getCalendarDate();

        Intent intent = new Intent("message_subject_intent");
        intent.putExtra("name", String.valueOf(dataList.get(i).getCalendarDate()));
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        String upToNCharacters = calendarDate.substring(0, Math.min(calendarDate.length(), 2));
        prefManager.storeValue(AppConstants.EEVENTDATE, upToNCharacters);
        prefManager.setEventdate(upToNCharacters);
        Log.i("geteventdate", "" + prefManager.getEventdate());
        System.out.println("upToNCharacters" + upToNCharacters);

        final ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();
        date.setText(upToNCharacters);
        Log.e("type", String.valueOf(type));

        if (singleSectionItems.size() > 0) {
            date.setBackgroundResource(R.drawable.button_background);
            date.setTextColor(Color.BLACK);
        }

        parentlayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (type == 2) {
                    Log.e("type", String.valueOf(type));
                    sharedpreferences = mContext.getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                    editor = sharedpreferences.edit();
                    logintype = sharedpreferences.getString("login_type", "");
                    Log.i("login1", "login" + logintype);
                    if (logintype.equalsIgnoreCase("1")) {
                        Log.i("login2", "login" + logintype);
                        if (singleSectionItems.size() > 0) {
                            Log.i("login3", "login" + logintype);
                            final Dialog dialog = new Dialog(mContext);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.activity_user_form);
                            requestQueue = Volley.newRequestQueue(mContext);
                            final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view_list);
                            TextView eventDate = (TextView) dialog.findViewById(R.id.eventDate);
                            Button saveCheckbox = dialog.findViewById(R.id.saveCheckbox);
                            saveCheckbox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    saveData();
                                }
                            });
                            no_packages_available = dialog.findViewById(R.id.no_packages_available);
                            requestQueue = Volley.newRequestQueue(mContext);
                            eventDate.setText(calendarDate);

                            recyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                                    LinearLayoutManager.VERTICAL, false));

                            String url_formation = AppConstants.BASE_URL + AppConstants.GETCALENDER + "date=" + calendarDate + "&type=day" + "&user_id=" + prefManager.getUserid();

                            Log.i("url", "dnbj " + url_formation);

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_formation, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("popup response", "response" + response);

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");

                                        if (status.equalsIgnoreCase("true")) {
                                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                            String monthname = jsonObject1.getString("monthname");
                                            String year = jsonObject1.getString("year");
                                            JSONArray jsonArray = jsonObject1.getJSONArray("data");

                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                                String eventDtae = jsonObject2.getString("event_date");
                                                JSONArray jsonArray1 = jsonObject2.getJSONArray("event_names");

                                                userFormModelArrayList = new ArrayList<>();
                                                for (int j = 0; j < jsonArray1.length(); j++) {
                                                    JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                                    String evenname = jsonObject3.getString("event_name");
                                                    String eventDesc = jsonObject3.getString("event_description");
                                                    int checkbox = jsonObject3.getInt("checked");
                                                    String userid = jsonObject3.getString("user_id");
                                                    String event_short=jsonObject3.getString("event_short");
                                                    String id=jsonObject3.getString("id");

                                                    popupdata_modelclass = new Popupdata_modelclass();
                                                    popupdata_modelclass.setEvent_name(evenname);
                                                    popupdata_modelclass.setEvent_description(eventDesc);
                                                    popupdata_modelclass.setChecked(checkbox);
                                                    popupdata_modelclass.setId(userid);
                                                    popupdata_modelclass.setEvent_short(event_short);
                                                    popupdata_modelclass.setId(id);

                                                    userFormModelArrayList.add(popupdata_modelclass);
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
                            Button dialogButton = (Button) dialog.findViewById(R.id.btnClose);
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                }
                            });
                            dialog.show();
                            date.setBackgroundResource(R.drawable.button_background);
                            date.setTextColor(Color.BLACK);

                        } else {
                            Intent intent = new Intent(mContext, FormActivity.class);
                            intent.putExtra("response_date", calendarDate);
                            mContext.startActivity(intent);
                        }

                    }

                    if (logintype.equalsIgnoreCase("2")) {

                        final Dialog dialog = new Dialog(mContext);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.activity_user_form);
                        requestQueue = Volley.newRequestQueue(mContext);
                        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view_list);
                        TextView eventDate = (TextView) dialog.findViewById(R.id.eventDate);
                        Button saveCheckbox = (Button) dialog.findViewById(R.id.saveCheckbox);
                        no_packages_available = dialog.findViewById(R.id.no_packages_available);
                        saveCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                saveData();

                            }
                        });
                        requestQueue = Volley.newRequestQueue(mContext);

                        eventDate.setText(calendarDate);

                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                                LinearLayoutManager.VERTICAL, false));
                        String url_formation = AppConstants.BASE_URL + AppConstants.GETCALENDER + "date=" + calendarDate + "&type=day" + "&user_id=" + prefManager.getUserid();
                        Log.i("url", "dnbj " + url_formation);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_formation, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("responsepopup", "response" + response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equalsIgnoreCase("true")) {
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                        String monthname = jsonObject1.getString("monthname");
                                        String year = jsonObject1.getString("year");
                                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                            String eventDtae = jsonObject2.getString("event_date");

                                            JSONArray jsonArray1 = jsonObject2.getJSONArray("event_names");
                                            userFormModelArrayList = new ArrayList<>();
                                            for (int j = 0; j < jsonArray1.length(); j++) {

                                                JSONObject jsonObject3 = jsonArray1.getJSONObject(j);
                                                String evenname = jsonObject3.getString("event_name");
                                                String eventDesc = jsonObject3.getString("event_description");
                                                int checkbox = jsonObject3.getInt("checked");
                                                String userid = jsonObject3.getString("user_id");
                                                String event_short=jsonObject3.getString("event_short");
                                                String id=jsonObject3.getString("id");

                                                popupdata_modelclass = new Popupdata_modelclass();
                                                popupdata_modelclass.setEvent_name(evenname);
                                                popupdata_modelclass.setEvent_description(eventDesc);
                                                popupdata_modelclass.setChecked(checkbox);
                                                popupdata_modelclass.setUser_id(userid);
                                                popupdata_modelclass.setEvent_short(event_short);
                                                popupdata_modelclass.setId(id);

                                                userFormModelArrayList.add(popupdata_modelclass);
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
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        date.setBackgroundResource(R.drawable.button_background);
                        date.setTextColor(Color.BLACK);
                    }

                }
                if (type == 1) {
                    Log.e("type", String.valueOf(type));
                    Intent intent = new Intent(mContext, MyCalendarFormActivity.class);
                    intent.putExtra("response_date", calendarDate);
                    mContext.startActivity(intent);
                }
            }
        });
        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext));
        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);
        itemRowHolder.recycler_view_list.setFocusable(false);
        EventAdapter itemListDataAdapter = new EventAdapter(mContext, singleSectionItems, calendarDate);
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
        itemRowHolder.recycler_view_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickGridItem(dataList.get(i).getCalendarDate());
                    Toast.makeText(mContext, "jdfk", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveData() {
        String usssid=prefManager.getUserid();
        String data = new Gson().toJson(userFormModelArrayList);
        String AddS = "{\"user_id\":\"" + usssid + "\",\"event_names\": "+ data + "}";
        
        Log.d("jsnresponse pernonal", "---" + AddS);
        JSONObject lstrmdt = null;
        try {
            lstrmdt = new JSONObject(AddS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, "https://eabhiyan.com/apis/calendar/rest/index.php/Signup/postCalenderUser",lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderVolleylogin", "---" + response);


                        
                            // Toast.makeText(RegisterFragment.this, regid , Toast.LENGTH_SHORT).show();

                       

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
            
                //do stuff with the body...
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                //return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
                return headers;
            }
        };
        jsonObjReq.setTag("");
        addToRequestQueue(jsonObjReq);
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }
    public <T> void addToRequestQueue(Request<T> req) {
        if (sch_RequestQueue == null) {
            sch_RequestQueue = Volley.newRequestQueue(mContext);
        }
        sch_RequestQueue.add(req);


//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://eabhiyan.com/apis/calendar/rest/index.php/Signup/postCalenderUser", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.i("saved response", "response" + response);
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(mContext, "Couldn't Find Network", Toast.LENGTH_LONG).show();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> param = new HashMap<>();
//
//                param.put("user_id",usssid);
//                String data = new Gson().toJson(userFormModelArrayList);
//                param.put("event_names", data);
//
//                Log.e("datapop", String.valueOf(param));
//                return param;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "text/plain");
//                return params;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "text/plain; charset=utf-8";
//            }
//        };
//        requestQueue.add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewListener mListener;
        protected RecyclerView recycler_view_list;

        public ItemRowHolder(View view, RecyclerViewListener listener) {
            super(view);
            date = view.findViewById(R.id.calendarDate);
            parentlayout = view.findViewById(R.id.parentLayout);
            mListener = listener;
            view.setOnClickListener(this);
            recycler_view_list = (RecyclerView) view.findViewById(R.id.eventRecyclerView);
            recycler_view_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "yes", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}


