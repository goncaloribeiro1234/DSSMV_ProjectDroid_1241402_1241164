package com.example.lojadevideojogos.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lojadevideojogos.BuildConfig;
import com.example.lojadevideojogos.R;
import com.example.lojadevideojogos.databinding.ActivityWelcomeBinding;
import com.example.lojadevideojogos.util.SessionManager;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Clique no Botão Cliente
        binding.btnClient.setOnClickListener(v -> {
            SessionManager.getInstance().setProfile(SessionManager.Profile.CLIENTE);
            openStoreActivity();
        });

        // Clique no Botão Gestor
        binding.btnManager.setOnClickListener(v -> showPinDialog());
    }

    private void showPinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_pin_title));
        builder.setMessage(getString(R.string.dialog_pin_message));

        // Cria o campo de texto para a Password
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        // Botão de Confirmar
        builder.setPositiveButton(getString(R.string.dialog_confirm), (dialog, which) -> {
            String enteredPin = input.getText().toString();

            // Valida com o PIN guardado no BuildConfig (ex: "1234")
            if (enteredPin.equals(BuildConfig.ADMIN_PIN)) {
                SessionManager.getInstance().setProfile(SessionManager.Profile.GESTOR);
                openStoreActivity();
            } else {
                Toast.makeText(this, getString(R.string.error_wrong_pin), Toast.LENGTH_SHORT).show();
            }
        });

        // Botão de Cancelar
        builder.setNegativeButton(getString(R.string.dialog_cancel), (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void openStoreActivity() {
        // Iremos criar a StoreActivity na próxima fase. Por agora, podes apontar para a MainActivity de testes
        // Intent intent = new Intent(this, StoreActivity.class);
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
        finish();// Fecha o WelcomeActivity para o botão "Voltar" do telemóvel não voltar aqui
    }
}