package com.example.rotalivre

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class user : AppCompatActivity() {

    private lateinit var nomeUsuario: TextView
    private lateinit var emailUsuario: TextView
    private lateinit var deslogar: AppCompatButton

    private val db = FirebaseFirestore.getInstance()

    var usuarioID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getSupportActionBar()?.hide()

        nomeUsuario = findViewById(R.id.textnomeUsuario)
        emailUsuario = findViewById(R.id.textemailUsuario)
        deslogar = findViewById(R.id.deslogar)

        deslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@user, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        val email = FirebaseAuth.getInstance().currentUser?.email
        usuarioID = FirebaseAuth.getInstance().currentUser?.uid

        val documentReference = db.collection("Usuários").document(usuarioID!!)

        documentReference.addSnapshotListener { documentSnapshot, error ->
            if (documentSnapshot != null && error == null) {
                nomeUsuario.text = documentSnapshot.getString("name")
                emailUsuario.text = email
            }
        }
    }
}