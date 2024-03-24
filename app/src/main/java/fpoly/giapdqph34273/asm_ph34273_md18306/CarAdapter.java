package fpoly.giapdqph34273.asm_ph34273_md18306;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarAdapter extends BaseAdapter {
    private List<CarModel> carModelList;
    private ApiService apiService;
    private Context context;

    public CarAdapter(Context context, List<CarModel> carModelList) {
        this.context = context;
        this.carModelList = carModelList;
    }

    @Override
    public int getCount() {
        return carModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return carModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Khởi tạo ApiService từ Retrofit
        apiService = retrofit.create(ApiService.class);

//        TextView tvID = rowView.findViewById(R.id.id);
//        ImageView imgAvatar = rowView.findViewById(R.id.imgAvatatr);
        TextView tvName = rowView.findViewById(R.id.txtName);
        TextView tvNamSX = rowView.findViewById(R.id.txtNamSX);
        TextView tvHang = rowView.findViewById(R.id.txtHang);
        TextView tvGia = rowView.findViewById(R.id.txtGia);

        Button btnUpdate = rowView.findViewById(R.id.btnSua);
        Button btnXoa = rowView.findViewById(R.id.btnXoa);

        CarModel carModel = carModelList.get(position);

//        tvID.setText(String.valueOf(carModel.get_id()));
        tvName.setText("Tên xe: " + carModel.getTen());
        tvNamSX.setText("Năm SX: " + carModel.getNamSX());
        tvHang.setText("Hãng: " + carModel.getHang());
        tvGia.setText("Giá: " + carModel.getGia());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToUpdateCar(carModel);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa xe này không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCarOnServer(carModel);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Không làm gì cả
                    }
                });
                builder.show();
            }
        });

        return rowView;
    }

    private void showDialogToUpdateCar(CarModel carModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_update, null);
        builder.setView(dialogView);

        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);

        EditText edTen = dialogView.findViewById(R.id.edtTenXe);
        EditText edNamSX = dialogView.findViewById(R.id.edtNamSX);
        EditText edHang = dialogView.findViewById(R.id.edtHang);
        EditText edGia = dialogView.findViewById(R.id.edtGia);

        Button btnUpdate = dialogView.findViewById(R.id.btnThem);

        tvTitle.setText("Cập nhật thông tin xe");
        edTen.setText(carModel.getTen());
        edNamSX.setText(String.valueOf(carModel.getNamSX()));
        edHang.setText(carModel.getHang());
        edGia.setText(String.valueOf(carModel.getGia()));
        btnUpdate.setText("Cập nhật");

        AlertDialog dialog = builder.create();
        dialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edTen.getText().toString();
                int namSX = Integer.parseInt(edNamSX.getText().toString());
                String hang = edHang.getText().toString();
                double gia = Double.parseDouble(edGia.getText().toString());

                carModel.setTen(ten);
                carModel.setNamSX(namSX);
                carModel.setHang(hang);
                carModel.setGia(gia);

                // Gửi yêu cầu cập nhật lên server
                updateCarOnServer(carModel, dialog);
            }
        });
    }
    private void updateCarOnServer(CarModel carModel, AlertDialog dialog) {
    // Validate the user's input
    if (carModel.getTen().isEmpty() || carModel.getHang().isEmpty() || carModel.getNamSX() <= 0 || carModel.getGia() <= 0) {
        Toast.makeText(context, "Please enter valid car details", Toast.LENGTH_SHORT).show();
        return;
    }

    // Show a loading spinner
    ProgressDialog progressDialog = new ProgressDialog(context);
    progressDialog.setMessage("Updating...");
    progressDialog.show();

    Call<Void> call = apiService.updateCar(carModel.get_id(), carModel);
    call.enqueue(new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            progressDialog.dismiss();
            if (response.isSuccessful()) {
                dialog.dismiss();
                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            progressDialog.dismiss();
            Log.e("Update Error", "Failed to update car", t);
            Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

    private void deleteCarOnServer(CarModel carModel) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();
        Call<Void> call = apiService.deleteCar(carModel.get_id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa xe khỏi danh sách
                    carModelList.remove(carModel);
                    notifyDataSetChanged();
                    // Hiển thị thông báo xóa thành công
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý khi xóa không thành công
                    Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                progressDialog.dismiss();
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}