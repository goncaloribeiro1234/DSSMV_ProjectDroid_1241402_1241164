package com.example.lojadevideojogos.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.example.lojadevideojogos.databinding.ActivityScannerBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScannerActivity extends AppCompatActivity {

    private ActivityScannerBinding binding;
    private ExecutorService cameraExecutor;
    private TextRecognizer textRecognizer;
    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cameraExecutor = Executors.newSingleThreadExecutor();
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        binding.btnCapture.setOnClickListener(v -> {
            String textoLido = binding.tvDetectedText.getText().toString();

            // Limpar o prefixo "Detectado: " se já estiver lá
            if (textoLido.startsWith("Detectado: ")) {
                textoLido = textoLido.replace("Detectado: ", "").trim();
            }

            if (!textoLido.isEmpty() && !textoLido.equals("A apanhar texto da capa...")) {
                // Normalizar para minúsculas para mapear diretamente para os jogos do Mock
                String tLower = textoLido.toLowerCase();
                String jogoFinal;

                if (tLower.contains("gta") || tLower.contains("grand theft auto")) {
                    jogoFinal = "Grand Theft Auto V";
                } else if (tLower.contains("witcher")) {
                    jogoFinal = "The Witcher 3";
                } else if (tLower.contains("cyberpunk")) {
                    jogoFinal = "Cyberpunk 2077";
                } else if (tLower.contains("red dead") || tLower.contains("redemption")) {
                    jogoFinal = "Red Dead Redemption 2";
                } else if (tLower.contains("god of war")) {
                    jogoFinal = "God of War";
                } else {
                    jogoFinal = textoLido;
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("SCANNED_GAME_NAME", jogoFinal);
                setResult(RESULT_OK, resultIntent);
                finish(); // Fecha a câmara e regressa à MainActivity com o jogo validado
            } else {
                Toast.makeText(this, "Ainda não detetou nenhum texto válido na capa!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, this::processImageProxy);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (Exception e) {
                Toast.makeText(this, "Erro ao iniciar câmara: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void processImageProxy(ImageProxy imageProxy) {
        android.media.Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            textRecognizer.process(image)
                    .addOnSuccessListener(visionText -> {
                        String recognizedText = visionText.getText();
                        if (!recognizedText.isEmpty()) {
                            runOnUiThread(() -> binding.tvDetectedText.setText("Detectado: " + recognizedText));
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Ignora falhas menores de frame
                    })
                    .addOnCompleteListener(task -> imageProxy.close());
        } else {
            imageProxy.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissão da câmara negada!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}