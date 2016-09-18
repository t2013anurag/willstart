package com.prince.android.willstart.Entity.Activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.prince.android.willstart.Boundary.API.ConnectAPI;
import com.prince.android.willstart.Entity.Instances.Company;
import com.prince.android.willstart.Entity.Instances.SearchResult;
import com.prince.android.willstart.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Prince Bansal Local on 9/17/2016.
 */

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ConnectAPI.ServerAuthenticateListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private SearchManager searchManager;
    private SearchView searchView;
    private String keyword;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private CoordinatorLayout root;
    private FloatingActionButton floatingActionButton;
    private List<Company> searchResultList;

    private ConnectAPI connectAPI;
    private SearchResultsAdapter adapter;
    private String mQuery;
    private SearchResult searchResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setInit();
        setData();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        connectAPI = new ConnectAPI();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        root=(CoordinatorLayout)findViewById(R.id.root);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
    }

    private void setInit() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Will Start");
        if (getIntent().hasExtra("keyword")) {
            keyword = getIntent().getStringExtra("keyword");
        }
        handleIntent(getIntent());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        connectAPI.setServerAuthenticateListener(this);
        setProgress(true);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this,ServicesInputActivity.class);
                i.putExtra("category",mQuery);
                startActivity(i);
            }
        });
    }

    private void setProgress(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(b ? View.GONE : View.VISIBLE);
        fab.setVisibility(b ? View.GONE : View.VISIBLE);
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.scale);
        hyperspaceJump.setStartTime(500);
        fab.startAnimation(hyperspaceJump);
    }

    private void setData() {
        /*List<String> imageUrls = new ArrayList<>();
        List<String> services = new ArrayList<>();
        services.add("Online Booking");
        services.add("Mobile App");
        services.add("Home Delivery");
        services.add("Menu HandPicking");
        imageUrls.add("https://pbs.twimg.com/profile_images/762369348300251136/5Obhonwa.jpg");
        imageUrls.add("http://www.logospike.com/wp-content/uploads/2015/11/Company_Logos_01.jpg");
        imageUrls.add("https://www.seeklogo.net/wp-content/uploads/2014/12/twitter-logo-vector-download.jpg");
        imageUrls.add("http://www.mobygames.com/images/i/26/09/650109.png");
        imageUrls.add("http://www.jamesgood.co.uk/sites/default/files/Logo-Blog_58.png");
        List<String> names = new ArrayList<>();
        names.add("Google");
        names.add("Dell");
        names.add("Twitter");
        names.add("EA Sports");
        names.add("Starbucks");
        List<Company> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Company c = new Company();
            c.setPicUrl(imageUrls.get(i));
            c.setName(names.get(i));
            c.setServicesOffered(services);
            list.add(c);
        }
        searchResultList = list;
        recyclerView.setAdapter(new SearchResultsAdapter(this));*/
        if(searchResultList!=null&&searchResultList.size()>0){
            adapter=new SearchResultsAdapter(this);
            recyclerView.setAdapter(adapter);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        if (searchView != null && keyword != null) {
            searchView.setQuery(null, false);
            searchView.setQuery(keyword, true);
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
        Log.i(TAG, "onNewIntent: ");
    }

    private void handleIntent(Intent intent) {
        Log.i(TAG, "handleIntent: ");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, "handleIntent: " + query);
            //use the query to search
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mQuery=query;
        showMessage(query);
        connectAPI.fetchMessages(query);
        return false;
    }

    private void showMessage(String query) {
        // Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        showMessage(newText);
        return false;
    }

    @Override
    public void onRequestInitiated(int code) {
        setProgress(true);
    }

    @Override
    public void onRequestCompleted(int code, Object searchResultList) {
        setProgress(false);
        List<SearchResult> list=(List<SearchResult>)searchResultList;
        this.searchResultList=list.get(0).getCompanyList();
        searchResult=list.get(0);
        setData();
    }

    @Override
    public void onRequestError(int code, String message) {
        setError();
    }

    private void setError() {
        setProgress(true);
        progressBar.setVisibility(View.GONE);
        Snackbar.make(root,"Error occured",Snackbar.LENGTH_SHORT).show();
    }

    public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchViewHolder> {


        private final Context context;

        public SearchResultsAdapter(Context context) {
            this.context = context;
        }

        @Override
        public SearchResultsAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.row_item, parent, false);
            SearchViewHolder searchViewHolder = new SearchViewHolder(view);
            return searchViewHolder;
        }

        @Override
        public void onBindViewHolder(SearchViewHolder holder, int position) {
            holder.companyName.setText(searchResultList.get(position).getName());
            Glide.with(context).load(searchResultList.get(position).getPicUrl()).asBitmap().into(holder.companyLogo);
            if (searchResultList.get(position).getServicesOffered() == null) {
                Log.i(TAG, "onBindViewHolder: null");
            }
            holder.servicesRecycler.setAdapter(new ServicesAdapter(context, searchResultList.get(position).getServicesOffered()));
        }

        @Override
        public int getItemCount() {
            return searchResultList.size();
        }

        public class SearchViewHolder extends RecyclerView.ViewHolder {

            private CircleImageView companyLogo;
            private TextView companyName;
            private RecyclerView servicesRecycler;

            public SearchViewHolder(View itemView) {
                super(itemView);
                companyLogo = (CircleImageView) itemView.findViewById(R.id.companyLogo);
                companyName = (TextView) itemView.findViewById(R.id.companyName);
                servicesRecycler = (RecyclerView) itemView.findViewById(R.id.recViewFeatures);
                servicesRecycler.setLayoutManager(new GridLayoutManager(context, 2));
            }
        }
    }

    public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {


        private final Context context;
        private List<String> featuresList;

        public ServicesAdapter(Context context, List<String> list) {
            this.context = context;
            featuresList = list;
        }

        @Override
        public ServicesAdapter.ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.feature_item, parent, false);
            ServiceViewHolder serviceViewHolder = new ServiceViewHolder(view);

            return serviceViewHolder;
        }

        @Override
        public void onBindViewHolder(ServiceViewHolder holder, int position) {
            String feat=featuresList.get(position);
            String[] arr=feat.split("_");
            StringBuilder  builder=new StringBuilder();
            for(int i=0;i<arr.length;i++){
                if(i!=arr.length-1){
                    builder.append(String.valueOf(arr[i].charAt(0)).toUpperCase()+arr[i].substring(1)+" ");
                }else{
                    builder.append(String.valueOf(arr[i].charAt(0)).toUpperCase()+arr[i].substring(1));
                }
            }
            if(searchResult.getDescriptions().containsKey(feat)){
                holder.featureName.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            }
            holder.featureName.setText(builder.toString());

        }

        @Override
        public int getItemCount() {
            return featuresList.size();
        }

        public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView featureName;

            public ServiceViewHolder(View itemView) {
                super(itemView);
                featureName = (TextView) itemView.findViewById(R.id.featureName);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(searchResult!=null){
                    if(searchResult.getDescriptions().containsKey(featuresList.get(getAdapterPosition()))){
                        final Dialog dialog = new Dialog(context);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.alert_dialog_view);

                        TextView text = (TextView) dialog.findViewById(R.id.alertText);
                        text.setText(searchResult.getDescriptions().get(featuresList.get(getAdapterPosition())));
                        dialog.show();
                    }
                }
            }
        }
    }
}
