package vn.hiep.demopdf417.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import vn.hiep.demopdf417.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA_PERMISSION = 1000;
    private ZXingScannerView mScannerView;
    private Button btnRequirePermission;

    private OnFragmentInteractionListener mListener;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScanFragment.
     */
    public static ScanFragment newInstance() {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        btnRequirePermission = view.findViewById(R.id.btnRequireCameraPermission);
        btnRequirePermission.setVisibility(View.GONE);
        btnRequirePermission.setOnClickListener(v -> requestCameraPermission());

        ViewGroup contentFrame = view.findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(getActivity());
        contentFrame.addView(mScannerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!checkCameraPermission()) {
            requestCameraPermission();
        }
    }

    public void onReceivedScanResult(String result) {
        if (mListener != null) {
            mListener.onFragmentInteraction(result);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        startScan();
    }

    public void startScan() {
        mScannerView.setResultHandler(this);
        // setAspectTolerance 0.01f is important setting so that Amazon devices can recognize QR code
        // default 0.1f does not let those devices works
        mScannerView.setAspectTolerance(0.1f);
        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.PDF_417));
        mScannerView.startCamera();
    }

    public void stopScan() {
        mScannerView.stopCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScan();
    }

    @Override
    public void handleResult(Result rawResult) {
        showDialog(rawResult.getText());
        onReceivedScanResult(rawResult.getText());
    }

    private void showDialog(String message) {
        Activity activity = getActivity();
        if (activity == null) return;
        new AlertDialog.Builder(activity)
                .setTitle(R.string.title_scan_result)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    stopScan();
                    startScan();
                })
                .setCancelable(false)
                .setIcon(R.drawable.ic_camera_black_24dp)
                .show();
    }

    private boolean checkCameraPermission() {
        Activity activity = getActivity();
        if (activity == null) return false;
        return (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermission() {
        Activity activity = getActivity();
        if (activity == null) return;
        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode != REQUEST_CAMERA_PERMISSION) {
            return;
        }
        if (grantResults.length > 0) {
            boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraAccepted) {
                btnRequirePermission.setVisibility(View.GONE);
                mScannerView.startCamera();
            } else {
                btnRequirePermission.setVisibility(View.VISIBLE);
            }
        }
    }
}
