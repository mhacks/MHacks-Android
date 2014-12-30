package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.mhacks.android.data.model.Award;
import com.mhacks.iv.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class AwardsFragment extends Fragment{

    private View mAwardsFragView;
//    private String[] descriptions = {
//            "Awarded for best VR project. Put your new Oculus Rift to use.",
//            "Awarded for best pitch. Meet with Peter Thiel and learn from the entrepreneur god himself.",
//            "Awarded for the project that best implements the Firebase API. Use your premium service to make more killer apps.",
//            "Awarded for best Arduino project. Get to work on more projects with your own brand new Arduino.",
//            "Awarded for best software project. Invest that money back into hacking."
//    };
//    private String[] prizes = {
//            "1 Oculus Rift Per Teammate",
//            "1 On 1 With Peter Thiel",
//            "2 Years of Firebase Premium",
//            "1 Arduino Per Teammate",
//            "$1000"
//    };
//    private String[] values = {
//            "$300",
//            "-",
//            "$100",
//            "$100",
//            "$1000"
//    };
//    private String[] titles = {
//            "Best VR Project",
//            "Best Pitch",
//            "Best Use of Firebase API",
//            "Best Arduino Project",
//            "Best Software Project"
//    };

    private List<Award> awardList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mAwardsFragView = inflater.inflate(R.layout.fragment_awards, container, false);

        //Put code for instantiating views, etc here. (before the return statement.)

        awardList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Award");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objectList, ParseException e) {
                if (e == null) {
                    Log.d("Awards", "Retrieved " + objectList.size() + " awards");
                    for (ParseObject p : objectList) {
                        Award a = (Award) p;
                        awardList.add(a);
                    }
                }
                else {
                    Log.d("Awards", "Error: " + e.getMessage());
                }
            }
        });

        CustomGrid adapter = new CustomGrid(mAwardsFragView.getContext());
        GridView gridView = (GridView) mAwardsFragView.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "You clicked an item" + position, Toast.LENGTH_SHORT).show();
            }
        });

        return mAwardsFragView;
    }

    private class CustomGrid extends BaseAdapter {

        private Context mContext;

        public CustomGrid(Context c) {
            mContext = c;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                grid = new View(mContext);
            }
            grid = inflater.inflate(R.layout.award_grid_item, null);
            return grid;
        }

    }
}