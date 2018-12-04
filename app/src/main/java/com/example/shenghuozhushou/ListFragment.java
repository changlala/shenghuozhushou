package com.example.shenghuozhushou;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> ,
        AdapterView.OnItemClickListener{
    private static final String TAG = "ListFragment";
    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };
    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    private final static int[] TO_IDS = {
            R.id.textview
    };

    // Define global mutable variables
    // Define a ListView object
    //未加限定符，包权限
    ListView mContactsList;
    // Define variables for the contact the user selects
    // The contact's _ID value
    long mContactId;
    // The contact's LOOKUP_KEY
    String mContactKey;
    // A content URI for the selected contact
    Uri mContactUri;
    // An adapter that binds the result Cursor to the ListView
    private SimpleCursorAdapter mCursorAdapter;

    Context mContext;

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the CONTACT_KEY column
    private static final int CONTACT_KEY_INDEX = 1;

    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME

            };
    private static final String SELECTION =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
    // Defines a variable for the search string
    private String mSearchString;
    // Defines the array to hold values that replace the ?
    private String[] mSelectionArgs = { mSearchString };
    public ListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContactsList = (ListView) inflater.inflate(R.layout.contacts_list_view,
                container, false);
        return mContactsList;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//
//        mContactsList =   inflater.inflate(R.layout.contacts_list_view,null).findViewById(R.id.listview);
//        mContactsList =  getActivity().findViewById(R.id.listview);


        mCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.contacts_item_view,null,
                FROM_COLUMNS,TO_IDS, 0);

        mContactsList.setAdapter(mCursorAdapter);
        mContactsList.setOnItemClickListener(this);

        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.d(TAG, "onCreateLoader: ");
        return new CursorLoader(getActivity(), ContactsContract.Contacts.CONTENT_URI,PROJECTION,
                null,null,null);
    }

    /*查询到一条数据，返回对应的结果cursor*/
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

//        while(cursor.moveToNext()!= false){
//            String name = cursor.getString(2);
//            Log.d(TAG, "onLoadFinished:  "+name);
//        }
//        cursor.moveToPosition(-1);

        //将查询所得cursor与适配器cursor交换，此操作会自动更新ListView
        mCursorAdapter.swapCursor(cursor);

        Log.d(TAG, "onLoadFinished:  "+mContactsList.getCount());

    }

    /*当侦测到结果cursor中存在stale 旧数据是时调用*/
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //删除SimpleCursorAdapter中的引用，否则会引发内存泄漏
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
        cursor.moveToPosition(position);
        mContactId = cursor.getLong(CONTACT_ID_INDEX);
        mContactKey = cursor.getString(CONTACT_KEY_INDEX);
        String name = cursor.getString(2);
        //通过_ID字段和LOOKUP_KEY字段得到指定联系人的contactUri
        mContactUri = ContactsContract.Contacts.getLookupUri(mContactId,mContactKey);
        //构建intent跳到详情页
        Intent intent = new Intent(Intent.ACTION_VIEW,mContactUri);
        if (intent != null && intent.resolveActivity(getActivity().getPackageManager()) != null) {//检查至少有一个符合的app
            startActivity(intent);
        }else{
            Log.d(TAG, "intent error");
        }
        Log.d(TAG, "onItemClick: "+name+" "+mContactUri);
//        ListFragment.this.getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(ListFragment.this.getActivity(),mContactUri.toString(),Toast.LENGTH_SHORT).show();
//            }
//        });
        // next ...
    }
}
