package com.example.dee.mywishlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import dataV.DatabaseHandler;
import model.MyWish;

public class DisplayWishesActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<MyWish> dbwishes = new ArrayList<>(); // holds all of our wishes
    private WishAdapter wishAdapter;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wishes);

        listview = (ListView)findViewById(R.id.list);

        refreshData();
    }

    private void refreshData() {
        dbwishes.clear();
        dba = new DatabaseHandler(getApplicationContext());

        ArrayList<MyWish> wishesFromDb = dba.getWishes();
        for(int i = 0; i < wishesFromDb.size(); i++)
        {
            String title = wishesFromDb.get(i).getTitle();
            String dateText = wishesFromDb.get(i).getRecordDate();
            String content = wishesFromDb.get(i).getContent();
            int mid = wishesFromDb.get(i).getItemId();

            MyWish myWish = new MyWish();
            myWish.setTitle(title);
            myWish.setContent(content);
            myWish.setRecordDate(dateText);
            myWish.setItemId(mid);
            dbwishes.add(myWish);
        }
        dba.close();

        //setup adapter
        wishAdapter = new WishAdapter(DisplayWishesActivity.this, R.layout.wish_row, dbwishes);
        listview.setAdapter(wishAdapter);
        wishAdapter.notifyDataSetChanged();
    }
    public class WishAdapter extends ArrayAdapter<MyWish>
    {
        Activity activity;
        int layoutResource;
        MyWish wish;
        ArrayList<MyWish> mData = new ArrayList<>();

        public WishAdapter(Activity act, int resource, ArrayList<MyWish> data) {
            super(act, resource, data);
            activity = act;
            layoutResource = resource;
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public int getPosition(MyWish item) {
            return super.getPosition(item);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Nullable
        @Override
        public MyWish getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) { // this is where listview created
            View row = convertView;
            ViewHolder holder = null;
            if(row == null || (row.getTag()) == null)
            {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResource, null);
                holder = new ViewHolder();

                holder.mTitle = (TextView) row.findViewById(R.id.name);
                holder.mDate = (TextView) row.findViewById(R.id.dateText);

                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
            }
            holder.wish = getItem(position);
            holder.mTitle.setText(holder.wish.getTitle());
            holder.mDate.setText(holder.wish.getRecordDate());

            final ViewHolder finalHolder = holder;
            holder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = finalHolder.wish.getContent().toString();
                    String dateText = finalHolder.wish.getRecordDate().toString();
                    String title = finalHolder.wish.getTitle().toString();

                    int mid = finalHolder.wish.getItemId();

                    Intent i = new Intent(DisplayWishesActivity.this, wishDetailActivity.class);
                    i.putExtra("content", text);
                    i.putExtra("title", title);
                    i.putExtra("date",dateText);
                    i.putExtra("id", mid);
                    startActivity(i);
                }
            });
            return row;
        }
    }

    class ViewHolder{
        MyWish wish;
        TextView mTitle;
        TextView mId;
        TextView mContent;
        TextView mDate;
    }


}


