package fpoly.giapdqph34273.asm_ph34273_md18306;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    TextInputEditText txtUser, txtPass;
    TextInputLayout layoutEmail, layoutPass;
    Button btnDangKy;
    TextView txtLogin;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhxa();
        mAuth = FirebaseAuth.getInstance();


        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = txtUser.getText().toString();
                password = txtPass.getText().toString();

                layoutEmail.setError(null);
                layoutPass.setError(null);

                if (email.isEmpty() && password.isEmpty()) {
                    layoutEmail.setError("Email đang để trống");
                    layoutPass.setError("Mật khẩu đang để trống");
                    return;
                }

                if (email.isEmpty()) {
                    layoutEmail.setError("Email đang để trống");
                    return;
                }

                if (password.isEmpty()) {
                    layoutPass.setError("Mật khẩu đang để trống");
                    return;
                }

                if (password.length() < 6) {
                    layoutPass.setError("Mật khẩu phải có ít nhất 6 ký tự");
                    return;
                }

                if (!password.matches("^(?=.*[a-zA-Z]).*$")) {
                    layoutPass.setError("Mật khẩu phải có ít nhất 1 chữ cái");
                    return;
                }

                if (!password.matches("^(?=.*[0-9]).*$")) {
                    layoutPass.setError("Mật khẩu phải có ít nhất 1 số");
                    return;
                }

                btnDangKy.setEnabled(false);
                btnDangKy.setText("Đang xử lý...");
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUp.this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                }

                                btnDangKy.setEnabled(true);
                                btnDangKy.setText("Register");
                            }
                        });
            }
        });

    }
    private void anhxa() {
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        btnDangKy = findViewById(R.id.btnDangKy);
        txtLogin = findViewById(R.id.txtLogin);
        layoutEmail=findViewById(R.id.layoutEmail);
        layoutPass=findViewById(R.id.layoutPass);
    }
}