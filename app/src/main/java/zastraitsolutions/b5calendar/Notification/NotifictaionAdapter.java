package zastraitsolutions.b5calendar.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import zastraitsolutions.b5calendar.R;

public class NotifictaionAdapter extends RecyclerView.Adapter<NotifictaionAdapter.SingleItemRowHolder> {

    private ArrayList<NotifictaionModel> notifictaionModelArrayList;
    private Context mContext;

    public NotifictaionAdapter(Context context, ArrayList<NotifictaionModel> notifictaionModelArrayList) {
        this.notifictaionModelArrayList = notifictaionModelArrayList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notifictaion_custom_layout, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        NotifictaionModel singleItem = notifictaionModelArrayList.get(i);

        holder.message.setText(singleItem.getMessage());
        holder.title.setText(singleItem.getTitle());



    }

    @Override
    public int getItemCount() {
        return (null != notifictaionModelArrayList ? notifictaionModelArrayList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView message,title;

        public SingleItemRowHolder(View view) {
            super(view);

            this.message = view.findViewById(R.id.message);
            this.title = view.findViewById(R.id.title);


        }
    }
}