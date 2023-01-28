package my.virkato.bestschools;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SchoolsListViewAdapter extends BaseAdapter {
    List<SchoolItem> data = new ArrayList<>();

    public void updateDataSet(List<SchoolItem> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SchoolItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, null);
        }
        TextView t_name = convertView.findViewById(R.id.t_name);
        TextView t_votes = convertView.findViewById(R.id.t_votes);

        t_name.setText(getItem(position).name);
        t_votes.setText(String.valueOf(getItem(position).votes));

        LinearLayout l_item = convertView.findViewById(R.id.l_item);
        if (position % 2 == 0) {
            l_item.setBackgroundColor(0x30AAAAAA);
        } else {
            l_item.setBackgroundColor(0x30EEEEEE);
        }

        return convertView;
    }
}
