package com.abaivel.mission2clean

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class MissionActivity : AppCompatActivity() {
    lateinit var boutonDebutMission:Button
    lateinit var boutonFinMission:Button
    lateinit var boutonManqueProduit:Button
    lateinit var address:String
    lateinit var settingsButton: ImageButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var addresses: List<Address>
    private var debut=true
    private val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission)
        context=this
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        searchLocation()
        nom= getSharedPreferences("COMPTE", MODE_PRIVATE).getString(
            "NOM",
            null).toString()
        prenom= getSharedPreferences("COMPTE", MODE_PRIVATE).getString(
            "PRENOM",
            null).toString()
        id= getSharedPreferences("COMPTE", MODE_PRIVATE).getString(
            "ID",
            null).toString()
        emailDestinataire=getSharedPreferences("COMPTE", MODE_PRIVATE).getString(
            "EMAIL_DESTINATAIRE",
            null).toString()
        boutonDebutMission=findViewById(R.id.boutonDebutMission)
        boutonFinMission=findViewById(R.id.boutonFinMission)
        boutonManqueProduit=findViewById(R.id.boutonManqueProduit)
        settingsButton=findViewById(R.id.boutonSettings)
        boutonDebutMission.setOnClickListener{
            val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            envoyerMail(emailDestinataire,currentDate, currentTime, address, nom, prenom, id, "Debut")

        }
        boutonFinMission.setOnClickListener{
            val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            envoyerMail(emailDestinataire,currentDate, currentTime, address, nom, prenom, id, "Fin")

        }
        boutonManqueProduit.setOnClickListener{
            val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            envoyerMail(emailDestinataire,currentDate, currentTime, address, nom, prenom, id, "Manque")

        }
        settingsButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    companion object{
        lateinit var nom:String
        lateinit var prenom:String
        lateinit var id:String
        lateinit var emailDestinataire: String
        lateinit var context : Context
    }

    private fun searchLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to acess this feature")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
                        )
                    })
                    .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.dismiss() })
                    .create()
                    .show()
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        println(location.latitude)
                        println(location.longitude)
                        try {
                            val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
                            println(Geocoder.isPresent())
                            addresses = geocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            )
                            address = addresses[0].getAddressLine(0)
                            println(address)
                        }catch (e:Exception){
                            println(e)
                        }
                    }
                }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                searchLocation()
            } else {
            }
        }
    }

    fun envoyerMail(mailDestinataire :String, date : String, heure : String, lieu : String, nom : String, prenom : String, id : String, type : String){
        val sEmail = ""
        val sPassword = ""
        val properties = Properties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.host"] = "outlook.office365.com"
        properties["mail.smtp.port"] = "587"
        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(sEmail, sPassword)
            }
        })
        var objet : String
        var contenu : String
        if (type=="Debut"){
            objet = "TrackMiss : Démarrage de la mission par $prenom $nom"
            contenu = "$prenom $nom (id : $id) a commencé la mission le $date à $heure et a été localisé à cette adresse : $lieu"
        }else if (type=="Fin"){
            objet = "TrackMiss : Fin de la mission par $prenom $nom"
            contenu = "$prenom $nom (id : $id) a fini sa mission le $date à $heure et a été localisé à cette adresse : $lieu"
        }else{
            objet = "Alerte : Manque de produits par $prenom $nom"
            contenu = "$prenom $nom (id : $id) a déclaré un manque de produits le $date à $heure lors de sa mission localisée à cette adresse : $lieu"
        }
        try {
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress(sEmail))
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(emailDestinataire)
            )
            message.subject = objet
            message.setText(contenu)
            SendMail().execute(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }


}