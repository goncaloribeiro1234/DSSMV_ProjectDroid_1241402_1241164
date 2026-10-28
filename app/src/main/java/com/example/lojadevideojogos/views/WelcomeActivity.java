package com.example.lojadevideojogos.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lojadevideojogos.BuildConfig;
import com.example.lojadevideojogos.R;
import com.example.lojadevideojogos.controllers.RequestController;
import com.example.lojadevideojogos.controllers.StoreController;
import com.example.lojadevideojogos.databinding.ActivityWelcomeBinding;
import com.example.lojadevideojogos.util.DataState;
import com.example.lojadevideojogos.util.SessionManager;
import com.example.lojadevideojogos.model.Game;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    private int step = 0;
    private int reqStep = 0;
    private String testGameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        testRestDb();

        binding.btnClient.setOnClickListener(v -> {
            SessionManager.getInstance().setProfile(SessionManager.Profile.CLIENTE);
            openStoreActivity();
        });

        binding.btnManager.setOnClickListener(v -> showPinDialog());
    }

    private void showPinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_pin_title));
        builder.setMessage(getString(R.string.dialog_pin_message));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.dialog_confirm), (dialog, which) -> {
            String enteredPin = input.getText().toString();

            if (enteredPin.equals(BuildConfig.ADMIN_PIN)) {
                SessionManager.getInstance().setProfile(SessionManager.Profile.GESTOR);
                openStoreActivity();
            } else {
                Toast.makeText(this, getString(R.string.error_wrong_pin), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openStoreActivity() {
        Intent intent = new Intent(this, StoreActivity.class);
        startActivity(intent);
        finish();
    }

    private void testRestDb() {
        StoreController store = new StoreController();
        RequestController requests = new RequestController();

        store.getGames().observe(this, state -> {
            if (state.status == DataState.Status.SUCCESS)
                Log.d("RESTDB", "GET jogos: " + state.data.size() + " na loja");
            else if (state.status == DataState.Status.EMPTY)
                Log.d("RESTDB", "GET jogos: loja vazia");
            else if (state.status == DataState.Status.ERROR)
                Log.e("RESTDB", "GET erro: " + state.errorMessage);
        });

        store.getActionState().observe(this, state -> {
            if (state.status == DataState.Status.ERROR) {
                Log.e("RESTDB", "Erro: " + state.errorMessage);
                return;
            }
            if (state.status != DataState.Status.SUCCESS) return;

            step++;
            switch (step) {
                case 1:
                    testGameId = state.data.getId();
                    Log.d("RESTDB", "POST ok. _id = " + testGameId);
                    state.data.setPrice(25.0);
                    store.updateGame(state.data);
                    break;
                case 2:
                    Log.d("RESTDB", "PUT ok. Preço = " + state.data.getPrice());
                    store.deleteGame(testGameId);
                    break;
                case 3:
                    Log.d("RESTDB", "DELETE ok");
                    store.loadGames();
                    testRequests(requests);
                    break;
            }
        });

        store.addGame(new Game(null, 9999, "TESTE Jogo", null, 10.0, 5, false, null));
    }

    private void testRequests(RequestController requests) {
        requests.getPendingRequests().observe(this, state -> {
            if (state.status == DataState.Status.SUCCESS)
                Log.d("RESTDB", "Pedidos PENDING: " + state.data.size());
            else if (state.status == DataState.Status.EMPTY)
                Log.d("RESTDB", "Pedidos PENDING: 0");
            else if (state.status == DataState.Status.ERROR)
                Log.e("RESTDB", "Pedidos erro: " + state.errorMessage);
        });

        requests.getActionState().observe(this, state -> {
            if (state.status == DataState.Status.ERROR) {
                Log.e("RESTDB", "Erro pedido: " + state.errorMessage);
                return;
            }
            if (state.status != DataState.Status.SUCCESS) return;

            reqStep++;
            if (reqStep == 1) {
                Log.d("RESTDB", "POST pedido ok. _id = " + state.data.getId());
                requests.rejectRequest(state.data);
            } else if (reqStep == 2) {
                Log.d("RESTDB", "PUT pedido ok. status = " + state.data.getStatus());
                requests.loadPendingRequests();
            }
        });

        requests.createRequest(9999, "TESTE Pedido", null);
    }
}