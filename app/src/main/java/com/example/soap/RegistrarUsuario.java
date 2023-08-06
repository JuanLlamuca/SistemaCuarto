package com.example.soap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrarUsuario extends AppCompatActivity {

    EditText cedula,nombres,apellidos,correo,telefono,direccion,clave;
    Button btnGuardar;
    RequestQueue requestQueue;
    TextView errorTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        cedula=findViewById(R.id.txt_cedula);
        nombres=findViewById(R.id.txt_nombres);
        apellidos=findViewById(R.id.txt_apellidos);
        correo=findViewById(R.id.txt_correo);
        telefono=findViewById(R.id.txt_telefono);
        direccion=findViewById(R.id.txt_direccion);
        clave=findViewById(R.id.txt_clave);
        btnGuardar=findViewById(R.id.btn_guardar);

        cedula.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validarCedula();
                }
            }
        });
        telefono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validarTelefono();
                }
            }
        });
        nombres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validarNombreApellido(nombres.getText().toString(), errorTextView);
                }
            }
        });

        apellidos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validarNombreApellido(apellidos.getText().toString(), errorTextView);
                }
            }
        });
        correo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validarCorreo();
                }
            }
        });
        clave.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validarClaveSegura();
                }
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validarCedula()) {
                    Toast.makeText(getApplicationContext(), "Ingrese solo números en la cédula", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validarNombreApellido(nombres.getText().toString(), errorTextView)) {
                    return;
                }

                if (!validarNombreApellido(apellidos.getText().toString(), errorTextView)) {
                    return;
                }
                if (!validarCorreo()) {
                    Toast.makeText(getApplicationContext(), "Ingrese una dirección de correo electrónico válida (ejemplo: usuario@gmail.com)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validarTelefono()) {
                    Toast.makeText(getApplicationContext(), "Ingrese un número de teléfono válido (ejemplo: 0987654321)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validarClaveSegura()) {
                    Toast.makeText(getApplicationContext(), "La clave debe tener al menos 8 caracteres y contener al menos una letra en mayúscula, una letra en minúscula y un número", Toast.LENGTH_SHORT).show();
                    return;
                }



                ejecutarservicio("https://prestamocomputadora.000webhostapp.com/pruebasoap/insertar.php");
            }

        });

    }

    private void ejecutarservicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Convertir la respuesta JSON a un objeto JSON
                    JSONObject jsonObject = new JSONObject(response);

                    // Obtener el estado y el mensaje del objeto JSON
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    // Código para manejar la respuesta del servidor
                    if (status.equals("success")) {
                        // Si el estado es "success", entonces la inserción fue exitosa
                        cedula.setText("");
                        nombres.setText("");
                        apellidos.setText("");
                        correo.setText("");
                        telefono.setText("");
                        direccion.setText("");
                        clave.setText("");

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        // Si el estado es "error", hubo un problema en la inserción
                        Log.e("Error", message);

                        TextView errorTextView = findViewById(R.id.errorTextView);
                        errorTextView.setVisibility(View.VISIBLE);
                        errorTextView.setText(message);
                    }
                } catch (JSONException e) {
                    // Si ocurre un error al analizar la respuesta JSON
                    Log.e("Error", "Error al analizar la respuesta JSON: " + e.getMessage());

                    TextView errorTextView = findViewById(R.id.errorTextView);
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Error al procesar la respuesta del servidor.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Si ocurre un error en la solicitud HTTP
                Log.e("Error", "Error en la solicitud HTTP: " + error.toString());

                TextView errorTextView = findViewById(R.id.errorTextView);
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText("Error en la conexión con el servidor.");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Código para obtener los parámetros de la solicitud, como ya tienes en tu código original
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("per_cedula", cedula.getText().toString());
                parametros.put("per_nombres", nombres.getText().toString());
                parametros.put("per_apellidos", apellidos.getText().toString());
                parametros.put("per_correo", correo.getText().toString());
                parametros.put("per_telefono", telefono.getText().toString());
                parametros.put("per_direccion", direccion.getText().toString());
                parametros.put("per_clave", clave.getText().toString());

                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean validarCedula() {
        String cedulaText = cedula.getText().toString();
        if (!TextUtils.isDigitsOnly(cedulaText)) {
             errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Ingrese solo números en la cédula");
            return false;
        }

        if (!validarCedulaEcuatoriana(cedulaText)) {
             errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Cédula no válida. Asegúrese de que sea ecuatoriana.");
            return false;
        }

        TextView errorTextView = findViewById(R.id.errorTextView);
        errorTextView.setVisibility(View.GONE);
        return true;
    }

    public boolean validarCedulaEcuatoriana(String cedula) {

        if (cedula.length() != 10) {
            return false;
        }

        if (!TextUtils.isDigitsOnly(cedula)) {
            return false;
        }


        int provincia = Integer.parseInt(cedula.substring(0, 2));

        // Verificar que los dos primeros dígitos estén dentro del rango válido (01 a 24 para provincias ecuatorianas)
        if (provincia < 1 || provincia > 24) {
            return false;
        }


        int sum = 0;
        int coef = 2;
        int verif = Integer.parseInt(cedula.substring(9, 10));

        for (int i = 8; i >= 0; i--) {
            int num = Integer.parseInt(cedula.substring(i, i + 1));
            int temp = num * coef;
            sum += (temp >= 10) ? temp - 9 : temp;
            coef = (coef == 2) ? 1 : 2;
        }

        int mod = sum % 10;
        int calculatedVerif = (mod == 0) ? 0 : 10 - mod;

        return verif == calculatedVerif;
    }

    public boolean validarNombreApellido(String texto, TextView errorTextView) {
        String regex = "^[a-zA-Z]+( [a-zA-Z]+)?$";

        if (!texto.matches(regex)) {
            errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Ingrese solo letras en los campos de nombres y apellidos");
            return false;
        }

        errorTextView.setVisibility(View.GONE);
        return true;
    }

    public boolean validarCorreo() {
        String correoText = correo.getText().toString().trim();


        // Verificar si el correo contiene la cadena "@gmail.com"
        if (!correoText.endsWith("@gmail.com")) {
            errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Ingrese un correo eléctronico invalido (ejemplo: usuario@gmail.com)");
            return false;
        }

        errorTextView.setVisibility(View.GONE);
        return true;
    }


    public boolean validarTelefono() {
        String telefonoText = telefono.getText().toString();

        // Verificar si el teléfono contiene solo dígitos
        if (!TextUtils.isDigitsOnly(telefonoText)) {
            errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Ingrese solo números en el teléfono");
            return false;
        }

        // Verificar si el teléfono tiene el formato adecuado para un número ecuatoriano
        if (!validarTelefonoEcuatoriano(telefonoText)) {
            errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Número de teléfono no válido. Asegúrese de que sea un número ecuatoriano.");
            return false;
        }

        errorTextView.setVisibility(View.GONE);
        return true;
    }

    public boolean validarTelefonoEcuatoriano(String telefono) {
        // Verificar que el teléfono tenga 10 dígitos
        if (telefono.length() != 10) {
            return false;
        }

        // Verificar que los dos primeros dígitos del teléfono sean 09 (código de celular de Ecuador)
        if (!telefono.startsWith("09")) {
            return false;
        }

        return true;
    }

    public boolean validarClaveSegura() {
        String claveText = clave.getText().toString();

        // Verificar que la clave tenga al menos 8 caracteres
        if (claveText.length() < 8) {
            errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("La clave debe tener al menos 8 caracteres");
            return false;
        }

        // Verificar que la clave contenga al menos una letra en mayúscula, una letra en minúscula y un número
        boolean tieneMayuscula = false;
        boolean tieneMinuscula = false;
        boolean tieneNumero = false;

        for (int i = 0; i < claveText.length(); i++) {
            char caracter = claveText.charAt(i);
            if (Character.isUpperCase(caracter)) {
                tieneMayuscula = true;
            } else if (Character.isLowerCase(caracter)) {
                tieneMinuscula = true;
            } else if (Character.isDigit(caracter)) {
                tieneNumero = true;
            }
        }

        if (!tieneMayuscula || !tieneMinuscula || !tieneNumero) {
            errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("La clave debe contener al menos una letra en mayúscula, una letra en minúscula y un número");
            return false;
        }

        errorTextView.setVisibility(View.GONE);
        return true;
    }



    public void finalizar(View view){
        this.finish();
    }
}