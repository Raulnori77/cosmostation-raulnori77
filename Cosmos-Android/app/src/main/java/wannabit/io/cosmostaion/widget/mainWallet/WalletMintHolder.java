package wannabit.io.cosmostaion.widget.mainWallet;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import org.jetbrains.annotations.NotNull;



import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.MainActivity;
import wannabit.io.cosmostaion.base.BaseChain;
import wannabit.io.cosmostaion.dao.ChainParam;
import wannabit.io.cosmostaion.dialog.Dialog_Help_Msg;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WLog;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class WalletMintHolder extends BaseHolder {
    public CardView     mAprCard;
    public TextView     mInflation, mAPR;

    public WalletMintHolder(@NonNull View itemView) {
        super(itemView);
        mAprCard            = itemView.findViewById(R.id.apr_card);
        mInflation          = itemView.findViewById(R.id.mint_inflation);
        mAPR                = itemView.findViewById(R.id.mint_apr);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final ChainParam.Params param = mainActivity.getBaseDao().mChainParam;
        final BaseChain baseChain = mainActivity.mBaseChain;
        if (param == null || param.mMintParams == null || param.mMintParams.mint_denom == null) { return; }
//        WLog.w("param.Inflation(baseChain) " + param.Inflation(baseChain));
        mInflation.setText(WDp.getPercentDp(param.Inflation(baseChain)));
        mAPR.setText(WDp.getDpEstApr(mainActivity.getBaseDao(), mainActivity.mBaseChain));


        mAprCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", mainActivity.getString(R.string.str_apr_help_title));
                bundle.putString("msg", mainActivity.getString(R.string.str_apr_help_msg));
                Dialog_Help_Msg dialog = Dialog_Help_Msg.newInstance(bundle);
                dialog.setCancelable(true);
                mainActivity.getSupportFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();
            }
        });
    }
}
