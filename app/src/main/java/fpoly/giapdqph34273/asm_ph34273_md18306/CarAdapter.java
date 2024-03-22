package fpoly.giapdqph34273.asm_ph34273_md18306;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.common.api.Api;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.txtName = convertView.findViewById(R.id.txtName);
            holder.txtNamSX = convertView.findViewById(R.id.txtNamSX);
            holder.txtHang = convertView.findViewById(R.id.txtHang);
            holder.txtGia = convertView.findViewById(R.id.txtGia);
            holder.btnSua = convertView.findViewById(R.id.btnSua);
            holder.btnXoa = convertView.findViewById(R.id.btnXoa);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.107.140:3000/") // Thay đổi URL base cho phù hợp với API của bạn
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CarModel carModel = carModelList.get(position);
        holder.txtName.setText(String.valueOf(carModel.getTen()));
        holder.txtNamSX.setText(String.valueOf(carModel.getNamSX()));
        holder.txtHang.setText(String.valueOf(carModel.getHang()));
        holder.txtGia.setText(String.valueOf(carModel.getGia()));

        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call = apiService.deleteCar(carModel.get_id());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            notifyDataSetChanged();
                            Toast.makeText(context, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                            
                        }else {
//                            Log.e(TAG, "Error deleting student: " + t.getMessage());
                            Toast.makeText(context, "Xoa that bai", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "xoa that bai failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialogSua();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtNamSX;
        TextView txtHang;
        TextView txtGia;
        Button btnSua,btnXoa;
    }

    private void dialogSua() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null);
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

                if (ten.isEmpty() || namSX.isEmpty() || hang.isEmpty() || gia.isEmpty()) {
                    if (ten.isEmpty()) {
                        layoutTenXe.setError("Tên xe đang để trống");
                    }
                    if (namSX.isEmpty()) {
                        layoutNamSX.setError("Năm sản xuất đang để trống");
                    }
                    if (hang.isEmpty()) {
                        layoutHang.setError("Hãng đang để trống");
                    }
                    if (gia.isEmpty()) {
                        layoutGia.setError("Giá đang để trống");
                    }
                    return;
                }





                dialog.dismiss();
            }
        });
    }
}
