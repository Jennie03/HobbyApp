package com.ubaya.hobbyapp.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.hobbyapp.R
import com.ubaya.hobbyapp.databinding.FragmentRegistBinding
import org.json.JSONObject

class RegistFragment : Fragment() {
    private  lateinit var binding: FragmentRegistBinding
    private var queue: RequestQueue? = null
    val TAG = "volleyTag"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener{
            val username = binding.txtusername.text.toString()
            val first_name = binding.txtFirstName.text.toString()
            val last_name = binding.txtLastName.text.toString()
            val email = binding.txtEmail.text.toString()
            val pass = binding.txtPassword.text.toString()
            val con_pass = binding.txtConfirmPass.text.toString()

            val dialog =AlertDialog.Builder(activity)
            if (pass == con_pass)
            {
                dialog.setTitle("Notification")
                dialog.setMessage("Please make sure that all your data are valid.\nDo you want to register now?")
                dialog.setPositiveButton("YES", DialogInterface.OnClickListener{dialog, which -> register(it, username,
                                            first_name, last_name, email, pass)})
                dialog.setNegativeButton("NO", DialogInterface.OnClickListener{dialog, which -> dialog.dismiss()})
                dialog.create().show()
            }
            else
            {
                dialog.setTitle("Notification")
                dialog.setMessage("Registration Failed.\nPlease make sure your password and password confirmation are the same.")
                dialog.setPositiveButton("OK", DialogInterface.OnClickListener{dialog, which -> dialog.dismiss()})
                dialog.create().show()
            }
        }
    }

    fun register(view: View, username: String, first_name: String, last_name: String, email: String, pass: String)
    {
        Log.d("regist", "registvolley")
        queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2/news/register.php"

        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle("Information")

        val stringRequest = object: StringRequest(
            Request.Method.POST, url,
            {
                Log.d("cekreg", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK")
                {
                    dialog.setMessage("Registration Successfull.\nYou can now login with your new username and password.")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener{dialog, which ->
                        val action = RegistFragmentDirections.actionLoginFragment()
                        Navigation.findNavController(view).navigate(action)
                    })
                }
                else
                {
                    dialog.setMessage("Registration Failed.")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener{dialog, which -> dialog.dismiss()})
                    dialog.create().show()
                }
                dialog.create().show()
            },
            {
                Log.e("errorcheck", it.toString())
            }
        )
        {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = username
                params["first_name"] = first_name
                params["last_name"] = last_name
                params["email"] = email
                params["password"] = pass
                return params
            }
        }

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}