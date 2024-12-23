package com.example.tubespab.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.tubespab.R
import com.example.tubespab.repository.UserRepository
import com.example.tubespab.util.AuthController
import com.example.tubespab.viewmodel.UserViewModel
import com.example.tubespab.viewmodel.UserViewModelFactory

class ProfileFragment : Fragment() {
    private val userId = AuthController.getCurrentUserUid()
    private val userJoinedDate = AuthController.getCurrentUserJoinedDate()
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvFullName: TextView = view.findViewById(R.id.tvFullName)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvPoint: TextView = view.findViewById(R.id.tvPoint)
        val tvXp: TextView = view.findViewById(R.id.tvXp)

        if (userId != null) {
            userViewModel.getUserById(userId) { user ->
                if (user != null) {
                    tvFullName.text = user.fullname
                    tvDate.text = userJoinedDate
                    tvPoint.text = user.point.toString()
                    tvXp.text = user.experience.toString()
                }
            }
        }

        val btnSetting: ImageButton = view.findViewById(R.id.btnSetting)
        btnSetting.setOnClickListener {
            AuthController.signOut(requireContext())
        }
    }
}