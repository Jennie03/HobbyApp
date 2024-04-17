package com.ubaya.hobbyapp.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.hobbyapp.R
import com.ubaya.hobbyapp.databinding.FragmentLoginBinding
import com.ubaya.hobbyapp.model.Users
import org.json.JSONObject

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var queue: RequestQueue? = null
    val TAG = "volleyTag"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener{
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()

            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle("Notification")
            dialog.setMessage("Login with this account?")
            dialog.setPositiveButton("YES", DialogInterface.OnClickListener{dialog, which -> login(username, password)})
            dialog.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->  dialog.dismiss()})
            dialog.create().show()

            binding.btnCreateAcc.setOnClickListener()
            {
                val action = LoginFragmentDirections.actionRegistFragment()
                Navigation.findNavController(it).navigate(action)
            }
        }
    }
    fun login(username: String, password: String){
        Log.d("login", "loginvolley")
        queue = Volley.newRequestQueue(activity)
        val url = "http://10.0.2.2/news/login.php"

        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle("Notification")

        val stringRequest = object: StringRequest(
            Request.Method.POST, url,
            {
                Log.d("cekdta", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK"){
                    val data = obj.getJSONArray("data")
                    if(data.length() > 0){
                        val userData = data.getJSONObject(0)
                        val sType = object: TypeToken<Users>(){}.type
                        val users = Gson().fromJson(userData.toString(), sType) as Users
                        Log.d("cekdta", users.toString())
                        dialog.setMessage("Successfully Logged in.\n Welcome! ${users.username}")
                        dialog.setPositiveButton("OK", DialogInterface.OnClickListener{dialog, which ->
                            val sharedPrefs =activity?.packageName
                            val shared: SharedPreferences =  requireActivity().getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                            val editor = shared.edit()
                            editor.putString("KEY_USER", userData.toString())
                            editor.apply()

                            val action = LoginFragmentDirections.actionNewsFragment()
                            Navigation.findNavController(requireView()).navigate(action)
                        })
                    }
                }
                else
                {
                    dialog.setMessage("Login Failed")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener{dialog, which -> dialog.dismiss()})
                }
                dialog.create().show()
                },
            {
                Log.e("error", it.toString())
            })
        {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}