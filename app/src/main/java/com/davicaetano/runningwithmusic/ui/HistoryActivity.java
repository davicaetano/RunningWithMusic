package com.davicaetano.runningwithmusic.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davicaetano.runningwithmusic.R;
import com.davicaetano.runningwithmusic.data.database.RWMContract;
import com.davicaetano.runningwithmusic.ui.presenter.PlayerActivityPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by davicaetano on 1/12/16.
 */
public class HistoryActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recycler_view_history) RecyclerView recyclerView;

    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Cursor cursor = getContentResolver().query(RWMContract.Sessions.CONTENT_URI,
                null,
                null,
                null,
                "time1 ASC");
        Log.v("davi","tamanho do cursor - " + cursor.getCount());

        adapter = new HistoryAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setupActivityComponent() {

    }

    public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CustomViewHolder>{
        private Cursor cursor;
        private Context context;

        public HistoryAdapter(Context context, Cursor cursor){
            this.cursor = cursor;
            this.context = context;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(HistoryAdapter.CustomViewHolder holder, int position) {
            Log.v("davi","position - " + position);
            if(cursor.moveToPosition(position)) {
                Log.v("davi", "dentro");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                Long time1 = 0L;
                Long time2 = 0L;
                if (cursor.getString(cursor.getColumnIndex(RWMContract.Sessions.COLUMN_TIME1)) != null){
                    time1 = Long.parseLong(cursor.getString(cursor.getColumnIndex(RWMContract.Sessions.COLUMN_TIME1)));
                }
                if (cursor.getString(cursor.getColumnIndex(RWMContract.Sessions.COLUMN_TIME2)) != null){
                    time2 = Long.parseLong(cursor.getString(cursor.getColumnIndex(RWMContract.Sessions.COLUMN_TIME2)));
                }
                Date date = new Date(time1);
                holder.time1.setText(dateFormat.format(date));
                holder.time2.setText(PlayerActivityPresenterImpl.setTime((int) (time2 - time1)));
                if(cursor.getString(cursor.getColumnIndex(RWMContract.Sessions.COLUMN_SPACE)) != null) {
                    holder.space.setText(cursor.getString(cursor.getColumnIndex(RWMContract.Sessions.COLUMN_SPACE)));
                }else {
                    holder.space.setText(getString(R.string.NOT_FINISHED));
                }
                if(cursor.getString(cursor.getColumnIndex(RWMContract.Sessions.COLUMN_SPEED))!= null) {
                    holder.speed.setText(cursor.getString(cursor.getColumnIndex(RWMContract.Sessions.COLUMN_SPEED)));
                }else{
                    holder.speed.setText(getString(R.string.NOT_FINISHED));
                }
            }
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder{
            public TextView time1;
            public TextView time2;
            public TextView space;
            public TextView speed;

            public CustomViewHolder(View view) {
                super(view);
                this.time1 = (TextView)view.findViewById(R.id.text_view_time1);
                this.time2 = (TextView)view.findViewById(R.id.text_view_time2);
                this.space = (TextView)view.findViewById(R.id.text_view_space);
                this.speed = (TextView)view.findViewById(R.id.text_view_speed);

            }
        }
    }

}
