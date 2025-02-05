package com.abaivel.mission2clean

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var editNom:EditText
    lateinit var editPrenom:EditText
    lateinit var editId:EditText
    lateinit var bouton: Button
    var emailParDefaut=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nom=getSharedPreferences("COMPTE", MODE_PRIVATE).getString(
            "NOM",
            null)
        if (nom!=null){
            val intent = Intent(this, MissionActivity::class.java)
            startActivity(intent)
            finish()
        }
        editNom=findViewById(R.id.nom)
        editPrenom=findViewById(R.id.prenom)
        editId=findViewById(R.id.id)
        bouton=findViewById(R.id.bouton)
        bouton.setOnClickListener{
            if (editNom.text.isEmpty() || editPrenom.text.isEmpty() || editId.text.isEmpty()){
                //message erreur
                Toast.makeText(applicationContext,"Vous devez remplir tous les champs",Toast.LENGTH_SHORT).show()
            }else{
                getSharedPreferences("COMPTE", MODE_PRIVATE)
                    .edit()
                    .putString("NOM", editNom.text.toString())
                    .apply()
                getSharedPreferences("COMPTE", MODE_PRIVATE)
                    .edit()
                    .putString("PRENOM", editPrenom.text.toString())
                    .apply()
                getSharedPreferences("COMPTE", MODE_PRIVATE)
                    .edit()
                    .putString("ID", editId.text.toString())
                    .apply()
                getSharedPreferences("COMPTE", MODE_PRIVATE)
                    .edit()
                    .putString("EMAIL_DESTINATAIRE", emailParDefaut)
                    .apply()
                //enregistrement
                //passage à la deuxième activité
                val intent = Intent(this, MissionActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}