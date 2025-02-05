package com.abaivel.mission2clean

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SettingsActivity : AppCompatActivity() {
    lateinit var editNom: EditText
    lateinit var editPrenom: EditText
    lateinit var editId: EditText
    lateinit var editEmail: EditText
    lateinit var boutonEditSave: Button
    lateinit var boutonRetour: ImageButton
    lateinit var fenetre: ConstraintLayout
    lateinit var btAnnulerRetour: Button
    lateinit var btRetourFenetre: Button
    var enregistre=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        editNom=findViewById(R.id.nom)
        editNom.setText(MissionActivity.nom)
        editPrenom=findViewById(R.id.prenom)
        editPrenom.setText(MissionActivity.prenom)
        editId=findViewById(R.id.id)
        editId.setText(MissionActivity.id)
        editEmail=findViewById(R.id.emailReception)
        editEmail.setText(MissionActivity.emailDestinataire)
        boutonEditSave=findViewById(R.id.btEditSave)
        boutonRetour=findViewById(R.id.btRetour)
        btAnnulerRetour=findViewById(R.id.btAnnulerRetour)
        btRetourFenetre=findViewById(R.id.btRetourFenetre)
        fenetre=findViewById(R.id.Fenetre)
        editNom.inputType = InputType.TYPE_NULL
        editPrenom.inputType = InputType.TYPE_NULL
        editId.inputType = InputType.TYPE_NULL
        editEmail.inputType = InputType.TYPE_NULL
        boutonRetour.setOnClickListener{
            if (enregistre || MissionActivity.nom==editNom.text.toString() &&
                MissionActivity.prenom==editPrenom.text.toString() &&
                MissionActivity.id==editId.text.toString() &&
                MissionActivity.emailDestinataire==editEmail.text.toString()) {
                finish()
            }else{
                fenetre.visibility=View.VISIBLE
            }
        }
        btRetourFenetre.setOnClickListener{
            finish()
        }
        btAnnulerRetour.setOnClickListener{
            fenetre.visibility=View.INVISIBLE
        }
        boutonEditSave.setOnClickListener{
            if (boutonEditSave.text.equals("Editer")){
                boutonEditSave.text="Enregistrer"
                editNom.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                editPrenom.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                editId.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                editEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                enregistre=false
            }else{
                boutonEditSave.text="Editer"
                MissionActivity.nom=editNom.text.toString()
                MissionActivity.prenom=editPrenom.text.toString()
                MissionActivity.id=editId.text.toString()
                MissionActivity.emailDestinataire=editEmail.text.toString()
                getSharedPreferences("COMPTE", MODE_PRIVATE)
                    .edit()
                    .putString("NOM", MissionActivity.nom)
                    .apply()
                getSharedPreferences("COMPTE", MODE_PRIVATE)
                    .edit()
                    .putString("PRENOM", MissionActivity.prenom)
                    .apply()
                getSharedPreferences("COMPTE", MODE_PRIVATE)
                    .edit()
                    .putString("ID", MissionActivity.id)
                    .apply()
                getSharedPreferences("COMPTE", MODE_PRIVATE)
                    .edit()
                    .putString("EMAIL_DESTINATAIRE", MissionActivity.emailDestinataire)
                    .apply()
                editNom.inputType = InputType.TYPE_NULL
                editPrenom.inputType = InputType.TYPE_NULL
                editId.inputType = InputType.TYPE_NULL
                editEmail.inputType = InputType.TYPE_NULL
                enregistre=true
            }
        }
    }
    private fun disableEditText(editText: EditText) {
        editText.isFocusable = false
        editText.isEnabled = false
        editText.isCursorVisible = false
        editText.keyListener = null
        editText.setBackgroundColor(Color.TRANSPARENT)
    }
    private fun enableEditText(editText: EditText) {
        editText.isFocusable = true
        editText.isEnabled = true
        editText.isCursorVisible = true
        editText.keyListener = null
        editText.setBackgroundColor(Color.TRANSPARENT)
    }
}