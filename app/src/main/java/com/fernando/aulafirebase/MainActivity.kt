package com.fernando.aulafirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fernando.aulafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnExecutar.setOnClickListener {
            cadastrarUsuario()
        }

        binding.btnLogar.setOnClickListener{
            logarUsuario()
        }


        binding.textViewEqueceuSenha.setOnClickListener{
            esqueceuSenha()
        }

       

    }

    private fun cadastrarUsuario() {
        val email = "pf1963@fiap.com.br"
        val senha = "Fernando@2024"


        //Passar os paramentros para criação do usuario $email e $senha
        autenticacao.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { authResult ->
                val id = authResult.user?.uid
                val email = authResult.user?.email

                binding.txtResultado.text = "Sucesso ao criar conta.. $id - $email"
            }.addOnFailureListener { exception ->
                val mensagemErro = exception.message
                binding.txtResultado.text = "Error: $mensagemErro"

            }
    }

    private fun verificarUsuarioLogado() {
        val usuario = autenticacao.currentUser

        if (usuario != null) {
            startActivity(Intent(this, LogadoActivity::class.java))

        }

    }

    private fun logarUsuario(){
        try{
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            autenticacao.signInWithEmailAndPassword(email,senha)
                .addOnSuccessListener { authResult->
                    verificarUsuarioLogado()
                }
                .addOnFailureListener{ exception->
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Verificar e-mail ou senha")
                        .setPositiveButton("OK"){dialog,posicao->

                        }
                        .create().show()
                }
        }catch (e:Exception){
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Digite email e senha e-mail ou senha")
                .setPositiveButton("OK"){dialog,posicao->

                }
                .create().show()
        }
    }

    private fun esqueceuSenha(){
        try{
            val email = binding.editEmail.text.toString()

            autenticacao.sendPasswordResetEmail(email)


        }catch (e:Exception){
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Error ao enviar email de recuperação")
                .setPositiveButton("OK"){dialog,posicao->
                }
                .create().show()
        }
    }

}
