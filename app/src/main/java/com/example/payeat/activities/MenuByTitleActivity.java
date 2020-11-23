
package com.example.payeat.activities;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;

        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.payeat.fragments.DishDetailsFragment;
        import com.example.payeat.R;
        import com.example.payeat.fragments.OrderDishFragment;

        import java.util.Arrays;
        import java.util.List;

public class MenuByTitleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener ,View.OnClickListener {
    private DishDetailsFragment fragment1;
    private boolean mode_manager;
    private Button goToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_by_title);
        DishAdapter adapter = new DishAdapter(this, R.layout.activity_menu_by_title_list_item,
                Arrays.asList("קרמבו", "קרמבו", "קרמבו", "קרמבו"));
        ListView DishListView = findViewById(R.id.category_menu_list);
        DishListView.setAdapter(adapter);
        DishListView.setOnItemClickListener(this);
        mode_manager = getIntent().getBooleanExtra("mode manager", false);
        goToCart = (Button) findViewById(R.id.go_to_my_cart_button);

        //Setting listeners to button
        goToCart.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Intent intent = null;
//        switch (view.getId()) {
//            case R.id.expand_dish_button:
//                fragment = DishDetailsFragment.newInstance((View.OnClickListener) this); //HELP!!
//                fragment.show(getSupportFragmentManager(), "DishDetailsFragment");
//                break;
//        }
//        Toast.makeText(this, "onItemClick - " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();

       // Intent intent = new Intent(this, MenuByTitleActivity.class); //Activity Dish
        //intent.putExtra(getResources().getString(R.string.intent_extras_menu_id),i); // TODO change 'i' to the real id according to database.
        //startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.go_to_my_cart_button)
            Toast.makeText(this, "הולך לעגלה!", Toast.LENGTH_SHORT ).show();


    }

    private class DishAdapter extends ArrayAdapter<String>{
        private List<String> list;
        public DishAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_menu_by_title_list_item, parent, false);
            }
            TextView title = convertView.findViewById(R.id.dish_name_text);
            title.setText(list.get(position));
            Button expandDishButton =  convertView.findViewById(R.id.expand_dish_button);
                expandDishButton.setVisibility(View.VISIBLE);
                expandDishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       fragment1 = DishDetailsFragment.newInstance(mode_manager);
                       fragment1.show(getSupportFragmentManager(), "DishDetailsFragment");
                    }
                });

            return convertView;
        }
    }

}