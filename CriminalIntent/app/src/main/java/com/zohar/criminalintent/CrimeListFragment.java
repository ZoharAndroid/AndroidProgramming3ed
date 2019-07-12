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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView mCrimeTitleTextView;
         TextView mCrimeDate;
         ImageView mSolvedImageView;

         private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);
            mCrimeDate = itemView.findViewById(R.id.crime_date);
            mCrimeTitleTextView = itemView.findViewById(R.id.crime_tile);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);

            itemView.setOnClickListener(this);
        }

        private void bind(Crime crime){
            mCrime = crime;
            mCrimeTitleTextView.setText(crime.getTitle());
            mCrimeDate.setText(crime.getDate().toString());
            mSolvedImageView.setVisibility(mCrime.isSolved()? View.VISIBLE : View.GONE);
        }


        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), mCrime.getTitle() + " click!" , Toast.LENGTH_SHORT).show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            //getItemViewType(i)
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_crime,viewGroup, false);
            CrimeHolder holder = new CrimeHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CrimeHolder viewHolder, int i) {
            Crime crime = mCrimes.get(i);
            viewHolder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}