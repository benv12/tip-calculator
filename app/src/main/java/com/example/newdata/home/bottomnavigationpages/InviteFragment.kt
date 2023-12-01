package com.example.newdata.home.bottomnavigationpages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newdata.R
import com.example.newdata.Utils.UIUtils
import com.example.newdata.common.BaseFragment
import com.example.newdata.common.EnterSingleValueDialog
import com.example.newdata.common.SnackBarMessage
import com.example.newdata.databinding.CardGuestsBinding
import com.example.newdata.databinding.FragmentInviteBinding
import com.example.newdata.home.MainViewModel
import com.example.newdata.model.GuestModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteFragment : BaseFragment() {
    private lateinit var binding: FragmentInviteBinding
    private lateinit var guestAdapter: GuestAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun getRoot(): View? {
        return binding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInviteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupAddGuestButton()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadGuestsFromDatabase()

        viewModel.guestList.observe(viewLifecycleOwner) { setupList(it) }
    }

    private fun setupList(list: List<GuestModel>) {
        guestAdapter.submitList(list)
        // If we remove the last Guest (Aka user) we set it to false so when we navigate out,
        // it adds the user
        if (list.isEmpty()) {
            viewModel.setUserAdded(false)
        }
    }

    private fun setupRecyclerView() {
        guestAdapter = GuestAdapter(viewModel)
        binding.recyclerView.layoutManager =  LinearLayoutManager(context)
        binding.recyclerView.adapter = guestAdapter
    }

    private fun setupAddGuestButton() {
        binding.actionAddGuest.setOnClickListener {
            showAddGuestDialog()
        }
    }

    private fun showAddGuestDialog() {
        val dialog: EnterSingleValueDialog = EnterSingleValueDialog.Builder()
            .setTitle(getString(R.string.enter_guest_name_modal_title))
            .setHint(getString(R.string.enter_guest_name_modal_hint))
            .setPositiveAction(getString(R.string.enter),
                object : EnterSingleValueDialog.OnConfirmListener {
                    override fun onConfirm(text: String) {
                        viewModel.addGuest(text)
                        val message = getString(R.string.message_successful_guest_add, text)
                        showSnackBar(SnackBarMessage(message))
                    }
                })
            .create()
        dialog.show(childFragmentManager, "add_guest_modal")
    }
}

class GuestAdapter(
    private val _viewModel: MainViewModel
): ListAdapter<GuestModel, GuestAdapter.GuestViewHolder>(GuestModelDiffCallback()) {

    class GuestViewHolder(private val binding: CardGuestsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(guestModel: GuestModel, viewModel: MainViewModel) {
            UIUtils.addUnderlineFlag(binding.actionRemoveGuest)
            binding.guest = guestModel
            binding.executePendingBindings()

            binding.actionRemoveGuest.setOnClickListener {
                viewModel.removeGuest(guestModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val binding = CardGuestsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val currentGuest = getItem(position)
        holder.bind(currentGuest, _viewModel)
    }

    class GuestModelDiffCallback : DiffUtil.ItemCallback<GuestModel>() {
        override fun areItemsTheSame(oldItem: GuestModel, newItem: GuestModel): Boolean {
            return oldItem.guestName == newItem.guestName
        }

        override fun areContentsTheSame(oldItem: GuestModel, newItem: GuestModel): Boolean {
            return oldItem == newItem
        }
    }
}

