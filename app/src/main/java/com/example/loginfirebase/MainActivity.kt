package com.example.loginfirebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
class MainActivity : AppCompatActivity() {
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnDangnhap: Button
    private lateinit var btnDangky: Button
    private lateinit var btnHienthi: Button
    private lateinit var txtDulieu: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        btnDangky = findViewById(R.id.btn_dangky)
        btnHienthi = findViewById(R.id.btn_hienthi)
        btnDangnhap = findViewById(R.id.btn_dangnhap)
        txtDulieu = findViewById(R.id.txt_dulieu)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        btnDangky.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty()){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Dang ky thanh cong",Toast.LENGTH_LONG).show()
                            saveUserData(email)
                            auth.signOut()
                        }
                        else
                        {
                            Toast.makeText(this,"Dang ky that bai: ${task.exception?.message}",Toast.LENGTH_LONG).show()
                        }
                    }
            }else{
                Toast.makeText(this,"Vui long nhap email va mat khau",Toast.LENGTH_LONG).show()
            }
        }
        btnDangnhap.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                            saveUserData(email)
                            task.exception?.printStackTrace()// Lưu dữ liệu sau khi đăng nhập
                        } else {
                            Toast.makeText(this, "Đăng nhập thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun saveUserData(email: String){
        val userId = auth.currentUser?.uid
        if(userId != null){
            val userMap = mapOf("email" to email)
            database.child("users").child(userId).setValue(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"Luu du lieu thanh cong",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this,"Luu du lieu that bai",Toast.LENGTH_LONG).show()
                }
        }
    }
}