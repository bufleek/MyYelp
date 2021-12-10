package com.myyelp.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myyelp.R;
import com.myyelp.databinding.FragmentFavoritesBinding;
import com.myyelp.ui.BusinessesAdapter;
import com.myyelp.ui.MainActivity;
import com.myyelp.ui.MainViewModel;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private FrameLayout states;
    private RecyclerView recyclerview;
    private BusinessesAdapter adapter;
    private MainViewModel mainViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        states = binding.frameStatesFavorites;
        recyclerview = binding.rvFavorites;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = ((MainActivity) requireActivity()).mainViewModel;
        adapter = new BusinessesAdapter(mainViewModel, true);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

        mainViewModel.mainRepo.favourites.observe(getViewLifecycleOwner(), businesses -> {
            states.removeAllViews();
            if (businesses.size() == 0) {
                View errorView = View.inflate(requireContext(), R.layout.item_error, null);
                errorView.findViewById(R.id.btn_retry).setVisibility(View.GONE);
                ((TextView) errorView.findViewById(R.id.tv_error)).setText("No favourites saved yet");
                states.addView(errorView);
            }
            adapter.updateData(new ArrayList<>(businesses));
        });
        mainViewModel.getFavourites();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}