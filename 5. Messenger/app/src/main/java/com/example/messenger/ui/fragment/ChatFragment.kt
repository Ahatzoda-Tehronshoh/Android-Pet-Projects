package com.example.messenger.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.bumptech.glide.Glide
import com.example.messenger.R
import com.example.messenger.data.remote.NetworkResult
import com.example.messenger.databinding.FragmentChatBinding
import com.example.messenger.model.Contact
import com.example.messenger.model.Message
import com.example.messenger.ui.adapter.MessageAdapter
import com.example.messenger.ui.viewmodel.ChatViewModel
import com.example.messenger.util.FcmFirebaseSender
import com.example.messenger.util.currentUser
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageListAdapter: MessageAdapter

    private val args: ChatFragmentArgs by navArgs()

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSavedData()
        setUpToolbar()
        listSettings()
        setUpListeners()

        getMessageList()
    }

    private fun getMessageList() {
        viewModel.apply {
            messageListRequestStatus.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> progressBar(true)
                    is NetworkResult.Success -> progressBar(false)
                    is NetworkResult.Error -> {
                        progressBar(false)
                        Toast.makeText(
                            requireContext(),
                            "Try again please!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            getMessageList(args.phoneNumber).observe(viewLifecycleOwner) { response ->
                val listOfMessages = mutableListOf<Message>()
                for (messageDB in response)
                    listOfMessages.add(
                        Message(
                            sent = (messageDB.fromUser == currentUser.phoneNumber),
                            text = messageDB.text,
                            date = messageDB.date
                        )
                    )

                messageListAdapter.submitList(listOfMessages)
            }
        }
    }

    private fun setUpListeners() {
        viewModel.ifNetworkStatusChanged.observe(viewLifecycleOwner) { status ->
            connectingAnimationStatus(!status)
        }
        binding.sendMessage.setOnClickListener {
            val messageText = binding.messageText.text.toString()
            if (messageText != "") {
                val sendingMessage = Message(
                    true,
                    messageText,
                    Calendar.getInstance().timeInMillis
                )
                val toUser = Contact(
                    name = args.name,
                    phoneNumber = args.phoneNumber
                )

                binding.messageText.setText("")

                // send request to server for adding message
                viewModel.apply {
                    sendMessage.observe(viewLifecycleOwner) { response ->
                        when (response) {
                            is NetworkResult.Loading -> progressBar(true)
                            is NetworkResult.Success -> {
                                progressBar(false)
                            }
                            is NetworkResult.Error -> Toast.makeText(
                                requireContext(),
                                "Try again please!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    sendMessage(sendingMessage, toUser)
                }
/*
                FirebaseMessaging.getInstance().subscribeToTopic("all")

                FcmFirebaseSender(
                    "/topics/all",
                    toUser.name,
                    sendingMessage.text,
                    requireContext(),
                    requireActivity()
                ).sendNotifications()*/
            }
        }
    }

    private fun connectingAnimationStatus(status: Boolean) {
        if (status) {
            progressBar(false)
            binding.connectingAnimation.apply {
                playAnimation()
                visibility = View.VISIBLE
            }
        } else {
            binding.connectingAnimation.apply {
                pauseAnimation()
                visibility = View.GONE
            }
        }
    }

    private fun progressBar(boolean: Boolean) {
        binding.progressBar.visibility = if (boolean) View.VISIBLE else View.GONE
    }

    private fun listSettings() {
        messageListAdapter = MessageAdapter()

        binding.messagesList.apply {
            val layoutMng = LinearLayoutManager(requireContext())
            layoutMng.stackFromEnd = true
            layoutManager = layoutMng

            adapter = messageListAdapter
            messageListAdapter.submitList(listOf())

            // Scroll to bottom on new messages
            messageListAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    layoutMng.smoothScrollToPosition(
                        binding.messagesList,
                        null,
                        messageListAdapter.itemCount
                    )
                }
            })
        }
    }

    private fun setUpToolbar() {
        binding.toolBar.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            title = args.name  // Name
            subtitle = args.phoneNumber  // Phone Number
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        Glide
            .with(requireContext())
            .load(if (args.image != null) Uri.parse(args.image) else R.color.teal_700)
            .into(binding.icon)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.editText = binding.messageText.text.toString()
    }

    private fun getSavedData(){
        binding.messageText.setText(viewModel.editText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}