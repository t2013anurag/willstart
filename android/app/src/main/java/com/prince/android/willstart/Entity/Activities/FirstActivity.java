package com.prince.android.willstart.Entity.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.prince.android.willstart.R;

public class FirstActivity extends AppCompatActivity implements ImageButton.OnClickListener{


    private EditText et;
    private ImageButton ib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        et=(EditText)findViewById(R.id.keyword);
        ib=(ImageButton)findViewById(R.id.imageButton);
        ib.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(FirstActivity.this, MainActivity.class);
        if(TextUtils.isEmpty(et.getText().toString())){
            et.setError("Please enter something");
        }else{
            intent.putExtra("keyword",et.getText().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //Pair<View,String> text=new Pair<>(findViewById(R.id.goal),getString(R.string.transition_activity_text));
                //Pair<View,String> bg=new Pair<>(findViewById(R.id.dashboard_button),getString(R.string.transition_activity_bg));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(FirstActivity.this,findViewById(R.id.keyword),"transitionShow");

                startActivity(intent,options.toBundle());
            }else {
                startActivity(intent);
            }
        }




    }
}
