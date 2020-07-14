package com.example.kamrankhan.pstapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RootFolderListAdapter extends BaseAdapter implements Filterable{

    private Context mContext;
    private ArrayList<RootFolders> mRootFolderList;

    CustomFilter customFilter;
    ArrayList<RootFolders> customFilterList;


    //Constructor
    public RootFolderListAdapter(Context mContext, ArrayList<RootFolders> mRootFolderList) {
        this.mContext = mContext;
        this.mRootFolderList = mRootFolderList;
        this.customFilterList=mRootFolderList;

    }

    @Override
    public int getCount() {
        return mRootFolderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRootFolderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_rootfolsers_list,null);
        }

        TextView tv_root=(TextView)convertView.findViewById(R.id.tv_rootfolder);
        TextView tv_count=(TextView)convertView.findViewById(R.id.tv_rootfoldercount);

        //View v=View.inflate(mContext, R.layout.item_rootfolsers_list,null);
        //TextView tv_root=(TextView)v.findViewById(R.id.tv_rootfolder);
        //TextView tv_count=(TextView)v.findViewById(R.id.tv_rootfoldercount);

        //Set Text for TextView
        tv_root.setText(mRootFolderList.get(position).getFolder_name());
        tv_count.setText(String.valueOf(" ("+mRootFolderList.get(position).getFolder_count())+")");


        return convertView;
        //return v;
    }

    @Override
    public Filter getFilter() {
        if(customFilter==null){
            customFilter=new CustomFilter();
        }
        return customFilter;
    }

    //INNER CLASS
    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){

                //CONSTRAINT TO UPPER
                constraint=constraint.toString().toUpperCase();
                ArrayList<RootFolders> filters=new ArrayList<RootFolders>();

                //GET SPECIFIC ITEMS
                for(int i=0;i<customFilterList.size();i++){
                    if(customFilterList.get(i).getFolder_name().toUpperCase().contains(constraint)){

                        RootFolders rf=new RootFolders(customFilterList.get(i).getFolder_name(), customFilterList.get(i).getFolder_count());
                        filters.add(rf);
                    }
                }
                results.count=filters.size();
                results.values=filters;
            }
            else {
                results.count=customFilterList.size();
                results.values=customFilterList;

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mRootFolderList= (ArrayList<RootFolders>) results.values;
            notifyDataSetChanged();
        }
    }
}
