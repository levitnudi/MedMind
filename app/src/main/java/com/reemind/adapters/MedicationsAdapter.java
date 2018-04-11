package com.reemind.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.reemind.AddEditMedicine;
import com.reemind.R;
import com.reemind.database.DatabaseHelper;
import com.reemind.models.MedData;
import com.reemind.receivers.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MedicationsAdapter extends RecyclerView.Adapter<MedicationsAdapter.MyViewHolder> {

    private static Context context;
    private List<MedData> medDatasList;
    public static View views;
    DatabaseHelper db;
    static int id;
 
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mIconView;
        private final TextView mTitleView;
        private final TextView mInfoView;
        private final TextView mFrequency;
        private final CardView mCardView;
        private final ImageButton mRemoveFromCardButton;

        public MyViewHolder(View view) {
            super(view);
            views = view;
            mCardView = (CardView) view.findViewById(R.id.card_view);
            mIconView = (ImageView) view.findViewById(R.id.drug_icon);
            mTitleView = (TextView) view.findViewById(R.id.drug_name);
            mInfoView = (TextView) view.findViewById(R.id.info_text);
            mFrequency = (TextView) view.findViewById(R.id.frequency);
            mRemoveFromCardButton = (ImageButton) view.findViewById(R.id.remove_from_medication);
        }
    }


    public interface RecyclerListener {
        void hideFab();
    }

    public MedicationsAdapter(Context context, List<MedData> medDatasList) {
        this.context = context;
        this.medDatasList = medDatasList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.med_card, parent, false);
        db = new DatabaseHelper(parent.getContext());
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MedData medData = medDatasList.get(position);

        holder.mTitleView.setText(medData.getColumnDrugName());

        // Displaying dot from HTML character code
        holder.mFrequency.setText("X"+medData.getColumnFrequency());

        // Formatting and displaying timestamp
        holder.mInfoView.setText(medData.getColumnDrugDesc());


        holder.mIconView.setImageResource(medData.getColumnIcon());

        //holder.mRemoveFromCardButton.setVisibility(View.GONE);

        holder.mRemoveFromCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMedData(position);
            }
        });

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = medData.getId();
               startActivity(view);
            }
        });

    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    public void deleteMedData(int position) {
        // deleting the note from db
       db.deleteMedication(medDatasList.get(position));
        // removing the note from the list
        medDatasList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        //make sure to cancel the alarm
        AlarmReceiver.cancelAlarm(context, id);
    }

    @Override
    public int getItemCount() {
        return medDatasList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }


    public static void startActivity(View view){
                   /* ItemCardActivity.startActivity(context, mItem);*/
        Intent intent = new Intent(context, AddEditMedicine.class);
        intent.putExtra("rowId", id);
        //intent.putExtra("NOTIFICATION_ID", reminderList.get(viewHolder.getAdapterPosition()).getId());

        // Add shared element transition animation if on Lollipop or later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //CardView cardView = (CardView) context.findViewById(R.id.card_view);

            TransitionSet setExit = new TransitionSet();
            Transition transition = new Fade();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            transition.excludeTarget(android.R.id.navigationBarBackground, true);
            //transition.excludeTarget(R.id.fab_button, true);
            //transition.excludeTarget(R.id.recycler_view, true);
            transition.setDuration(400);
            setExit.addTransition(transition);

            ((Activity) context).getWindow().setSharedElementsUseOverlay(false);
            ((Activity) context).getWindow().setReenterTransition(null);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(((Activity) context),view, "cardTransition");
            ActivityCompat.startActivity(((Activity) context), intent, options.toBundle());

        } else {
            context.startActivity(intent);
        }

    }



}
