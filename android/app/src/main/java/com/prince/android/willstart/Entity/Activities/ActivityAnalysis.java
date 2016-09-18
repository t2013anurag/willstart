package com.prince.android.willstart.Entity.Activities;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.prince.android.willstart.Entity.Instances.SuggestionResult;
import com.prince.android.willstart.R;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shuvam Ghosh on 9/18/2016.
 */

public class ActivityAnalysis extends AppCompatActivity {

    private RecyclerView rv;
    private TextView successRateView;
    private ImageView star1,star2,star3,star4,star5;
    private ImageView[] starsViewArray;
    private NestedScrollView nestedScrollView;

    private RecyclerViewAdapter adapter;
    private ArrayList<String> suggestions;
    private int suggChecked=0;
    private SuggestionResult mResults;
    private CharSequence[] featuresResult;
    private Toolbar toolbar;
    private String TAG=ActivityAnalysis.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        suggestions= new ArrayList<String>();
        init();
        setInit();
        setData();
    }

    private void init() {
        rv=(RecyclerView)findViewById(R.id.recView);
        mResults= Parcels.unwrap(getIntent().getParcelableExtra("results"));
        featuresResult=getIntent().getCharSequenceArrayExtra("sugg");
        star1=(ImageView)findViewById(R.id.start1);
        star2=(ImageView)findViewById(R.id.start2);
        star3=(ImageView)findViewById(R.id.start3);
        star4=(ImageView)findViewById(R.id.start4);
        star5=(ImageView)findViewById(R.id.start5);
        nestedScrollView=(NestedScrollView)findViewById(R.id.nested_scroll);
        successRateView=(TextView)findViewById(R.id.success_rate);
        starsViewArray=new ImageView[5];
        starsViewArray[0]=star1;
        starsViewArray[1]=star2;
        starsViewArray[2]=star3;
        starsViewArray[3]=star4;
        starsViewArray[4]=star5;
        toolbar=(Toolbar)findViewById(R.id.toolbar);
    }

    private void setInit() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Analysis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String s[]=mResults.getSuggestions().get(0).split(",");
        Pattern pattern=Pattern.compile("[A-Za-z ]+");
        List<String> sugg=new ArrayList<>();
        for(int i=0;i<s.length;i++) {
            Matcher matcher = pattern.matcher(s[i]);
            while (matcher.find()){
                String match=matcher.group();
                match=match.trim();
                if(!TextUtils.isEmpty(match))
                sugg.add(matcher.group());
            }
        }
        mResults.setSuggestions(sugg);
        rv.setNestedScrollingEnabled(false);


    }

    private void setData() {
        //To be changed
        successRateView.setText(mResults.getSuccessRate()+"");
        showStars();
        adapter=new RecyclerViewAdapter();
        rv.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               nestedScrollView.fullScroll(Gravity.TOP);
            }
        },0);

    }

    private void showStars() {
        int numberOfStars=(int)mResults.getSuccessRate()/20;
        for (int i = 0; i < numberOfStars+1; i++) {
            starsViewArray[i].setImageResource(R.drawable.ic_star);
        }
    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyView> {


        public RecyclerViewAdapter() {

        }

        @Override
        public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_item,parent,false);
            MyView myView= new MyView(v);
            return myView;
        }

        @Override
        public void onBindViewHolder(MyView holder, int position) {

            if(position<featuresResult.length) {
                holder.suggestion.setText(featuresResult[position]);
            }else{
                holder.suggestion.setText(mResults.getSuggestions().get(position-featuresResult.length));
            }

        }

        @Override
        public int getItemCount() {
            return mResults.getSuggestions().size()+featuresResult.length;
        }

        public class MyView extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView suggestion;
            private CheckBox addCheck;

            public MyView(View itemView) {
                super(itemView);
                suggestion=(TextView)itemView.findViewById(R.id.suggestion);
                addCheck=(CheckBox)itemView.findViewById(R.id.check_add);
                addCheck.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(!((CheckBox)v).isChecked()){
                    suggChecked++;
                    changeScore();
                }else{
                    suggChecked--;
                    changeScore();
                }
            }
        }
    }

    private void changeScore() {
        String per=successRateView.getText().toString();
        successRateView.setText(""+(mResults.getSuccessRate()+suggChecked));
    }
}
