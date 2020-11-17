
package com.example.payeat;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;

        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.Arrays;
        import java.util.List;

public class MenuByTitleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_by_title);

        DishAdapter adapter = new DishAdapter(this, R.layout.menu_item,
                Arrays.asList("סלט חלומי","סלט יווני","סלט ירוק", "סלט טוסט"));
        ListView DishListView = findViewById(R.id.category_menu_list);
        DishListView.setAdapter(adapter);
        DishListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "onItemClick - " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();

       // Intent intent = new Intent(this, MenuByTitleActivity.class); //Activity Dish
        //intent.putExtra(getResources().getString(R.string.intent_extras_menu_id),i); // TODO change 'i' to the real id according to database.
        //startActivity(intent);
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, parent, false);
            }
            TextView title = convertView.findViewById(R.id.dish_name_text);
            title.setText(list.get(position));

            return convertView;
        }
    }

}