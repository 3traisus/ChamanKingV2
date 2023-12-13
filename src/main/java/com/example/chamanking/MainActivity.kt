package com.example.chamanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chamanking.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val db = com.google.firebase.ktx.Firebase.firestore
    private var data = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Ponemos la imagen
        Glide
            .with(binding.root.context).
            //load("https://loremflickr.com/320/240?lock=1")
            load("https://firebasestorage.googleapis.com/v0/b/chamanking.appspot.com/o/Prueba%2FBosque.png?alt=media&token=a123f351-c50d-46fc-bf94-1f26139f55ab")
            .into(binding.imagen)


        //Te dirige a otra vista
        binding.loginBtnAceptar.setOnClickListener {
            println(binding.InputUser.text.toString()+"/"+binding.Inputcontra.text.toString())
            sig(binding.InputUser.text.toString(),binding.Inputcontra.text.toString())
        }


    }

    //Mando un evento a firebase
    private fun EventoFirebase(){
        val analitycs:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("Login","Logeo Exitoso")
        analitycs.logEvent("InitScreen",bundle)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            redirectSucces()
        }
        else {

        }
    }

    private fun sig(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        redirectSucces()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    val exception = task.exception
                    exception?.let {
                        // Print or log the exception details
                        println(it.message)
                    }
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun redirectSucces(){
        val intent = Intent(this@MainActivity, Principal::class.java)
        intent.putExtra("clave_name", data)
        // Iniciar la nueva actividad
        startActivity(intent)
    }

}