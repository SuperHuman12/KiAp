package com.developer.android.quickveggis.ui.activity;
/*Chanded by happyandhappy on 11/2/2017*/
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.android.quickveggis.R;
import com.developer.android.quickveggis.api.ServiceAPI;
import com.developer.android.quickveggis.api.VeggiesService;
import com.developer.android.quickveggis.api.response.ResponseCallback;
import com.developer.android.quickveggis.db.HistoryRepo;
import com.developer.android.quickveggis.model.Category;
import com.developer.android.quickveggis.model.Manufacturer;
import com.developer.android.quickveggis.model.WalletHistory;
import com.developer.android.quickveggis.ui.adapter.WalletHistoryAdapter;
import com.developer.android.quickveggis.ui.adapter.brandslistAdapter;
import com.developer.android.quickveggis.ui.adapter.retailerslistAdapter;
import com.developer.android.quickveggis.ui.fragments.AllProductsFragment;
import com.developer.android.quickveggis.ui.fragments.CartFragment;
import com.developer.android.quickveggis.ui.fragments.FilterFragment;
import com.developer.android.quickveggis.ui.fragments.ProductsFragment;
import com.developer.android.quickveggis.ui.utils.FragmentUtils;
import com.developer.android.quickveggis.ui.utils.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.developer.android.quickveggis.App.categories;
import static com.developer.android.quickveggis.App.getContext;

public class ProductsActivity extends AppCompatActivity {
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btnFilter)
    ImageView txtFilter;
    @Bind(R.id.categoryMenuLayout)
    RelativeLayout categoryMenuLayout;
    @Bind(R.id.categoryText)
    TextView categoryText;
    @Bind(R.id.categoryRV)
    RecyclerView categoryRV;
    @Bind(R.id.dimmer)
    ImageView dimmer;
    @Bind(R.id.retailers_rv)
    RecyclerView retailers_rv;
    @Bind(R.id.radioAnswers)
    RadioGroup sortOption1;
    @Bind(R.id.brands_rv)
    RecyclerView brands_rv;
    @Bind(R.id.content)
    FrameLayout content_lay;



    public static int selectedPosition = -1;
    public static int selectedPositionBrands = -1;
    Animation animShow,animHide;
    LinearLayout filters_layout;
    LinearLayout filters_layout_child;
    TextView reset_tv,close_tv;
    Map<String, Boolean> selected;
    List<String> brands;
    retailerslistAdapter adapter;
    LinearLayout filter_parent;
    brandslistAdapter brandsAdapter;
    ServiceAPI serviceapi;



    public ArrayList<Manufacturer> manifactlist;
    public Category category;
    public List<String> categoryList = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    ArrayList<Integer> images = new ArrayList<>();
    int index = 0;

    public static ProductsActivity _inst;

    /* renamed from: com.quickveggies.quickveggies.ui.activity.ProductsActivity.1 */
    class C02651 implements OnClickListener{
        C02651(){
        }

        public void onClick(View v){
            /* Click on cart */
            // The Drawer
           //ProductsActivity.this.toggleMenu();

            //The Cart..
            Intent i = new Intent(getContext(),CartActivity.class);
            startActivity(i);
        }
    }


    class C02962 implements OnClickListener {
        C02962() {
        }

        public void onClick(View v) {

            //Toast.makeText(ProductsActivity.this, "Working!!!", Toast.LENGTH_SHORT).show();
            filters_layout_child.startAnimation(animHide);
            filters_layout.setVisibility(View.GONE);
            filters_layout_child.setVisibility(View.GONE);
            content_lay.setVisibility(View.VISIBLE);

             //      iTem - 1 -
            String item1 = "";
            switch (sortOption1.getCheckedRadioButtonId()){
                case R.id.first:
                    item1 = "1";
                    break;
                case R.id.second:
                    item1 = "2";
                    break;
                case R.id.third:
                    item1 = "3";
                    break;
                case R.id.fourth:
                    item1 = "4";
                    break;
                default:
                    break;
            }
            //      iTem - 2 -
            String item2 = "";
            //item2 = sortOption2.getText().toString();
            //      iTem - 3 -
            String item3 = "";

            filter(item1, item2, item3);
        }
    }


    private void filter(String item1, String item2, String item3) {

        // New:  dateAdded = "2016-05-20 12:05:43"
        // Popularity:  popularity="8.00", viewed="53", rating=0, reviews.reviewTotal="0", reward=null
        // Ending soon: dateAvailable= "2016-05-20 12:05:43";
        // SavingHighToLow: TotalTaskAmount="8.00"


        for (int i = 0; i < brands.size(); i ++) {
            String item = (String) this.brands.get( i );
            boolean isSelected = ((Boolean) this.selected.get(item)).booleanValue();
            if (isSelected) {
                item3 = item3 + " ," + brands.get(i);
            }
        }

        ((ProductsActivity) this).doFilte(item1, "", item3);

    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ProductsActivity.class);
    }

    ProductsFragment    productsFragment;
    AllProductsFragment    allProductsFragment;
    public FilterFragment      filterFragment;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);


        serviceapi = new ServiceAPI();
        serviceapi.getManufacturers(new ResponseCallback<ArrayList<Manufacturer>>() {
            @Override
            public void onSuccess(ArrayList<Manufacturer> data){
                manifactlist = data;
                setbrandsRvAdapter();
            }

            @Override
            public void onFailure(String error) {

            }
        });

        filter_parent = (LinearLayout) findViewById(R.id.filter_parent);
        animShow = AnimationUtils.loadAnimation(getContext(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( getContext(), R.anim.view_hide);
        filters_layout = (LinearLayout) findViewById(R.id.filters_layout);
        filters_layout_child = (LinearLayout) findViewById(R.id.filters_layout_child);
        reset_tv = (TextView) findViewById(R.id.reset_tv);
        close_tv = (TextView) findViewById(R.id.close_tv);
        this.selected = new HashMap();
        this.brands = new ArrayList();
        this.close_tv.setOnClickListener(new C02962());


        reset_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){
                filter("1", "", "");
                filters_layout_child.startAnimation(animHide);
                filters_layout.setVisibility(View.GONE);
                filters_layout_child.setVisibility(View.GONE);
                content_lay.setVisibility(View.VISIBLE);
            }
        });


        if (getIntent().hasExtra("category-list")){
            categoryList = getIntent().getStringArrayListExtra("category-list");
        }
        if (getIntent().hasExtra("index")) {
            index = getIntent().getIntExtra("index", 0);
        }
        category = categories.get(index);

        ButterKnife.bind(this);

        if (categoryList.size() > 0){
            categoryText.setText(categoryList.get(index));
        }

        data.add("More mega Store");
        data.add("24SEVEN");
        data.add("easyday");
        data.add("Wallmart");
        data.add("The Home Deput");
        data.add("H M");

        images.add(R.drawable.ic_more);
        images.add(R.drawable.ic_twenty_four);
        images.add(R.drawable.ic_easy_day);
        images.add(R.drawable.ic_walmart);
        images.add(R.drawable.ic_home);
        images.add(R.drawable.ic_hm);


        categoryRV.setLayoutManager(new GridLayoutManager(ProductsActivity.this,3));
        CategoryAdapter adapter = new CategoryAdapter();
        categoryRV.setAdapter(adapter);

        categoryRV.addOnItemTouchListener(new RecyclerItemClickListener(ProductsActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                categoryRV.setVisibility(View.GONE);
                dimmer.setVisibility(View.GONE);

                if (!categoryText.getText().equals(categoryList.get(i))){
                    categoryText.setText(categoryList.get(i));

                    filterFragment=FilterFragment.newInstance();

                    if (categoryList.get(i).equals(getString(R.string.all))){
                        allProductsFragment=allProductsFragment.newInstance(categories.get(i));
                        FragmentUtils.popBackStack(ProductsActivity.this);
                        FragmentUtils.changeFragment(ProductsActivity.this,R.id.content,allProductsFragment,false);
                    }else{
                        productsFragment=ProductsFragment.newInstance(categories.get(i));
                        FragmentUtils.popBackStack(ProductsActivity.this);
                        FragmentUtils.changeFragment(ProductsActivity.this,R.id.content,productsFragment, false);
                    }

                    FragmentUtils.changeFragment(ProductsActivity.this,R.id.filter,filterFragment,false);
                }
            }
        }));

        categoryMenuLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryRV.getVisibility()==View.VISIBLE){
                    categoryRV.setVisibility(View.GONE);
                    dimmer.setVisibility(View.GONE);
                }else{
                    categoryRV.setVisibility(View.VISIBLE);
                    dimmer.setVisibility(View.VISIBLE);
                }

            }
        });
        filterFragment = FilterFragment.newInstance();

        if (categoryList.get(index).equals(getString(R.string.all))){
            allProductsFragment = AllProductsFragment.newInstance(categories.get(index));
            FragmentUtils.popBackStack(ProductsActivity.this);
            FragmentUtils.changeFragment(ProductsActivity.this, R.id.content, allProductsFragment, false);
        } else {
            productsFragment = ProductsFragment.newInstance(categories.get(index));
            FragmentUtils.popBackStack(ProductsActivity.this);
            FragmentUtils.changeFragment(ProductsActivity.this, R.id.content, productsFragment, false);
        }
        FragmentUtils.changeFragment(ProductsActivity.this, R.id.filter, filterFragment, false);

        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        this.txtFilter.setOnClickListener(new C02651());
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        _inst = this;
         setRvAdapter();

    }


    void setbrandsRvAdapter(){

        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        brands_rv.setLayoutManager(layoutManager);
        brandsAdapter = new brandslistAdapter(getContext(), manifactlist,images);
        brands_rv.setAdapter(this.brandsAdapter);

        brands_rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position){
                        // TODO Handle item click
                       // Toast.makeText(ProductsActivity.this, "working..", Toast.LENGTH_SHORT).show();
                        selectedPositionBrands = position;
                        brandsAdapter.notifyDataSetChanged();
                    }
                })
        );

    }


    void setRvAdapter(){
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        retailers_rv.setLayoutManager(layoutManager);
        adapter = new retailerslistAdapter(getContext(), data,images);
        retailers_rv.setAdapter(this.adapter);

        retailers_rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position){
                        // TODO Handle item click
                       // Toast.makeText(ProductsActivity.this, "working..", Toast.LENGTH_SHORT).show();
                        selectedPosition = position;
                        adapter.notifyDataSetChanged();
                    }
                })
        );

    }


    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

        public class Holder extends RecyclerView.ViewHolder {
            @Bind(R.id.imgIcon)
            ImageView imgIcon;
            @Bind(R.id.txtTitle)
            TextView txtTitle;
            @Bind(R.id.newsNumtxt)
            Button newsNumtxt;

            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public CategoryAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryAdapter.Holder(LayoutInflater.from(ProductsActivity._inst).inflate(R.layout.item_category, parent, false));
        }

        public void onBindViewHolder(CategoryAdapter.Holder holder, int position) {
            Category category = categories.get(position);
            holder.txtTitle.setText(category.getName());

            if (Integer.parseInt(category.getNewoffers())>0)  holder.newsNumtxt.setText(category.getNewoffers()+"  New");
            else {
                holder.newsNumtxt.setBackgroundResource(R.color.white);
                holder.newsNumtxt.setText("");
            }
            Picasso.with(ProductsActivity.this).load(category.getImage().replace(" ", "%20")).into(holder.imgIcon);
        }

        public int getItemCount() {
            return categories.size();
        }
    }


    public void toggleMenu() {
        if (this.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            this.drawerLayout.closeDrawers();
        } else {
            this.drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    public void doFilte(String item1, String item2, String item3) {
        if (categoryText.getText().equals(getString(R.string.all))) {
            allProductsFragment.doFilte(item1, item2, item3);
        } else {
            productsFragment.doFilte(item1, item2, item3);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _inst = null;
    }

    private void fillBrands(){
        this.brands.clear();
        Toast.makeText(_inst, "reset", Toast.LENGTH_SHORT).show();

        this.brands = new HistoryRepo(getApplicationContext()).getHistoryBrands();

        for (String item : this.brands) {
            this.selected.put(item, Boolean.valueOf(false));
        }
        adapter.notifyDataSetChanged();
    }

}
