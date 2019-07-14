package com.zohar.criminalintent;

import android.content.Intent;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CrimeListFragment extends Fragment {

    private List<Crime> mCrimes;
    private CrimeAdapter mCrimeAdapter;
    private RecyclerView mCrimeRecyclerView;

    private final int REQUEST_CRIME = 1;
    private Crime mCrime;

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


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getContext());
        mCrimes = crimeLab.getCrimes();

        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(mCrimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        }else{
            mCrimeAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView mCrimeTitleTextView;
         TextView mCrimeDate;
         ImageView mSolvedImageView;
         View view;

         private Crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            mCrimeDate = itemView.findViewById(R.id.crime_date);
            mCrimeTitleTextView = itemView.findViewById(R.id.crime_tile);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);

            itemView.setOnClickListener(this);
        }

        private void bind(Crime crime){
            mCrime = crime;
            mCrimeTitleTextView.setText(crime.getTitle());
            Date date = crime.getDate();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.CHINA);
            mCrimeDate.setText(format.format(date));
            mSolvedImageView.setVisibility(mCrime.isSolved()? View.VISIBLE : View.GONE);
        }


        @Override
        public void onClick(View v) {
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CRIME){

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
            mCrime = mCrimes.get(i);
            viewHolder.bind(mCrime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
