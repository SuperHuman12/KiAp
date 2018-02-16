package com.developer.android.quickveggis.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.android.quickveggis.R;
import com.developer.android.quickveggis.api.ServiceAPI;
import com.developer.android.quickveggis.api.response.ResponseCallback;
import com.developer.android.quickveggis.config.Config;
import com.developer.android.quickveggis.model.CartItem;
import com.developer.android.quickveggis.model.Category;
import com.developer.android.quickveggis.model.Product;
import com.developer.android.quickveggis.ui.activity.MainActivity;
import com.developer.android.quickveggis.ui.activity.ProductsActivity;
import com.developer.android.quickveggis.ui.activity.SearchActivity;
import com.developer.android.quickveggis.ui.adapter.ProductAdapter;
import com.developer.android.quickveggis.ui.utils.PreferenceUtil;
import com.developer.android.quickveggis.ui.utils.RecyclerItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.developer.android.quickveggis.App.shoppingListChanged;
import static com.developer.android.quickveggis.ui.utils.FadeAnim.startFadeInAnim;

public class ProductsFragment extends Fragment {
    ProductAdapter adapter;
    ArrayList<Product> dataSearch;
    ArrayList<Product> products;
    ArrayList<Product> productsFiltered;
    ArrayList<CartItem> cartItems;
    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.backToTop)
    RelativeLayout backToTop;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.bottomLayout)
    LinearLayout bottomLayout;
    @Bind(R.id.productsLoadingPage)
    LinearLayout productsLoadingPage;
    @Bind(R.id.noProudctsLayout)
    RelativeLayout noProudctsLayout;
    @Bind(R.id.tutorialLayout)
    RelativeLayout tutorialLayout;
    @Bind(R.id.filter_custom_lay)
    LinearLayout filter_custom_lay;
    @Bind(R.id.edtText_search)
    EditText edtText_search;
    @Bind(R.id.search_iv)
    ImageView search_iv;
    @Bind(R.id.cross_icon)
    ImageView cross_icon;

    LinearLayout filter_parent;
    TextView categoryText;
    LinearLayout filters_layout_child;
    Animation animShow,animHide;
    TextView close_tv;
    LinearLayout filters_layout;
    Map<String, Boolean> selected;
    List<String> brands;
    Category category;
    FrameLayout content_lay;

    /* renamed from: com.quickveggies.quickveggies.ui.fragment.ProductsFragment.1 */
    class C05681 implements RecyclerItemClickListener.OnItemClickListener{
        C05681(){
        }

        public void onItemClick(View view, int position) {
            if (position < 0 && position >= productsFiltered.size())    return;
            Intent intent = MainActivity.getStartIntent(getContext(), Config.PRODUCT_MODE);
            intent.putExtra("item", position);

            Bundle bundle = new Bundle();
            bundle.putString("product", new Gson().toJson(productsFiltered.get(position)));
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
        }
    }

    public ProductsFragment() {
        this.products = new ArrayList();
        this.productsFiltered = new ArrayList<>();
    }

    public static ProductsFragment newInstance(Category category) {
        Bundle args = new Bundle();
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(args);
        fragment.category = category;
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        this.selected = new HashMap();
        this.brands = new ArrayList();
        filters_layout_child = (LinearLayout) getActivity().findViewById(R.id.filters_layout_child);
        filters_layout = (LinearLayout) getActivity().findViewById(R.id.filters_layout);
        categoryText = (TextView) getActivity().findViewById(R.id.categoryText);
        filter_parent = (LinearLayout) getActivity().findViewById(R.id.filter_parent);


        animShow = AnimationUtils.loadAnimation(getContext(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( getContext(), R.anim.view_hide);

        tutorialLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                v.setVisibility(View.GONE);
            }
        });



        if(!categoryText.getText().toString().equals("Favourites")){
            filter_parent.setVisibility(View.VISIBLE);
        }else{
            filter_parent.setVisibility(View.GONE);
        }
        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });

        filter_custom_lay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                content_lay.setVisibility(View.GONE);
                filters_layout.setVisibility(View.VISIBLE);
                filters_layout_child.setVisibility(View.VISIBLE);
                filters_layout_child.startAnimation(animShow);
            }
        });

        cross_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtText_search.setText("");
            }
        });


        content_lay = (FrameLayout) getActivity().findViewById(R.id.content);
        products = new ArrayList();
        productsFiltered = new ArrayList<>();
        cartItems = new ArrayList<>();
        this.rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.adapter = new ProductAdapter(getContext(), productsFiltered);
        this.rv.setAdapter(this.adapter);
        this.rv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new C05681()));

        this.productsLoadingPage.setVisibility(View.VISIBLE);

        //Get product data from API Happyandhappy
        ServiceAPI.newInstance().getProductsByCategory(category.getCategoryId(), new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> data) {

                productsLoadingPage.setVisibility(View.GONE);
                if (data.size() > 0) {
                    rv.setVisibility(View.VISIBLE);
                    startFadeInAnim(rv);
                } else {
                    noProudctsLayout.setVisibility(View.VISIBLE);
                }
                ArrayList<String> arrayTemp = ((ProductsActivity) ProductsFragment.this.getActivity()).filterFragment.brandList;
                for ( Product proTemp: data ) {
                    if (proTemp.getManufacturer() != null) {
                        if ( !arrayTemp.contains(proTemp.getManufacturer()) ) {
                            ((ProductsActivity) ProductsFragment.this.getActivity()).filterFragment.brandList.add(proTemp.getManufacturer());
                        }
                    }
                }
                products.addAll(data);
                productsFiltered.addAll(data);

                //Get CartItem data from server
                ServiceAPI.newInstance().getCartItems(new ResponseCallback<ArrayList<CartItem>>() {
                    @Override
                    public void onSuccess(ArrayList<CartItem> data) {
                        cartItems.clear();
                        if (data.size()>0) cartItems.addAll(data);
                        //Compare the CartItem and Products
                        if (cartItems.size()>0){
                            ArrayList<Product> temp=new ArrayList<>();
                            for (Product proTemp:productsFiltered){
                                for (CartItem cartTemp:cartItems){
                                    if (proTemp.getId().equals(cartTemp.getProductId()))proTemp.setAddedCart();
                                }
                                temp.add(proTemp);
                            }
                            productsFiltered.clear();
                            productsFiltered.addAll(temp);
                        }
                        adapter.setProducts(productsFiltered);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                });

                if (PreferenceUtil.getBooleanFromPreference(getActivity(), Config.PRODUCTS_TUTORIAL_VISIBLE, true)) {
                    tutorialLayout.setVisibility(View.VISIBLE);
                    PreferenceUtil.saveBooleanToPreference(getActivity(), Config.PRODUCTS_TUTORIAL_VISIBLE, false);
                } else {
                    tutorialLayout.setVisibility(View.GONE);
                }

                bottomLayout.setVisibility(View.VISIBLE);
                if (productsFiltered.size() >= 12) {
                    backToTop.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String error) {
                productsLoadingPage.setVisibility(View.GONE);
                noProudctsLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });


        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0, 0);
                rv.smoothScrollToPosition(0);
            }
        });

    }


    public void performSearch(){
        String item1 = "";
        String item2 = "";
        item2 = edtText_search.getText().toString();
        String item3 = "";
        filter(item1, item2, item3);
    }


    private void filter(String item1, String item2, String item3){

        // New:  dateAdded = "2016-05-20 12:05:43"
        // Popularity:  popularity="8.00", viewed="53", rating=0, reviews.reviewTotal="0", reward=null
        // Ending soon: dateAvailable= "2016-05-20 12:05:43";
        // SavingHighToLow: TotalTaskAmount="8.00"

        for (int i = 0; i < brands.size(); i ++) {
            String item = (String) this.brands.get(i);
            boolean isSelected = ((Boolean) this.selected.get(item)).booleanValue();
            if (isSelected) {
                item3 = item3 + " ," + brands.get(i);
            }
        }

        ((ProductsFragment) this).doFilte(item1,item2, item3);
    }



    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    if(filters_layout.getVisibility() == View.VISIBLE){
                        content_lay.setVisibility(View.VISIBLE);
                        filters_layout_child.startAnimation(animHide);
                        filters_layout.setVisibility(View.GONE);
                        filters_layout_child.setVisibility(View.GONE);
                    }else{
                        getActivity().finish();
                    }
                    return true;
                }
                return false;
            }
        });
    }


    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    public void doFilte(String item1, String item2, String item3) {

        // sorting item1
        if ( !item1.equals("") ) {
            this.sortProductsByiTem1(item1);
        }

        productsFiltered.clear();
        productsFiltered.addAll(products);
        // Filter by brands
        for (Product item : products) {

            if (item3.equals("")) break;

            if (item.getManufacturer() != null) {
                if (item3.toUpperCase().contains(item.getManufacturer().toUpperCase())) {

                } else {
                    productsFiltered.remove(item);
                }
            } else {
                productsFiltered.remove(item);
            }
        }

        // sorting item3
        adapter.notifyDataSetChanged();
    }

    private void sortProductsByiTem1(final String item1) {
        Comparator<Product> comparator = new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {

                switch (Integer.valueOf(item1)) {
                    case 1:  // New
                        return lhs.getDateAdded().compareTo(rhs.getDateAdded());

                    case 2:   // Propularity
//                        return lhs.getPopularity().compareTo(rhs.getPopularity());
                        return (int)((Float.valueOf(lhs.getPopularity()) - Float.valueOf(rhs.getPopularity())) * 100);
                    case 3: //  Ending soon
                     case 4:  // Price low to high
                        return (int)((Float.valueOf(lhs.getTotalTaskAmount()) - Float.valueOf(rhs.getTotalTaskAmount())) * 100);

                    default:
                        break;
                }
                return 0;

            }
        };

        Collections.sort(products, comparator);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 513){
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.getInstance().invalidateOptionsMenu();
    }
}
