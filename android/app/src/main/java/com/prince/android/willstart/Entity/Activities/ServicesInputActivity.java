package com.prince.android.willstart.Entity.Activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prince.android.willstart.Boundary.API.ConnectAPI;
import com.prince.android.willstart.Entity.Instances.InputView;
import com.prince.android.willstart.Entity.Instances.SuggestionResult;
import com.prince.android.willstart.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ServicesInputActivity extends AppCompatActivity implements ConnectAPI.ServerAuthenticateListener {

    private static final String TAG = SearchResultsActivity.class.getSimpleName();
    private LinearLayout inputContainer;
    private LinearLayout.LayoutParams params;
    private List<InputView> inputViewList;
    private Toolbar toolbar;

    private ConnectAPI connectApi;
    private FloatingActionButton fabButton;

    private String category;
    private CoordinatorLayout root;
    private SuggestionResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_bullets);
        init();
        setInit();
    }


    private void init() {
        inputContainer=(LinearLayout)findViewById(R.id.innerLinearLayout);
        root=(CoordinatorLayout)findViewById(R.id.root);
        inputViewList=new ArrayList<>();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        connectApi=new ConnectAPI();
        fabButton=(FloatingActionButton)findViewById(R.id.fabBullet);
        category=getIntent().hasExtra("category")?getIntent().getStringExtra("category"):"";
    }

    private void setInit() {
        addInputView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        connectApi.setServerAuthenticateListener(this);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputViewList.get(inputViewList.size()-1).getInputCheck().setChecked(true);
                addInputView();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.suggestion_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.done){
            List<String> phraseList=new ArrayList<>();
            for(InputView iv:inputViewList){
                String text=iv.getInputField().getText().toString();
                if(!TextUtils.isEmpty(text))
                phraseList.add(text);
            }
            connectApi.fetchSuggestions(phraseList,category);
        }
        return super.onOptionsItemSelected(item);

    }

    private void addInputView() {
        final InputView inputView=new InputView();
        final View v=getLayoutInflater().inflate(R.layout.service_input_item,inputContainer,false);
        inputView.setInputField((EditText) v.findViewById(R.id.phrase));
        inputView.setInputCheck((CheckBox) v.findViewById(R.id.check));
        inputView.getInputField().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_NEXT){
                    Log.i(TAG, "onEditorAction: ");
                    if(!TextUtils.isEmpty(v.getText().toString())) {
                        addInputView();
                        inputView.getInputCheck().setChecked(true);
                    }
                }
                return false;
            }
        });
        inputView.getInputCheck().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Log.i(TAG, "onClick: ");

                if(!((CheckBox)v2).isChecked())
                {
                    Log.i(TAG, "onClick: on unchecked");
                    inputContainer.removeView(v);
                }
            }
        });
        inputViewList.add(inputView);
        inputContainer.addView(v);
    }

    @Override
    public void onRequestInitiated(int code) {
        Log.i(TAG, "onRequestInitiated: ");
    }

    @Override
    public void onRequestCompleted(int code, Object searchResultList) {
        if(code==ConnectAPI.FETCH_SUGGESTIONS_CODE) {
            Log.i(TAG, "onRequestCompleted: ");
            result = (SuggestionResult) searchResultList;
        }
        if(code==ConnectAPI.FETCH_RECOMMEND_CODE){
            List<String> sugg=(List<String>)searchResultList;
            CharSequence[] arr=new CharSequence[sugg.size()];
            Parcelable parcelable= Parcels.wrap(SuggestionResult.class,result);
            Intent intent=new Intent(this,ActivityAnalysis.class);
            intent.putExtra("results",parcelable);
            intent.putExtra("sugg",arr);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestError(int code, String message) {
        Log.i(TAG, "onRequestError: ");
        showMessage("Network error");
    }

    private void showMessage(String s) {
        Snackbar.make(root,s,Snackbar.LENGTH_SHORT).show();
    }
}
