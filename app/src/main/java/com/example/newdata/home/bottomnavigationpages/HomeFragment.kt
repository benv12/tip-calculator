package com.example.newdata.home.bottomnavigationpages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import com.example.newdata.R
import com.example.newdata.common.BaseFragment
import com.example.newdata.common.Event
import com.example.newdata.databinding.FragmentHomeBinding
import com.example.newdata.home.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: MainViewModel by viewModels()

    private var guestSize: Int = 1
    private var isCurrentUserAdded: Boolean? = false

    override fun getRoot(): View? {
        return binding.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle(getString(R.string.app_name))
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadGuestsFromDatabase()
        viewModel.isUserAdded.observe(viewLifecycleOwner) { setUserAdded(it) }
        viewModel.guestCount.observe(viewLifecycleOwner) { setGuestCount(it) }
    }

    private fun setUserAdded(event: Event<Boolean?>) {
        event.contentIfNotHandled.let {
            if (!it!!) {
                viewModel.userName?.let { currentUser ->
                    viewModel.addGuest(currentUser)
                    viewModel.setUserAdded(true)
                }
            }
        }
    }

    private fun setGuestCount(count: Int) {
        var guestCount = count
        // This is just in case we remove ourselves on accident
        if (guestCount == 0) {
            viewModel.setUserAdded(false)
            guestCount = 1
        }
        guestSize = guestCount
    }

    private fun setupListeners() {
        // Set up SeekBar listener
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update tip percentage label
                binding.tipLabel.text = "$progress%"

                // Calculate tip and total amount
                calculateTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }
        })

        // Set up EditText listener
        binding.billAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Calculate tip and total amount
                calculateTipAndTotal()
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })

        // Set up action button click listeners
        binding.actionTipFifteenPercent.setOnClickListener { updateSeekBarAndCalculate(15) }
        binding.actionTipTwentyPercent.setOnClickListener { updateSeekBarAndCalculate(20) }
        binding.actionTipTwentyFivePercent.setOnClickListener { updateSeekBarAndCalculate(25) }
        binding.actionTip30Percent.setOnClickListener { updateSeekBarAndCalculate(30) }
    }

    private fun updateSeekBarAndCalculate(percentage: Int) {
        // Set the SeekBar progress to the specified percentage
        binding.seekBar.progress = percentage

        // Calculate tip and total amount
        calculateTipAndTotal()
    }

    private fun calculateTipAndTotal() {
        // Get the bill amount
        val billAmount = binding.billAmount.text.toString().toDoubleOrNull() ?: 0.0

        // Get the tip percentage from SeekBar
        val tipPercentage = binding.seekBar.progress.toDouble()

        // Calculate tip and total amount
        val tipAmount = (billAmount * tipPercentage) / 100
        val totalAmount = billAmount + tipAmount
        val guestAmount = (billAmount + tipAmount) / guestSize

        // Update TextViews with calculated values
        binding.myTip.text = String.format(getString(R.string.amount_format), tipAmount)
        binding.myTotalAmount.text = String.format(getString(R.string.amount_format), totalAmount)
        binding.guestAmount.text = String.format(getString(R.string.amount_format), guestAmount)
    }
}
