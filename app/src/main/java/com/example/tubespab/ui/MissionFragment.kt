package com.example.tubespab.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.adapter.MissionAdapter
import com.example.tubespab.repository.MissionRepository
import com.example.tubespab.viewmodel.MissionViewModel
import com.example.tubespab.viewmodel.MissionViewModelFactory

class MissionFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var missionAdapter: MissionAdapter
    private val missionViewModel: MissionViewModel by viewModels {
        MissionViewModelFactory(MissionRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.missionBox)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        missionAdapter = MissionAdapter(emptyList())
        recyclerView.adapter = missionAdapter

        missionViewModel.getMissions().observe(viewLifecycleOwner, Observer { missions ->
            missionAdapter.updateMissions(missions)
        })
    }
}