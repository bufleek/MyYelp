package com.myyelp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myyelp.R;
import com.myyelp.data.models.Business;
import com.myyelp.databinding.FragmentHomeBinding;
import com.myyelp.ui.BusinessesAdapter;
import com.myyelp.ui.MainActivity;
import com.myyelp.ui.MainViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private MainViewModel mainViewModel;
    private FrameLayout states;
    private BusinessesAdapter adapter;
    private Spinner spinner;
    private int selectedSort = 0;
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = ((MainActivity) requireActivity()).mainViewModel;
        states = view.findViewById(R.id.frame_states_home);
        adapter = new BusinessesAdapter(mainViewModel);
        spinner = view.findViewById(R.id.spinner);

        String[] sortings = {"Rating", "Price"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, sortings);
        spinnerAdapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        searchView = view.findViewById(R.id.searchView);
        //add searchview listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainViewModel.searchBusiness();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mainViewModel.searchTerm = newText;
                return true;
            }
        });

        //change searchview text color
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.onSurface));
        searchEditText.setHintTextColor(getResources().getColor(R.color.onSurface));

        //setup businesses recyclerview
        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.rv_businesses);
        recyclerview.setAdapter(adapter); //setting recyclerview adapter
        recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerview.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));//adding divider to recyclerview

        //observe ui states from Main ViewModel
        //when isSearching is true -> inflate states with loading view
        mainViewModel.mainRepo.isSearchLoading.observe(getViewLifecycleOwner(), isSearchLoading -> {
            states.removeAllViews();
            if (isSearchLoading) {
                states.addView(View.inflate(requireContext(), R.layout.item_loading, null));
            }
        });

        //observe error state
        mainViewModel.mainRepo.searchError.observe(getViewLifecycleOwner(), searchError -> {
            states.removeAllViews();
            if (searchError != null) {
                View errorView = View.inflate(requireContext(), R.layout.item_error, null);
                ((TextView) errorView.findViewById(R.id.tv_error)).setText(searchError);
                ((Button) errorView.findViewById(R.id.btn_retry)).setOnClickListener(it -> {
                    mainViewModel.searchBusiness();
                });
                states.addView(errorView);
            }
        });

        //observe response state and update recyclerview
        mainViewModel.mainRepo.searchResponse.observe(getViewLifecycleOwner(), searchResponse -> {
            updateData();
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSort = position;
                if(mainViewModel.mainRepo.searchResponse.getValue() != null){
                    updateData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mainViewModel.searchBusiness();
    }

    private void updateData() {
        if (mainViewModel.mainRepo.searchResponse.getValue() != null){
            //sort results before updating recyclerview
            ArrayList<Business> sortedBusinesses = mainViewModel.mainRepo.searchResponse.getValue().businesses;
            if (selectedSort == 0){
                Collections.sort(sortedBusinesses, (o1, o2) -> String.valueOf(o1.rating).compareTo(String.valueOf(o2.rating)));
                Collections.reverse(sortedBusinesses);
            }else{
                Collections.sort(sortedBusinesses, (o1, o2) -> o1.price.compareTo(o2.price));
            }
            adapter.updateData(sortedBusinesses);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}