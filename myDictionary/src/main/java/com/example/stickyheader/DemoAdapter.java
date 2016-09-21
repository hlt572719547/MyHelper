package com.example.stickyheader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mydictionary.R;

/**
 * Created by Administrator on 2016/6/3.
 */
public class DemoAdapter extends SectioningAdapter {

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView titleTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        }
    }

    Context context;
    DemoModel[] demos;
    ItemClickListener itemClickListener;

    public DemoAdapter(Context context, DemoModel[] demos, ItemClickListener itemClickListener) {
        this.context = context;
        this.demos = demos;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getNumberOfSections() {
        return 1;
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return demos.length;
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public SectioningAdapter.HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_demo_header, parent, false);
        return new HeaderViewHolder(v);
    }

    @Override
    public SectioningAdapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_demo_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex) {
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        hvh.titleTextView.setText(context.getString(R.string.main_demo_list_title));
    }

    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex) {
        ItemViewHolder ivh = (ItemViewHolder) viewHolder;

        final DemoModel dm = demos[itemIndex];
        ivh.titleTextView.setText(dm.title);
        ivh.descriptionTextView.setText(dm.description);

        ivh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(dm);
            }
        });
    }

    public interface ItemClickListener {
        void onItemClick(DemoModel demoModel);
    }
}
