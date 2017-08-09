package test1.nh.com.demos1.activities.staggered_lo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 2017/6/29.
 */

public class StaggerAdapter  extends RecyclerView.Adapter<StaggerAdapter.StagVH> {

    private List<RvItem> itemList;
    private Context context;

    public StaggerAdapter(Context context, List<RvItem> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public StagVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stagger_list_item, null);
        LinearLayout resizer= (LinearLayout) layoutView.findViewById(R.id.resizer);
        resizer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,itemList.get(viewType).height));
        StagVH rcv = new StagVH(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(StagVH holder, int position) {
        holder.txt.setText(itemList.get(position).getTxt());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }



    static class StagVH extends RecyclerView.ViewHolder{
        public TextView txt;
        public LinearLayout resizer;
        public StagVH(View itemView) {
            super(itemView);
            txt= (TextView) itemView.findViewById(R.id.item_txt);
            resizer= (LinearLayout) itemView.findViewById(R.id.resizer);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
