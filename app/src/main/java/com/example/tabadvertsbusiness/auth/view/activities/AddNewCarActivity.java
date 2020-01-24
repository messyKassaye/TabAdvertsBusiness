package com.example.tabadvertsbusiness.auth.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabadvertsbusiness.R;
import com.example.tabadvertsbusiness.auth.model.Category;
import com.example.tabadvertsbusiness.auth.model.Child;
import com.example.tabadvertsbusiness.auth.response.CategoryResponse;
import com.example.tabadvertsbusiness.auth.view_model.CategoryViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AddNewCarActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CategoryViewModel viewModel;
    private LinearLayout car_type_main_container;
    private RadioGroup car_category_layout,car_type_layout;
    private TextView car_type_label;
    private Button registerCar;

    private Category carCategory;
    private Child categoryChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car);

        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Register new car");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white_color));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        car_category_layout = (RadioGroup) findViewById(R.id.car_category_layout);
        car_type_main_container = (LinearLayout)findViewById(R.id.car_type_layout_container);
        car_type_label = (TextView)findViewById(R.id.car_type_label);
        car_type_layout = (RadioGroup) findViewById(R.id.cars_type_layout);
        registerCar = (Button)findViewById(R.id.register_car_button);

        //Fetch list of car categories
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        viewModel.index().observe(this,categoryResponse -> {
            if(categoryResponse!=null){
                List<Category> list = categoryResponse.getData();
                for (int i =0;i<list.size();i++){
                    RadioButton radioButton = new RadioButton(this);
                    Category category = list.get(i);
                    radioButton.setText(category.getName());
                    radioButton.setTag(category);
                    radioButton.setId(category.getId());
                    car_category_layout.addView(radioButton);
                }
            }

            car_category_layout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    View radioButton = car_category_layout.findViewById(i);
                     carCategory = (Category)radioButton.getTag();
                    checkChanged(carCategory);
                }
            });
        });

        registerCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void checkChanged(Category category){
        car_type_layout.removeAllViews();
        car_type_main_container.setVisibility(View.VISIBLE);
        car_type_label.setText("Select what kind of "+category.getName()+" you have");

        for (int i =0;i<category.getChild().size();i++){
            Child child = category.getChild().get(i);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setTag(child);
            radioButton.setId(child.getId());
            radioButton.setText(child.getName());
            car_type_layout.addView(radioButton);
        }

        car_type_layout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = car_type_layout.findViewById(i);
                categoryChild = (Child) radioButton.getTag();
                registerCar.setVisibility(View.VISIBLE);
            }
        });

    }
}
