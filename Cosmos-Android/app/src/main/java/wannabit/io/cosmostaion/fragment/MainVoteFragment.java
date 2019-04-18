package wannabit.io.cosmostaion.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.MainActivity;
import wannabit.io.cosmostaion.activities.WebActivity;
import wannabit.io.cosmostaion.base.BaseChain;
import wannabit.io.cosmostaion.base.BaseConstant;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.model.type.Proposal;
import wannabit.io.cosmostaion.task.FetchTask.AllProposalTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WLog;

public class MainVoteFragment extends BaseFragment implements TaskListener {

    private SwipeRefreshLayout      mSwipeRefreshLayout;
    private RecyclerView            mRecyclerView;
    private TextView                mEmptyProposal;
    private VoteAdapter             mVoteAdapter;

    private ArrayList<Proposal>     mProposals = new ArrayList<>();

    public static MainVoteFragment newInstance(Bundle bundle) {
        MainVoteFragment fragment = new MainVoteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_vote, container, false);
        mSwipeRefreshLayout     = rootView.findViewById(R.id.layer_refresher);
        mRecyclerView           = rootView.findViewById(R.id.recycler);
        mEmptyProposal           = rootView.findViewById(R.id.empty_proposal);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onFetchProposals();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mVoteAdapter = new VoteAdapter();
        mRecyclerView.setAdapter(mVoteAdapter);
        onFetchProposals();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_accounts :
                getMainActivity().onShowTopAccountsView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefreshTab() {
        if(!isAdded()) return;
        onFetchProposals();
    }

    private void onFetchProposals() {
        if(getMainActivity() == null || getMainActivity().mAccount == null) return;
        WLog.w("onFetchProposals : " + getMainActivity().mAccount.address);
        WLog.w("onFetchProposals : " + getMainActivity().mAccount.baseChain);
        new AllProposalTask(getBaseApplication(), this, BaseChain.getChain(getMainActivity().mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onTaskResponse(TaskResult result) {
        if(!isAdded()) return;
        if (result.taskType == BaseConstant.TASK_FETCH_ALL_PROPOSAL) {
            if(result.isSuccess) {
                ArrayList<Proposal> temp = (ArrayList<Proposal>)result.resultData;
                if(temp != null && temp.size() > 0) {
                    mProposals = temp;
                    onSortingProposal(mProposals);
                    mVoteAdapter.notifyDataSetChanged();
                    mEmptyProposal.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mEmptyProposal.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            } else {
                mEmptyProposal.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }

        }
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.VoteHolder> {
        @NonNull
        @Override
        public VoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new VoteHolder(getLayoutInflater().inflate(R.layout.item_proposal, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VoteHolder voteHolder, int position) {
            final Proposal proposal = mProposals.get(position);
            voteHolder.proposal_id.setText("#" + proposal.value.proposal_id);
            voteHolder.proposal_status.setText(proposal.value.proposal_status);
            voteHolder.proposal_title.setText(proposal.value.title);
            voteHolder.proposal_details.setText(proposal.value.description);
            if (proposal.value.proposal_status.equals("DepositPeriod")) {
                voteHolder.proposal_status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_deposit_img));
            } else if (proposal.value.proposal_status.equals("VotingPeriod")) {
                voteHolder.proposal_status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_voting_img));
            } else if (proposal.value.proposal_status.equals("Rejected")) {
                voteHolder.proposal_status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_rejected_img));
            } else if (proposal.value.proposal_status.equals("Passed")) {
                voteHolder.proposal_status_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_passed_img));
            } else {
                voteHolder.proposal_status_img.setVisibility(View.GONE);
            }

            voteHolder.card_proposal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webintent = new Intent(getBaseActivity(), WebActivity.class);
                    webintent.putExtra("voteId", proposal.value.proposal_id);
                    startActivity(webintent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mProposals.size();
        }

        public class VoteHolder extends RecyclerView.ViewHolder {
            private CardView card_proposal;
            private TextView proposal_id, proposal_status, proposal_title, proposal_details;
            private ImageView proposal_status_img;

            public VoteHolder(@NonNull View itemView) {
                super(itemView);
                card_proposal               = itemView.findViewById(R.id.card_proposal);
                proposal_id                 = itemView.findViewById(R.id.proposal_id);
                proposal_status             = itemView.findViewById(R.id.proposal_status);
                proposal_title              = itemView.findViewById(R.id.proposal_title);
                proposal_details            = itemView.findViewById(R.id.proposal_details);
                proposal_status_img         = itemView.findViewById(R.id.proposal_status_img);

            }
        }
    }

    public MainActivity getMainActivity() {
        return (MainActivity)getBaseActivity();
    }


    public void onSortingProposal(ArrayList<Proposal> proposals) {
        Collections.sort(proposals, new Comparator<Proposal>() {
            @Override
            public int compare(Proposal o1, Proposal o2) {
                if (Integer.parseInt(o1.value.proposal_id) < Integer.parseInt(o2.value.proposal_id)) return 1;
                else if (Integer.parseInt(o1.value.proposal_id) > Integer.parseInt(o2.value.proposal_id)) return -1;
                else return 0;

            }
        });
    }
}