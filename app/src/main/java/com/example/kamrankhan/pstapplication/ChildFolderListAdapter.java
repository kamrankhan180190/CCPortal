package com.example.kamrankhan.pstapplication;


import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ChildFolderListAdapter extends BaseAdapter implements Filterable{
    private Context mContext;
    private ArrayList<ChildFolders> mChildFolderList;

    CustomFilter customFilter;
    ArrayList<ChildFolders> customFilterList;

    private SparseBooleanArray mSelectedItemsIds;

    //private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

    //Constructor
    public ChildFolderListAdapter(Context mContext, ArrayList<ChildFolders> mChildFolderList) {
        this.mContext = mContext;
        this.mChildFolderList = mChildFolderList;
        this.customFilterList=mChildFolderList;
    }

    @Override
    public int getCount() {
        return mChildFolderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChildFolderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_childfolders_list,null);
        }

        TextView tv_sender=(TextView)convertView.findViewById(R.id.tv_sender);
        TextView tv_subject=(TextView)convertView.findViewById(R.id.tv_subject);
        TextView tv_date=(TextView)convertView.findViewById(R.id.tv_date);

        //View v=View.inflate(mContext, R.layout.item_childfolders_list,null);
        //TextView tv_sender=(TextView)v.findViewById(R.id.tv_sender);
        //TextView tv_subject=(TextView)v.findViewById(R.id.tv_subject);
        //TextView tv_date=(TextView)v.findViewById(R.id.tv_date);

        //Set Text for TextView
        tv_sender.setText(mChildFolderList.get(position).getSender());
        tv_subject.setText((mChildFolderList.get(position).getSubject()));
        tv_date.setText(String.valueOf(mChildFolderList.get(position).getTime()));

        return convertView;

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
                ArrayList<ChildFolders> filters=new ArrayList<ChildFolders>();

                //GET SPECIFIC ITEMS
                for(int i=0;i<customFilterList.size();i++){
                    if(customFilterList.get(i).getSender().toUpperCase().contains(constraint)){

                        ChildFolders cf=new ChildFolders(customFilterList.get(i).getId(), customFilterList.get(i).getSender(), customFilterList.get(i).getSubject(),customFilterList.get(i).getTime(), customFilterList.get(i).getMsgBody());
                        filters.add(cf);
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

            mChildFolderList= (ArrayList<ChildFolders>) results.values;
            notifyDataSetChanged();
        }


    }

    /*
    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }
    */




    // other classes
    /***
     * Methods required for do selections, remove selections, etc.
     */

    /*
    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    */
}


