package com.example.rotalivre

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class Login : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editSenha: EditText
    private lateinit var continuar: Button
    private lateinit var criarconta: TextView
    private lateinit var progressbar: ProgressBar
    private lateinit var editNome: EditText

    val mensagens = arrayOf("Preencha todos os campos", "Login Realizado com sucesso")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {

            val intent = Intent(this, Principal::class.java)
            startActivity(intent)
            finish()
        }

        editEmail = findViewById(R.id.edit_email)
        editSenha = findViewById(R.id.edit_senha)
        criarconta = findViewById(R.id.criarconta)
        continuar = findViewById(R.id.continuar)
        progressbar = findViewById(R.id.progressbar)

        continuar.setOnClickListener { view ->
            val email = editEmail.text.toString()
            val senha = editSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.WHITE)
                    .setTextColor(Color.BLACK)
                snackbar.show()
            } else {
                progressbar.visibility = View.VISIBLE

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        progressbar.visibility = View.GONE

                        if (task.isSuccessful) {
                            val snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.GREEN)
                                .setTextColor(Color.WHITE)
                            snackbar.show()

                            val intent = Intent(this, Principal::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val exception = task.exception
                            val errorMessage = when ((exception as? FirebaseAuthException)?.errorCode) {
                                "ERROR_INVALID_EMAIL" -> "Email inválido."
                                "ERROR_WRONG_PASSWORD" -> "Senha incorreta."
                                "ERROR_USER_NOT_FOUND" -> "Usuário não encontrado."
                                else -> "Erro desconhecido: ${exception?.message}"
                            }

                            val snackbar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(Color.RED)
                                .setTextColor(Color.WHITE)
                            snackbar.show()
                        }
                    }
            }
        }

        criarconta.setOnClickListener {
            val intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }
    }
}