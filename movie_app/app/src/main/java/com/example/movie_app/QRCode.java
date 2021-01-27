package com.example.movie_app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;


import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.grpc.Attributes;

import java.util.List;


public class QRCode extends Fragment {
    public static final String EXTRA_MESSAGE = "com.example.movie_app.extra.MESSAGE";
    Button scan_btn;
    CameraView cameraView;
    boolean isDetected = false;
    BarcodeScanner scanner;
    BarcodeScannerOptions options;
    String urlQRCode=null;
    View view;
    Intent intent;



    public QRCode() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_q_r_code, container, false);


        Dexter.withActivity(getActivity())
                .withPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO})
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        setUpCamera();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();


        return view;

    }

    private void setUpCamera() {

        cameraView = (CameraView) view.findViewById(R.id.cameraView);
        cameraView.setLifecycleOwner(this);
        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                InputImage image = InputImage.fromBitmap(frame2bitMap(frame), frame.getRotationToView());
                processImage(image);
            }
        });
        options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC
                ).build();
        scanner = BarcodeScanning.getClient();
    }


    private void processImage(InputImage image) {
        if(!isDetected){
            Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            for(Barcode barcode: barcodes){
                                Rect bounds = barcode.getBoundingBox();
                                Point[] corners = barcode.getCornerPoints();

                                String rawValue = barcode.getRawValue();

                                int valueType = barcode.getValueType();
                                // See API reference for complete list of supported types
                                switch (valueType) {
                                    case Barcode.TYPE_URL:
                                        urlQRCode = barcode.getUrl().getUrl();
                                        break;
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            if(!urlQRCode.isEmpty()){
                System.out.println("################################# URLLLLLLLLLL--> "+urlQRCode);
                String[] idYoutube = urlQRCode.split("/");
                System.out.println("IDDDDDDDDDDD "+idYoutube[3]);
                // System.out.println(idYoutube[2]);
                intent = new Intent(getContext(), PlayVideo.class);
                intent.putExtra(EXTRA_MESSAGE, idYoutube[3]);
                QRCode.this.startActivity(intent);
                getActivity().finish();

            }

        }
    }
    private Bitmap frame2bitMap(Frame frame) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuvImage = new YuvImage(frame.getData(), ImageFormat.NV21, frame.getSize().getWidth(), frame.getSize().getHeight(), null);
        yuvImage.compressToJpeg(new Rect(0, 0, frame.getSize().getWidth(), frame.getSize().getHeight()), 90, out);
        byte[] imageBytes = out.toByteArray();
        Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return image;
    }



}
