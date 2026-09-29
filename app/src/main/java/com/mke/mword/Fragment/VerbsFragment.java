package com.mke.mword.Fragment;

import static com.mke.mword.Database.Constants.CURRIENTPAGETYPE;
import static com.mke.mword.Database.Constants.DB_VERBS_PAGES;
import static com.mke.mword.Database.Constants.SCREENPAGESTATUS;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_EXAM;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_LISTEN;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_TALK;
import static com.mke.mword.Database.Constants.SCREEN_PAGE_WRITE;
import static com.mke.mword.Database.Constants.TVSCREEN_MAXLINES;
import static com.mke.mword.Database.Constants.TVSCREEN_VERB_PAGE_MAXLINES;
import static com.mke.mword.Database.mkDB.nPageNumber;
import static com.mke.mword.Database.mkDB.nWordNumber;
import static com.mke.mword.MainActivity.g_Context;
import static com.mke.mword.MainActivity.mkdb;
import static com.mke.mword.Utils.Utils.GetListViewList;

import android.content.*;
import android.os.Bundle;
import android.text.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.mke.mword.*;
import com.mke.mword.ExpListView.*;

public class VerbsFragment extends androidx.fragment.app.Fragment {
    private ActionSlideExpandableListView listVerbs=null;
    private ArrayAdapter<SpannableString> pAdapter=null;
    public  boolean bRefreshList=false;
    public  void SetUpdate() {bRefreshList=true;}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.verbs_fragment, container, false);
        listVerbs = (ActionSlideExpandableListView) v.findViewById(R.id.verbs_frag_listView);
// fill the list with data
        listVerbs.setAdapter(buildDummyData());
        listVerbs.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
            @Override
            public void onClick(View listView, View buttonview, int position) {
                nPageNumber = position;
                nWordNumber= mkdb.GetPageLastWVSNumber(CURRIENTPAGETYPE,nPageNumber);
                if(buttonview.getId()==R.id.buttonA) {
                    SCREENPAGESTATUS=SCREEN_PAGE_LISTEN;
                    TVSCREEN_MAXLINES=TVSCREEN_VERB_PAGE_MAXLINES;
                    startActivity(new Intent(g_Context, ScreenView.class));
                }else if (buttonview.getId()==R.id.buttonB){
                    SCREENPAGESTATUS=SCREEN_PAGE_TALK;
                    startActivity(new Intent(g_Context, ScreenView.class));
                }else if (buttonview.getId()==R.id.buttonC){
                    SCREENPAGESTATUS=SCREEN_PAGE_WRITE;
                    startActivity(new Intent(g_Context, WriteScreen.class));
                }else if(buttonview.getId()==R.id.buttonD){
                    SCREENPAGESTATUS=SCREEN_PAGE_EXAM;
                    startActivity(new Intent(g_Context, quizScreen.class));
                }
                listVerbs.collapse();
            }

            // note that we also add 1 or more ids to the setItemActionListener
            // this is needed in order for the listview to discover the buttons
        }, R.id.buttonA, R.id.buttonB,R.id.buttonC,R.id.buttonD);


        return v;
    }
    public static androidx.fragment.app.Fragment newInstance(String text) {
        VerbsFragment  f = new VerbsFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
    public ListView GetListView(){ return listVerbs;}

    protected ListAdapter GetAdapter(){ return pAdapter;}

    protected ArrayAdapter<SpannableString> buildDummyData() {
        pAdapter =new ArrayAdapter<SpannableString>(g_Context,R.layout.expandable_list_item,
                R.id.listview_button_text,GetListViewList(DB_VERBS_PAGES));
        return pAdapter;
    }

    public void UpdateList(){
        if (pAdapter != null && !pAdapter.isEmpty() && bRefreshList) {
            listVerbs.setAdapter(buildDummyData());
            pAdapter.notifyDataSetChanged();
            bRefreshList = false;
        }
    }

}
