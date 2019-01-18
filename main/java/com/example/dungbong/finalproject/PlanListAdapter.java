package com.example.dungbong.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlanListAdapter  extends ArrayAdapter<PlanListItem> {

    private int resource;
    private List<PlanListItem> Items;
    private LayoutInflater layoutInflater;

    public PlanListAdapter(Context context, int resource, List<PlanListItem> items){
        super(context,resource,items);

        this.resource = resource;
        this.Items = items;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;

        if(convertView != null){
            view = convertView;
        }else{
            view = this.layoutInflater.inflate(this.resource,null);
        }

        PlanListItem mitem = this.Items.get(position);

        TextView title = (TextView)view.findViewById(R.id.planlist_planname);
        title.setText(mitem.Return_PlanName());

        TextView date = (TextView)view.findViewById(R.id.planlist_plandate);
        date.setText(mitem.Return_PlanDate());

        //list Check true or false?
        ImageView Check = (ImageView)view.findViewById(R.id.planlist_check);
        if(mitem.Return_ClearFrag() != 1 )Check.setVisibility(View.INVISIBLE);
        else Check.setVisibility(View.VISIBLE);

        return view;




    }
}
