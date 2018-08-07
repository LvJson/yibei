package com.ts.lys.yibei.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.BrokerCheckListBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单 - 持仓
 */
public class AccountChooseAdapter extends RecyclerView.Adapter<AccountChooseAdapter.ViewHolder> {


    private Context mContext;
    List<BrokerCheckListBean.DataBean.BrokerBean> accountBeanList;


    private OnItemClickListener listener;

    public AccountChooseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<BrokerCheckListBean.DataBean.BrokerBean> accountBeanList) {

        this.accountBeanList = accountBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.adapter_account_choose_layout, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            final BrokerCheckListBean.DataBean.BrokerBean ab = accountBeanList.get(position);

            if (ab.getNow())
                holder.ivCheckState.setVisibility(View.VISIBLE);
            else
                holder.ivCheckState.setVisibility(View.GONE);

            if (ab.getName().equals(mContext.getString(R.string.demo_account))) {
                holder.ivIcon.setImageResource(R.mipmap.demo_icon);
                if (ab.getStatus() == 0)
                    holder.tvExplain.setText(mContext.getString(R.string.have_maturity));
                else
                    holder.tvExplain.setText(mContext.getString(R.string.maturity) + "：" + ab.getDestory());
            } else {
                holder.ivIcon.setImageResource(R.mipmap.simple_mmig_icon);
                holder.tvExplain.setText(mContext.getString(R.string.account_id) + "：" + ab.getAccount());
            }

            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(ab);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return accountBeanList == null ? 0 : accountBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_explain)
        TextView tvExplain;
        @Bind(R.id.iv_check_state)
        ImageView ivCheckState;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            AutoUtils.autoSize(itemView);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(BrokerCheckListBean.DataBean.BrokerBean ab);

    }
}
