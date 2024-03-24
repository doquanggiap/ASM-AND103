package fpoly.giapdqph34273.asm_ph34273_md18306;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    ListView lvMain;
    List<CarModel> carModelList;
    CarAdapter carAdapter;
    FloatingActionButton btnThem;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        lvMain = findViewById(R.id.listViewMain);
        btnThem = findViewById(R.id.btnThem);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        getCarList();


        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_them();
            }
        });


    }

    public void dialog_them() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_update, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputEditText edtTenXe, edtNamSX, edtHang, edtGia;
        TextInputLayout layoutTenXe, layoutNamSX, layoutHang, layoutGia;

        edtTenXe = view.findViewById(R.id.edtTenXe);
        edtNamSX = view.findViewById(R.id.edtNamSX);
        edtHang = view.findViewById(R.id.edtHang);
        edtGia = view.findViewById(R.id.edtGia);

        layoutTenXe = view.findViewById(R.id.layoutTenXe);
        layoutNamSX = view.findViewById(R.id.layoutNamSX);
        layoutHang = view.findViewById(R.id.layoutHang);
        layoutGia = view.findViewById(R.id.layoutGia);


        Button btnAdd = view.findViewById(R.id.btnThem);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTenXe.getText().toString().trim();
                String namSX = edtNamSX.getText().toString().trim();
                String hang = edtHang.getText().toString().trim();
                String gia = edtGia.getText().toString().trim();

                layoutTenXe.setError(null);
                layoutNamSX.setError(null);
                layoutHang.setError(null);
                layoutGia.setError(null);

                if (ten.isEmpty() && namSX.isEmpty() && hang.isEmpty() && gia.isEmpty()) {
                    layoutTenXe.setError("Tên xe đang để trống");
                    layoutNamSX.setError("Năm sản xuất đang để trống");
                    layoutHang.setError("Hãng đang để trống");
                    layoutGia.setError("Giá đang để trống");
                    return;
                }

                if (ten.isEmpty()) {
                    layoutTenXe.setError("Tên đang để trống");
                    return;
                }

                if (namSX.isEmpty()) {
                    layoutNamSX.setError("Năm đang để trống");
                    return;
                }

                if (hang.isEmpty()) {
                    layoutHang.setError("Hãng đang để trống");
                    return;
                }

                if (gia.isEmpty()) {
                    layoutGia.setError("Giá đang để trống");
                    return;
                }

                CarModel newCar = new CarModel(ten, Integer.parseInt(namSX), hang, Double.parseDouble(gia));

                ProgressDialog progressDialog = new ProgressDialog(Home.this);
                progressDialog.setMessage("Adding...");
                progressDialog.show();

                Call<Void> call = apiService.addCar(newCar);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            carModelList.add(newCar);
                            carAdapter.notifyDataSetChanged();
                            getCarList();
                            Toast.makeText(Home.this, "Thêm xe thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Failed to add car: " + response.message());
                            Toast.makeText(Home.this, "Thêm xe không thành công", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(Home.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();

            }
        });
    }

    private void getCarList() {
        Call<List<CarModel>> call = apiService.getCars();

        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful()) {
                    carModelList = response.body();
                    carAdapter = new CarAdapter(Home.this, carModelList);
                    carAdapter.notifyDataSetChanged();
                    lvMain.setAdapter(carAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("Loi", t.getMessage());
            }
        });
    }
}