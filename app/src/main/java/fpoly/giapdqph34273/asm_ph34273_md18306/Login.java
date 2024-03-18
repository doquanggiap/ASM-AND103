package fpoly.giapdqph34273.asm_ph34273_md18306;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    TextInputEditText txtUser, txtPass;
    TextInputLayout layoutEmail, layoutPass;

    Button btndangnhap;
    TextView txtSignUp, txtResetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        anhxa();

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void anhxa() {
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        btndangnhap = findViewById(R.id.btndangnhap);
        txtSignUp = findViewById(R.id.txtSignUp);
        txtResetPass = findViewById(R.id.txtResetPass);
        layoutEmail=findViewById(R.id.layoutEmail);
        layoutPass=findViewById(R.id.layoutPass);
    }
}