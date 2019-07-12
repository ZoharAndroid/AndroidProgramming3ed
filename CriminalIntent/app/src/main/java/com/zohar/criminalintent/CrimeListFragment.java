package com.zohar.criminalintent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private List<Crime> mCrimes;
    private CrimeAdapter mCrimeAdapter;
    private RecyclerView mCrimeRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCrimeRecyclerView.setLayoutManager(layoutManager);

        updateUI();

        return view;

    }


    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.getInstance(getContext());
        mCrimes = crimeLab.getCrimes();

        mCrimeAdapter = new CrimeAdapter(mCrimes);
        mCrimeRecyclerView.setAdapter(mCrimeAdapter);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder{

        private TextView mCrimeTitleTextView;
        private TextView mCrimeDate;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);

            mCrimeDate = itemView.findViewById(R.id.crime_date);
            mCrimeTitleTextView = itemView.findViewById(R.id.crime_tile);

        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter{

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_crime,viewGroup, false);
            CrimeHolder holder = new CrimeHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
