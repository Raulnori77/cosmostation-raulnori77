package wannabit.io.cosmostaion.fragment.chains.ok;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.chains.ok.OKUnbondingActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.model.type.Coin;
import wannabit.io.cosmostaion.utils.WDp;

import static wannabit.io.cosmostaion.base.BaseChain.OK_TEST;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_OK_TEST;

public class OKUnbondingFragment0 extends BaseFragment implements View.OnClickListener {

    private Button      mCancel, mNextBtn;
    private EditText    mAmountInput;
    private TextView    mAvailableAmount;
    private TextView    mAvailableDenom;
    private ImageView   mClearAll;
    private Button      mAdd01, mAdd1, mAdd10, mAdd100, mAddHalf, mAddMax;
    private BigDecimal  mMaxAvailable = BigDecimal.ZERO;

    private int                 mDpDecimal = 18;
    private String              mDecimalChecker, mDecimalSetter,
                                mDecimalDivider2, mDecimalDivider1;

    public static OKUnbondingFragment0 newInstance(Bundle bundle) {
        OKUnbondingFragment0 fragment = new OKUnbondingFragment0();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stake_withdraw_0, container, false);
        mCancel = rootView.findViewById(R.id.btn_cancel);
        mNextBtn = rootView.findViewById(R.id.btn_next);
        mAmountInput = rootView.findViewById(R.id.et_amount_coin);
        mAvailableAmount = rootView.findViewById(R.id.tv_max_coin);
        mAvailableDenom = rootView.findViewById(R.id.tv_symbol_coin);
        mClearAll = rootView.findViewById(R.id.clearAll);
        mAdd01 = rootView.findViewById(R.id.btn_add_01);
        mAdd1 = rootView.findViewById(R.id.btn_add_1);
        mAdd10 = rootView.findViewById(R.id.btn_add_10);
        mAdd100 = rootView.findViewById(R.id.btn_add_100);
        mAddHalf = rootView.findViewById(R.id.btn_add_half);
        mAddMax = rootView.findViewById(R.id.btn_add_all);
        mCancel.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mClearAll.setOnClickListener(this);
        mAdd01.setOnClickListener(this);
        mAdd1.setOnClickListener(this);
        mAdd10.setOnClickListener(this);
        mAdd100.setOnClickListener(this);
        mAddHalf.setOnClickListener(this);
        mAddMax.setOnClickListener(this);

        mAmountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable et) {
                String es = et.toString().trim();
                if(TextUtils.isEmpty(es)) {
                    mAmountInput.setBackground(getResources().getDrawable(R.drawable.edittext_box));
                } else if (es.startsWith(".")) {
                    mAmountInput.setBackground(getResources().getDrawable(R.drawable.edittext_box));
                    mAmountInput.setText("");
                } else if (es.endsWith(".")) {
                    mAmountInput.setBackground(getResources().getDrawable(R.drawable.edittext_box_error));
                    mAmountInput.setVisibility(View.VISIBLE);
                } else if(es.length() > 1 && es.startsWith("0") && !es.startsWith("0.")) {
                    mAmountInput.setText("0");
                    mAmountInput.setSelection(1);
                }

                if (es.equals(mDecimalChecker)) {
                    mAmountInput.setText(mDecimalSetter);
                    mAmountInput.setSelection(mDpDecimal + 1);

                } else {
                    try {
                        final BigDecimal inputAmount = new BigDecimal(es);
                        if (BigDecimal.ZERO.compareTo(inputAmount) >= 0 ){
                            mAmountInput.setBackground(getResources().getDrawable(R.drawable.edittext_box_error));
                            return;
                        }

                        BigDecimal checkPosition = inputAmount.movePointRight(mDpDecimal);
                        BigDecimal checkMax = checkPosition.setScale(0, RoundingMode.DOWN);
                        if (checkPosition.compareTo(checkMax) != 0) {
                            String recover = es.substring(0, es.length() - 1);
                            mAmountInput.setText(recover);
                            mAmountInput.setSelection(recover.length());
                            return;
                        }
                        if (getSActivity().mBaseChain.equals(OK_TEST)) {
                            if (inputAmount.compareTo(mMaxAvailable) > 0) {
                                mAmountInput.setBackground(getResources().getDrawable(R.drawable.edittext_box_error));
                            } else {
                                mAmountInput.setBackground(getResources().getDrawable(R.drawable.edittext_box));
                            }
                        }

                    } catch (Exception e) {}
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mAvailableDenom);
        if (getSActivity().mBaseChain.equals(OK_TEST)) {
            mMaxAvailable = WDp.getOkDepositCoin(getBaseDao().mOkStaking);
            mAvailableAmount.setText(WDp.getDpAmount2(getContext(), mMaxAvailable, 0, mDpDecimal));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mCancel)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mNextBtn)) {
            if(isValidateWithdrawAmount()) {
                getSActivity().onNextStep();
            } else {
                Toast.makeText(getContext(), R.string.error_invalid_amounts, Toast.LENGTH_SHORT).show();
            }

        } else if (v.equals(mAdd01)) {
            BigDecimal existed = BigDecimal.ZERO;
            String es = mAmountInput.getText().toString().trim();
            if(es.length() > 0) {
                existed = new BigDecimal(es);
            }
            mAmountInput.setText(existed.add(new BigDecimal("0.1")).toPlainString());

        } else if (v.equals(mAdd1)) {
            BigDecimal existed = BigDecimal.ZERO;
            String es = mAmountInput.getText().toString().trim();
            if(es.length() > 0) {
                existed = new BigDecimal(es);
            }
            mAmountInput.setText(existed.add(new BigDecimal("1")).toPlainString());

        } else if (v.equals(mAdd10)) {
            BigDecimal existed = BigDecimal.ZERO;
            String es = mAmountInput.getText().toString().trim();
            if(es.length() > 0) {
                existed = new BigDecimal(es);
            }
            mAmountInput.setText(existed.add(new BigDecimal("10")).toPlainString());

        } else if (v.equals(mAdd100)) {
            BigDecimal existed = BigDecimal.ZERO;
            String es = mAmountInput.getText().toString().trim();
            if(es.length() > 0) {
                existed = new BigDecimal(es);
            }
            mAmountInput.setText(existed.add(new BigDecimal("100")).toPlainString());

        } else if (v.equals(mAddHalf)) {
            if (getSActivity().mBaseChain.equals(OK_TEST)) {
                mAmountInput.setText(mMaxAvailable.divide(new BigDecimal("2"), 8, RoundingMode.DOWN).toPlainString());
            }

        } else if (v.equals(mAddMax)) {
            if (getSActivity().mBaseChain.equals(OK_TEST)) {
                mAmountInput.setText(mMaxAvailable.toPlainString());
            }

        } else if (v.equals(mClearAll)) {
            mAmountInput.setText("");

        }

    }

    private boolean isValidateWithdrawAmount() {
        try {
            if (getSActivity().mBaseChain.equals(OK_TEST)) {
                BigDecimal depositTemp = new BigDecimal(mAmountInput.getText().toString().trim());
                if (depositTemp.compareTo(BigDecimal.ZERO) <= 0) return false;
                if (depositTemp.compareTo(mMaxAvailable) > 0) return false;
                Coin token = new Coin(TOKEN_OK_TEST, depositTemp.setScale(mDpDecimal).toPlainString());
                getSActivity().mToWithdrawCoin = token;
                return true;
            }
            return false;

        } catch (Exception e) {
            getSActivity().mToWithdrawCoin = null;
            return false;
        }

    }

    private void setDpDecimals(int decimals) {
        mDecimalChecker = "0.";
        mDecimalSetter = "0.";
        mDecimalDivider2 = "2";
        mDecimalDivider1 = "1";
        for (int i = 0; i < decimals; i ++) {
            mDecimalChecker = mDecimalChecker+"0";
            mDecimalDivider2 = mDecimalDivider2 + "0";
            mDecimalDivider1 = mDecimalDivider1 + "0";
        }
        for (int i = 0; i < decimals-1; i ++) {
            mDecimalSetter = mDecimalSetter+"0";
        }
    }

    private OKUnbondingActivity getSActivity() {
        return (OKUnbondingActivity)getBaseActivity();
    }
}