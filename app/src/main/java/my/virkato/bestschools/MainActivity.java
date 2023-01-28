package my.virkato.bestschools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private RefreshService refreshService;
    private ListView lv_schools;
    private SchoolsListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.t_top)).setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://habr.com/ru/post/710330"))));

        adapter = new SchoolsListViewAdapter();
        lv_schools = findViewById(R.id.lv_schools);
        lv_schools.setAdapter(adapter);

        RefreshService.OnComplete onComplete = schools -> {
            Log.e("list", "received " + schools.size());
            schools.sort(Comparator.comparingInt((SchoolItem o) -> o.votes).reversed().thenComparing(o -> o.name));
            lv_schools.post(() -> adapter.updateDataSet(schools));
        };
        refreshService = new RefreshService(onComplete);
        refreshService.start();
        Log.e("Main", "Started");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refreshService != null) {
            refreshService.interrupt();
        }
    }
}