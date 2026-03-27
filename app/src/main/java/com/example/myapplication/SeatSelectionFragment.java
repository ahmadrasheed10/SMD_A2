package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SeatSelectionFragment extends Fragment {

    private final int PRICE = 500;

    private ArrayList<String> selectedSeats = new ArrayList<>();
    private ArrayList<String> bookedSeats = new ArrayList<>();

    TextView txtSelected;
    Button btnSnacks, btnBook;

    String movie;
    boolean isComingSoon;
    String trailerUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);

        if (getArguments() != null) {
            movie = getArguments().getString("movie");
            isComingSoon = getArguments().getBoolean("isComingSoon", false);
            trailerUrl = getArguments().getString("trailerUrl");
        }

        bookedSeats.add("A2");
        bookedSeats.add("B3");
        bookedSeats.add("C4");
        bookedSeats.add("D1");

        txtSelected = view.findViewById(R.id.txtSelected);
        btnSnacks = view.findViewById(R.id.btnSnacks);
        btnBook = view.findViewById(R.id.btnBook);
        TextView txtMovieName = view.findViewById(R.id.txtMovieName);
        ImageButton btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnBook.setEnabled(false);
        btnSnacks.setEnabled(false);

        if (movie != null)
            txtMovieName.setText(movie);

        int[] seats = {
                R.id.seat1, R.id.seat2, R.id.seat3, R.id.seat4, R.id.seat5, R.id.seat6,
                R.id.seat7, R.id.seat8, R.id.seat9, R.id.seat10, R.id.seat11, R.id.seat12,
                R.id.seat13, R.id.seat14, R.id.seat15, R.id.seat16, R.id.seat17, R.id.seat18,
                R.id.seat19, R.id.seat20, R.id.seat21, R.id.seat22, R.id.seat23, R.id.seat24,
                R.id.seat25, R.id.seat26, R.id.seat27, R.id.seat28, R.id.seat29, R.id.seat30,
                R.id.seat31, R.id.seat32, R.id.seat33, R.id.seat34, R.id.seat35, R.id.seat36
        };

        if (isComingSoon) {
            btnBook.setText("Coming Soon");
            btnBook.setEnabled(false);
            btnBook.setClickable(false);

            btnSnacks.setText("Watch Trailer");
            btnSnacks.setEnabled(true);
            btnSnacks.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.seat_selected));
            btnSnacks.setOnClickListener(v -> {
                if (trailerUrl != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(trailerUrl));
                    startActivity(intent);
                }
            });
        } else {
            btnBook.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToTicketSummary(
                            movie, selectedSeats, selectedSeats.size() * PRICE, 0);
                }
            });

            btnSnacks.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToSnacks(
                            movie, selectedSeats, selectedSeats.size() * PRICE, selectedSeats.size());
                }
            });
        }

        for (int i = 0; i < seats.length; i++) {
            Button seat = view.findViewById(seats[i]);
            String label = "" + (char)('A' + (i / 6)) + ((i % 6) + 1);
            seat.setText(label);

            if (isComingSoon) {
                seat.setEnabled(false);
                seat.setClickable(false);
            } else {
                if (bookedSeats.contains(label)) {
                    seat.setEnabled(false);
                    seat.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.seat_booked));
                } else {
                    seat.setOnClickListener(v -> toggleSeat(seat, label));
                }
            }
        }

        return view;
    }

    private void toggleSeat(Button seat, String label) {

        if (seat.isSelected()) {

            seat.setSelected(false);
            seat.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_available));

            selectedSeats.remove(label);

        } else {

            seat.setSelected(true);
            seat.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_selected));

            selectedSeats.add(label);
        }

        txtSelected.setText("Selected Seats: " + selectedSeats.size());

        updateButtons();
    }

    private void updateButtons() {

        if (!selectedSeats.isEmpty()) {

            btnBook.setEnabled(true);
            btnSnacks.setEnabled(true);

            btnBook.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_booked));

            btnSnacks.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_selected));

        }
        else {

            btnBook.setEnabled(false);
            btnSnacks.setEnabled(false);

            btnBook.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray));

            btnSnacks.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray));
        }
    }
}
