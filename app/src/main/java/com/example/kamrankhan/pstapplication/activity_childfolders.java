package com.example.kamrankhan.pstapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamrankhan.pstapplication.PST.PSTException;
import com.example.kamrankhan.pstapplication.PST.PSTFile;
import com.example.kamrankhan.pstapplication.PST.PSTFolder;
import com.example.kamrankhan.pstapplication.PST.PSTMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import static android.R.attr.mode;


public class activity_childfolders extends AppCompatActivity {

    private ListView lvChildFolders;
    private DbAdapter dbHelper;
    private ChildFolderListAdapter adapter;
    private ArrayList<ChildFolders> mChildFoldersList;

    private ArrayList<ChildFolders> selectionList;

    private ArrayList<RootFolders> folderNamesList;

    String foldervalue=null;
    String FolderName = null;
    String nameOfFolder = null;

    //Action Mode for toolbar
    private ActionMode mActionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childfolders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar ( ).setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ( ).setHomeAsUpIndicator ( R.drawable.previous );
        //Getting the value of clicked item via intent and storing its value in foldervalue variable
        foldervalue=getIntent().getExtras().getString("rootFolderName");


        if(foldervalue.equalsIgnoreCase("Deleted Items")){
            nameOfFolder = "Deleted_Items";
        }
        else if(foldervalue.equalsIgnoreCase("Conversation Action Settings")){
            nameOfFolder = "Conversation_Action_Settings";
        }
        else if(foldervalue.equalsIgnoreCase("Junk E-mail")){
            nameOfFolder = "Junk_E_mail";
        }
        else if(foldervalue.equalsIgnoreCase("Quick Step Settings")){
            nameOfFolder = "Quick_Step_Settings";
        }
        else if(foldervalue.equalsIgnoreCase("RSS Feeds")){
            nameOfFolder = "RSS_Feeds";
        }
        else{
            nameOfFolder = foldervalue;
        }

        FolderName = nameOfFolder;

        //Setting Actionbar Title with the value of foldervalue variable
        setTitle(foldervalue);


        //Open DATABASE
        dbHelper = new DbAdapter(this);
        dbHelper.open();

        //Add data into DATABASE
        //dbHelper.insertFromPST();

        mChildFoldersList=new ArrayList<>();
        selectionList = new ArrayList<>();
        folderNamesList = new ArrayList<>();

        //Get Data from DATABASE
        fetchFromDB();

        adapter=new ChildFolderListAdapter(getApplicationContext(),mChildFoldersList);
        lvChildFolders=(ListView)findViewById(R.id.listView_Child);
        //If childfolder is empty, this view will be set
        lvChildFolders.setEmptyView(findViewById(R.id.empty_list_item));

        lvChildFolders.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Register the ListView  for Context menu
        //registerForContextMenu(lvChildFolders);



        //OnClick Listener

        lvChildFolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //If ActionMode not null select item
                if (mActionMode == null) {
                    //Extracting and Saving the value of the clicked item in TextView variable
                    TextView tvsender = (TextView) view.findViewById(R.id.tv_sender); //now tvrootfolders contain the value of clicked item
                    TextView tvsubject = (TextView) view.findViewById(R.id.tv_subject);
                    TextView tvdate = (TextView) view.findViewById(R.id.tv_date);

                    //listview clicked item index
                    int itemPosition = position;

                    //Listview clicked item value
                    String itemSender = tvsender.getText().toString();
                    String itemSubject = tvsubject.getText().toString();
                    String itemDate = tvdate.getText().toString();

                    Toast.makeText(getApplicationContext(), itemSender, Toast.LENGTH_LONG).show();
                    //if(position==5) {
                    Intent i = new Intent(activity_childfolders.this, activity_subchildfolder.class);
                    i.putExtra("subSender", itemSender);
                    i.putExtra("subSubject", itemSubject);
                    i.putExtra("subDate", itemDate);
                    i.putExtra("folderName", foldervalue);
                    startActivity(i);
                    //}
                }
            }
        });



        //Long Click Listener
        /*
        lvChildFolders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                //Extracting and Saving the value of the clicked item in TextView variable
                TextView tvsender=(TextView)view.findViewById(R.id.tv_sender); //now tvrootfolders contain the value of clicked item
                TextView tvsubject=(TextView)view.findViewById(R.id.tv_subject);
                TextView tvdate=(TextView)view.findViewById(R.id.tv_date);

                //listview clicked item index
                int itemPosition = position;

                //Listview clicked item value
                final String itemSender = tvsender.getText().toString();
                final String itemSubject = tvsubject.getText().toString();
                final String itemDate = tvdate.getText().toString();


                AlertDialog.Builder alert = new AlertDialog.Builder(activity_childfolders.this);

                alert.setTitle("Delete");
                alert.setMessage("Do you want to delete ' "+itemSender+" ' ?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String nameOfFolder = null;

                        // main code on after clicking yes
                        //Deleting Selected item from Listview
                        //Method
                        dbHelper.delete(itemSender, itemSubject, itemDate, foldervalue);

                        //Clears Lisview from Data
                        mChildFoldersList.clear();
                        //Fetches data from DB and populates Listview
                        fetchFromDB();
                        adapter.notifyDataSetChanged();




                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;

            }
        });
        */




        lvChildFolders.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvChildFolders.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int nr = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                if (checked) {
                    //nr++;

                    selectionList.add(mChildFoldersList.get(position));
                    nr++;
                    actionMode.setTitle(nr + "");

                    //adapter.setNewSelection(position, checked);
                    
                } else {
                    //nr--;

                    selectionList.remove(mChildFoldersList.get(position));
                    nr--;
                    actionMode.setTitle(nr + "");

                    //adapter.removeSelection(position);
                }


            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                //nr = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_list_view, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                String sender_Name;
                String sender_subject;
                String sender_date;



                switch (menuItem.getItemId()) {

                    case R.id.delete_item:{

                        //DATA IN "selectionList" is added by clciking in  method : "onItemCheckedStateChanged"
                        for(ChildFolders childFolders: selectionList){
                            sender_Name = childFolders.getSender();
                            sender_subject = childFolders.getSubject();
                            sender_date = childFolders.getTime();

                            //Deleting Selected item from Listview
                            //Method
                            dbHelper.delete(sender_Name, sender_subject, sender_date, foldervalue);


                        }

                        Toast.makeText(getApplicationContext(), nr+" items deleted", Toast.LENGTH_LONG).show();
                        //Clears Lisview from Data
                        mChildFoldersList.clear();
                        //Fetches data from DB and populates Listview
                        fetchFromDB();
                        adapter.notifyDataSetChanged();
                        //selectionList.clear();

                        nr = 0;
                        //adapter.clearSelection();
                        actionMode.finish();
                        break;

                    }

                    case R.id.move_item:{

                        folderNamesList.clear();
                        //GETTING TABLE NAMES FROM DATABASE
                        Cursor cursor =dbHelper.QueryData("SELECT name FROM sqlite_master WHERE type='table'");
                        if(cursor !=null){
                            if(cursor.moveToFirst()){
                                do{
                                    String folderName = cursor.getString(0);
                                    Cursor cursor2 =dbHelper.QueryData("SELECT * FROM "+folderName);
                                    int count = cursor2.getCount();

                                    RootFolders rootFolders = new RootFolders();
                                    if(folderName.equalsIgnoreCase("Deleted_Items")){
                                        rootFolders.setFolder_name("Deleted Items");
                                        rootFolders.setFolder_count(count);
                                    }
                                    else if(folderName.equalsIgnoreCase("Conversation_Action_Settings")){
                                        rootFolders.setFolder_name("Conversation Action Settings");
                                        rootFolders.setFolder_count(count);
                                    }
                                    else if(folderName.equalsIgnoreCase("Junk_E_mail")){
                                        rootFolders.setFolder_name("Junk E-mail");
                                        rootFolders.setFolder_count(count);
                                    }
                                    else if(folderName.equalsIgnoreCase("Quick_Step_Settings")){
                                        rootFolders.setFolder_name("Quick Step Settings");
                                        rootFolders.setFolder_count(count);
                                    }
                                    else if(folderName.equalsIgnoreCase("RSS_Feeds")){
                                        rootFolders.setFolder_name("RSS Feeds");
                                        rootFolders.setFolder_count(count);
                                    }
                                    else{
                                        rootFolders.setFolder_name(folderName);
                                        rootFolders.setFolder_count(count);
                                    }


                                    if(cursor.getString(0).equals("sqlite_sequence")
                                            || cursor.getString(0).equals("android_metadata")
                                            || cursor.getString(0).equals("Deleted_Email")
                                            || cursor.getString(0).equals("Deleted_Folder")
                                            || cursor.getString(0).equals("Copied_Emails")
                                            || cursor.getString(0).equals("Copied_Folders")
                                            || cursor.getString(0).equals("Moved_Email")
                                            || cursor.getString(0).equals("Moved_Folders")
                                            || cursor.getString(0).equals("New_Folders")
                                            ){
                                        //do nothing i.e dont add these tables names in list
                                    }
                                    else{
                                        folderNamesList.add(rootFolders);
                                        //Collections.sort(mRootFolderList);      //Sorting in ascending
                                        Collections.reverse(folderNamesList);   //Sorting in descending by reversing
                                    }

                                }while(cursor.moveToNext());
                            }

                            //Collections.reverse(mRootFolderList);   //Sorting in descending by reversing
                            Collections.sort(folderNamesList);      //Sorting in ascending
                        }


                            RootFolderListAdapter adapter = new RootFolderListAdapter(getApplicationContext(),folderNamesList);
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom_alert_dialogue, null);
                            ListView lv = (ListView) convertView.findViewById(R.id.lv_custom_alert);
                            lv.setAdapter(adapter);

                            //ALERT DIALOG CODE STARTS HERE
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity_childfolders.this);
                            alertDialog.setView(convertView);
                            alertDialog.setTitle("Move To");
                            final AlertDialog ad = alertDialog.show();

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //Extracting and Saving the value of the clicked item in TextView variable
                                    TextView tvrootfolders=(TextView)view.findViewById(R.id.tv_rootfolder); //now tvrootfolders contain the value of clicked item

                                    //listview clicked item index
                                    int itemPosition = position;

                                    //Listview clicked item value
                                    String itemValue = tvrootfolders.getText().toString();

                                    //MOVE METHOD TO MOVE EMAIL FROM ONE FOLDER TO ANOTHER
                                    moveEmails(itemValue);

                                    ad.dismiss();


                                }
                            });

                            nr = 0;
                            //adapter.clearSelection();
                        //selectionList.clear();
                            actionMode.finish();
                        break;


                    }

                    case R.id.copy_item:{

                        folderNamesList.clear();
                        //GETTING TABLE NAMES FROM DATABASE
                        fetchTableNamesFromDB();


                        RootFolderListAdapter adapter = new RootFolderListAdapter(getApplicationContext(),folderNamesList);
                        LayoutInflater inflater = getLayoutInflater();
                        View convertView = (View) inflater.inflate(R.layout.custom_alert_dialogue, null);
                        ListView lv = (ListView) convertView.findViewById(R.id.lv_custom_alert);
                        lv.setAdapter(adapter);

                        //ALERT DIALOG CODE STARTS HERE
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity_childfolders.this);
                        alertDialog.setView(convertView);
                        alertDialog.setTitle("Copy To");
                        final AlertDialog ad = alertDialog.show();

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Extracting and Saving the value of the clicked item in TextView variable
                                TextView tvrootfolders=(TextView)view.findViewById(R.id.tv_rootfolder); //now tvrootfolders contain the value of clicked item

                                //listview clicked item index
                                int itemPosition = position;

                                //Listview clicked item value
                                String itemValue = tvrootfolders.getText().toString();

                                //MOVE METHOD TO MOVE EMAIL FROM ONE FOLDER TO ANOTHER
                                copyEmails(itemValue);

                                ad.dismiss();


                            }
                        });

                        nr = 0;
                        //adapter.clearSelection();
                        //selectionList.clear();
                        actionMode.finish();
                        break;

                    }

                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                //nr = 0;
                //selectionList.clear();

            }
        });



    }

    public void moveEmails(String toFolder){

        String toFolderName;
        if(toFolder.equalsIgnoreCase("Deleted Items")){
            toFolderName = "Deleted_Items";

        }
        else if(toFolder.equalsIgnoreCase("Conversation Action Settings")){
            toFolderName = "Conversation_Action_Settings";

        }
        else if(toFolder.equalsIgnoreCase("Junk E-mail")){
            toFolderName = "Junk_E_mail";

        }
        else if(toFolder.equalsIgnoreCase("Quick Step Settings")){
            toFolderName = "Quick_Step_Settings";

        }
        else if(toFolder.equalsIgnoreCase("RSS Feeds")){
            toFolderName = "RSS_Feeds";

        }
        else{
            toFolderName = toFolder;
        }


        for(ChildFolders childFolders: selectionList){
            String sender_Name = childFolders.getSender();
            String sender_subject = childFolders.getSubject();
            String sender_date = childFolders.getTime();

            //Deleting Selected item from Listview
            //Method
            dbHelper.move(sender_Name, sender_subject, sender_date, foldervalue, toFolderName);

            Toast.makeText(getApplicationContext(),"Moved from: "+" ' "+foldervalue+" ' "+" to: "+toFolderName,Toast.LENGTH_LONG).show();
            //Clears Lisview from Data
            mChildFoldersList.clear();
            //Fetches data from DB and populates Listview
            fetchFromDB();
            adapter.notifyDataSetChanged();

        }
    }

    public void copyEmails(String toFolder){

        String toFolderName;
        if(toFolder.equalsIgnoreCase("Deleted Items")){
            toFolderName = "Deleted_Items";

        }
        else if(toFolder.equalsIgnoreCase("Conversation Action Settings")){
            toFolderName = "Conversation_Action_Settings";

        }
        else if(toFolder.equalsIgnoreCase("Junk E-mail")){
            toFolderName = "Junk_E_mail";

        }
        else if(toFolder.equalsIgnoreCase("Quick Step Settings")){
            toFolderName = "Quick_Step_Settings";

        }
        else if(toFolder.equalsIgnoreCase("RSS Feeds")){
            toFolderName = "RSS_Feeds";

        }
        else{
            toFolderName = toFolder;
        }


        for(ChildFolders childFolders: selectionList){
            String sender_Name = childFolders.getSender();
            String sender_subject = childFolders.getSubject();
            String sender_date = childFolders.getTime();

            //Deleting Selected item from Listview
            //Method
            dbHelper.copy(sender_Name, sender_subject, sender_date, foldervalue, toFolderName);

            Toast.makeText(getApplicationContext(),"Copied from: "+" ' "+foldervalue+" ' "+" to: "+toFolderName,Toast.LENGTH_LONG).show();
            //Clears Lisview from Data
            mChildFoldersList.clear();
            //Fetches data from DB and populates Listview
            fetchFromDB();
            adapter.notifyDataSetChanged();

        }
    }

    public void fetchTableNamesFromDB(){

        Cursor cursor =dbHelper.QueryData("SELECT name FROM sqlite_master WHERE type='table'");
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    String folderName = cursor.getString(0);
                    Cursor cursor2 =dbHelper.QueryData("SELECT * FROM "+folderName);
                    int count = cursor2.getCount();

                    RootFolders rootFolders = new RootFolders();
                    if(folderName.equalsIgnoreCase("Deleted_Items")){
                        rootFolders.setFolder_name("Deleted Items");
                        rootFolders.setFolder_count(count);
                    }
                    else if(folderName.equalsIgnoreCase("Conversation_Action_Settings")){
                        rootFolders.setFolder_name("Conversation Action Settings");
                        rootFolders.setFolder_count(count);
                    }
                    else if(folderName.equalsIgnoreCase("Junk_E_mail")){
                        rootFolders.setFolder_name("Junk E-mail");
                        rootFolders.setFolder_count(count);
                    }
                    else if(folderName.equalsIgnoreCase("Quick_Step_Settings")){
                        rootFolders.setFolder_name("Quick Step Settings");
                        rootFolders.setFolder_count(count);
                    }
                    else if(folderName.equalsIgnoreCase("RSS_Feeds")){
                        rootFolders.setFolder_name("RSS Feeds");
                        rootFolders.setFolder_count(count);
                    }
                    else{
                        rootFolders.setFolder_name(folderName);
                        rootFolders.setFolder_count(count);
                    }


                    if(cursor.getString(0).equals("sqlite_sequence")
                            || cursor.getString(0).equals("android_metadata")
                            || cursor.getString(0).equals("Deleted_Email")
                            || cursor.getString(0).equals("Deleted_Folder")
                            || cursor.getString(0).equals("Copied_Emails")
                            || cursor.getString(0).equals("Copied_Folders")
                            || cursor.getString(0).equals("Moved_Email")
                            || cursor.getString(0).equals("Moved_Folders")
                            || cursor.getString(0).equals("New_Folders")
                            ){
                        //do nothing i.e dont add these tables names in list
                    }
                    else{
                        folderNamesList.add(rootFolders);
                        //Collections.sort(mRootFolderList);      //Sorting in ascending
                        Collections.reverse(folderNamesList);   //Sorting in descending by reversing
                    }

                }while(cursor.moveToNext());
            }

            //Collections.reverse(mRootFolderList);   //Sorting in descending by reversing
            Collections.sort(folderNamesList);      //Sorting in ascending
        }
    }

    //ONCLICK METHOD FOR CONTEXTUAL ACTION BAR " MOVE ACTION "
    public void moveItem(MenuItem item){
        Toast.makeText(this, "Move Item", Toast.LENGTH_LONG).show();
    }

    //ONCLICK METHOD FOR CONTEXTUAL ACTION BAR " COPY ACTION "
    public void copyItem(MenuItem item){
        Toast.makeText(this, "Copy Item", Toast.LENGTH_LONG).show();
    }
    
    protected void fetchFromDB(){

        Cursor cursor =dbHelper.QueryData("SELECT * FROM "+FolderName);
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    ChildFolders childFolders = new ChildFolders();
                    childFolders.setSender(cursor.getString(1));
                    childFolders.setSubject(cursor.getString(3));
                    childFolders.setTime(cursor.getString(4));

                    mChildFoldersList.add(childFolders);
                    Collections.sort(mChildFoldersList);      //Sorting in ascending


                }while(cursor.moveToNext());
            }

            Collections.reverse(mChildFoldersList);   //Sorting in descending by reversing
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.id_search:
                Toast.makeText(getApplicationContext(),"Search",Toast.LENGTH_SHORT).show();
                break;


            default:
                //finish() is for back button
                finish();
                break;
        }
        return true;
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_childfolders, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        final SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select the Action");
        menu.add(0, v.getId(), 0, "Delete");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Copy");
        menu.add(0, v.getId(), 0, "Move");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle()=="Delete"){
            Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="Copy"){
            Toast.makeText(getApplicationContext(),"Copy",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="Move"){
            Toast.makeText(getApplicationContext(),"Move",Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }
}

