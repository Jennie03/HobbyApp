package com.ubaya.hobbyapp.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import com.ubaya.hobbyapp.databinding.FragmentProfileBinding
import com.ubaya.hobbyapp.model.Users
import org.json.JSONObject

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var queue: RequestQueue? = null
    val TAG ="volleyTag"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var shared = activity?.packageName
        var sharedPref: SharedPreferences = requireActivity().getSharedPreferences(shared, Context.MODE_PRIVATE)
        var res = sharedPref.getString("KEY_USER", "")
        Log.d("cek", res.toString())

        if (res != null) {
            val sType = object : TypeToken<Users>() {}.type
            val user = Gson().fromJson(res.toString(), sType) as Users

            binding.txtCurrUser.text =
                "You're currently logged in as:\n" +
                        "name: ${user.first_name} ${user.last_name}\n" +
                        "username: ${user.username}"

            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle("Notification")

            binding.btnUpdateData.setOnClickListener {
                dialog.setMessage("Do you want to change your profile?")
                dialog.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
                    val new_first_name = binding.txtChangeFirstName.text.toString()
                    val new_last_name = binding.txtChangeLastName.text.toString()
                    val new_password = binding.txtChangePassword.text.toString()

                    updateData(user, new_first_name, new_last_name, new_password)
                })
                dialog.setNegativeButton(
                    "NO",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                dialog.create().show()
            }

            binding.btnLogout.setOnClickListener {
                dialog.setMessage("Do you want to logout?")
                dialog.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
                    val shared = activity?.packageName
                    val sharedPrefs: SharedPreferences =
                        requireActivity().getSharedPreferences(shared, Context.MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor.remove("KEY_USER")
                    editor.apply()

                    val action = ProfileFragmentDirections.actionLoginFragmentFromProfile()
                    Navigation.findNavController(it).navigate(action)
                })
                dialog.setNegativeButton(
                    "NO",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                dialog.create().show()
            }
        }
    }

    fun updateData(users: Users, first_name: String, last_name: String, password: String){
        var new_first_name: String?
        var new_last_name: String?
        var new_password: String?

        if (first_name != "")
        {
            new_first_name = first_name
        }
        else
        {
            new_first_name = users.first_name
        }

        if (last_name != "")
        {
            new_last_name = last_name
        }
        else
        {
            new_last_name = users.last_name
        }

        if (password != "")
        {
            new_password = password
        }
        else
        {
            new_password = users.password
        }

        Log.d("updatedata", "done")

        queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2/news/update_data.php"

        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle("Notification")

        val stringRequest = object: StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("cek", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK")
                {
                    dialog.setMessage("Update successful.")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss()
                    })
                }
                else
                {
                    dialog.setMessage("Update failed.")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss()})
                    dialog.create().show()
                }
                dialog.create().show()
            },
            {
                Log.e("errorcheck", it.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["id"] = users.id.toString()
                params["first_name"] = new_first_name.toString()
                params["last_name"] = new_last_name.toString()
                params["password"] = new_password.toString()
                return params
            }
        }
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}