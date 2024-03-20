package com.example.apkdatateman

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.apkdatateman.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(),View.OnClickListener {
    private var auth:FirebaseAuth? = null
    private val  RC_SIGN_IN = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logout.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.showData.setOnClickListener(this)

        auth= FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String): Boolean{
        return  TextUtils.isEmpty(s)
    }

    override fun onClick(p0: View?) {
        when (p0?.getId()){
            R.id.save ->    {
                val getUserID = auth!!.currentUser!!.uid
                val database = FirebaseDatabase.getInstance()
                val getNama: String = binding.nama.getText().toString()
                val getAlamat: String = binding.alamat.getText().toString()
                val getNoHP: String = binding.noHp.getText().toString()
                val getReference: DatabaseReference
                getReference = database.reference

                if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getNoHP)) {
                    Toast.makeText(this@MainActivity, "Data tidak boleh ada yang kosong",
                        Toast.LENGTH_SHORT).show()
                } else {
                    getReference.child("Admin").child(getUserID).child("ApkDataTeman")
                        .setValue(apk_data_teman(getNama, getAlamat, getNoHP))
                        .addOnCompleteListener(this) {
                            binding.nama.setText("")
                            binding.alamat.setText("")
                            binding.noHp.setText("")
                            Toast.makeText(this@MainActivity, "Data tersimpan",
                                Toast.LENGTH_SHORT).show()
                        }

                }
            }
            R.id.logout ->  {
                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void>{
                        override fun onComplete(p0: Task<Void>) {
                            Toast.makeText(this@MainActivity,"Logout Okeee",Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            }
            R.id.show_data -> {}
        }
    }
}