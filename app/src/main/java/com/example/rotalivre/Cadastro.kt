package com.example.rotalivre

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore

class Cadastro : AppCompatActivity() {


    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editSenha: EditText
    private lateinit var editTelefone: EditText
    private lateinit var continuar2: Button

    val mensagens = arrayOf("Preencha todos os campos", "Cadastro Realizado com sucesso")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        editName = findViewById(R.id.edit_name)
        editEmail = findViewById(R.id.edit_email)
        editSenha = findViewById(R.id.edit_senha)
        editTelefone = findViewById(R.id.edit_telefone)
        continuar2 = findViewById(R.id.continuar2)

        continuar2.setOnClickListener {

            val nome = editName.text.toString()
            val email = editEmail.text.toString()
            val senha = editSenha.text.toString()
            val telefone = editTelefone.text.toString()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty()) {
                val snackbar = Snackbar.make(it, mensagens[0], Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(Color.WHITE)
                    .setTextColor(Color.BLACK)
                snackbar.show()
            } else {
                cadastrarUsuario(it)
            }
        }
    }

    private fun cadastrarUsuario(view: View) {
        val email = editEmail.text.toString()
        val senha = editSenha.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    salvarDadosUsuario()
                    val snackbar = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.WHITE)
                        .setTextColor(Color.BLACK)
                    snackbar.show()
                } else {
                    val exception = task.exception
                    val errorMessage = when ((exception as? FirebaseAuthException)?.errorCode) {
                        "ERROR_WEAK_PASSWORD" -> "A senha deve ter pelo menos 6 caracteres."
                        "ERROR_EMAIL_ALREADY_IN_USE" -> "Este email já está sendo usado."
                        "ERROR_INVALID_EMAIL" -> "Digite um email válido."
                        else -> "Erro desconhecido: ${exception?.message}"
                    }
                    val snackbar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(Color.RED)
                        .setTextColor(Color.WHITE)
                    snackbar.show()
                }
            }
    }

    private fun salvarDadosUsuario() {
        val name = editName.text.toString()
        val telefone = editTelefone.text.toString()

        val db = FirebaseFirestore.getInstance()

        val usuarios = hashMapOf(
            "name" to name,
            "telefone" to telefone
        )

        val usuarioID = FirebaseAuth.getInstance().currentUser?.uid

        if (usuarioID != null) {
            val documentReference = db.collection("Usuários").document(usuarioID)
            documentReference.set(usuarios)
                .addOnSuccessListener {
                    Log.d("db", "Sucesso ao salvar os dados")
                }
                .addOnFailureListener { e ->
                    Log.d("db_error", "Erro ao salvar os dados: ${e.toString()}")
                }
        }
    }
}

