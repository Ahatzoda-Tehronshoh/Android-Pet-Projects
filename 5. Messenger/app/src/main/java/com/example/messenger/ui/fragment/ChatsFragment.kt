package com.example.messenger.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.R
import com.example.messenger.data.fetchAllContacts
import com.example.messenger.data.getPhoneNumber
import com.example.messenger.data.hideKeyboard
import com.example.messenger.data.remote.NetworkResult
import com.example.messenger.databinding.FragmentChatsBinding
import com.example.messenger.model.Contact
import com.example.messenger.ui.adapter.ContactAdapter
import com.example.messenger.ui.viewmodel.ChatsViewModel
import com.example.messenger.util.REQUEST_CODE_PERMISSIONS
import com.example.messenger.util.currentUser


class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatsViewModel by viewModels()

    private lateinit var contactsAdapter: ContactAdapter
    private lateinit var chatAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSavedData()

        fetchContacts()
        setUpContacts()
        searchingSettings()
    }

    private fun setUpChats() {
        chatAdapter = ContactAdapter()

        binding.chats.apply {
            layoutManager = LinearLayoutManager(requireContext())
            chatAdapter.onClick = {
                val action = ChatsFragmentDirections.actionChatsFragmentToChatFragment(
                    phoneNumber = it.phoneNumber, name = it.name, image = it.icon
                )
                findNavController().navigate(action)
            }
            adapter = chatAdapter
        }

        // send request to server for getting list of existing chats
        viewModel.apply {
            chatsListRequestStatus.observe(viewLifecycleOwner) { status ->
                when (status) {
                    is NetworkResult.Loading -> progressBar(true)
                    is NetworkResult.Success -> progressBar(false)
                    is NetworkResult.Error -> {
                        progressBar(false)
                        Toast.makeText(
                            requireContext(), "Try again please!", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            getListOfChats().observe(viewLifecycleOwner) { response ->
                chatAdapter.submitList(response)
                binding.noChatsTextView.visibility =
                    if (response == null || response.isEmpty()) VISIBLE
                    else GONE
            }


            ifNetworkStatusChanged.observe(viewLifecycleOwner) { status ->
                connectingAnimationStatus(!status)
            }
        }
    }

    private fun connectingAnimationStatus(status: Boolean) {
        if (status) {
            progressBar(false)
            binding.connectingAnimation.apply {
                playAnimation()
                visibility = VISIBLE
            }
        } else {
            binding.connectingAnimation.apply {
                pauseAnimation()
                visibility = GONE
            }
        }
    }

    private fun progressBar(boolean: Boolean) {
        binding.progressBar.visibility = if (boolean) VISIBLE else GONE
    }

    private fun setUpContacts() {
        contactsAdapter = ContactAdapter()

        binding.contacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            contactsAdapter.onClick = {
                val action = ChatsFragmentDirections.actionChatsFragmentToChatFragment(
                    phoneNumber = it.phoneNumber, name = it.name, image = it.icon
                )
                findNavController().navigate(action)
            }
            adapter = contactsAdapter
        }
    }

    private fun setPhoneNumber() {
        binding.phoneNumber.text = requireContext().getPhoneNumber()
    }

    private fun fetchContacts() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            )
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            currentUser.phoneNumber = requireContext().getPhoneNumber()

            viewModel.listOfContacts = requireActivity().fetchAllContacts()

            setPhoneNumber()
            setUpChats()

            viewModel.apply {
                addUserIfNotExistResponse.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is NetworkResult.Loading,
                        is NetworkResult.Success -> {
                        }
                        is NetworkResult.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Try again please!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                addUserIfNotExist()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in permissions.indices) {
            when (requestCode) {
                REQUEST_CODE_PERMISSIONS -> if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // permission i granted
                    fetchContacts()
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permissions[i]
                    )
                ) {
                    // permission i denied
                    Toast.makeText(
                        requireActivity(),
                        "Permission have not been accepted!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    // permission i denied and don't ask for it again
                }
                else -> throw RuntimeException("unhandled permissions request code: $requestCode")
            }
        }
    }


    private fun searchingSettings() {
        binding.searchEditText.setOnFocusChangeListener { _, focusOn ->
            if (focusOn) {
                // show contacts list and hide chats list
                binding.contacts.visibility = VISIBLE
                binding.chats.visibility = GONE
                binding.noChatsTextView.visibility = GONE

                contactsAdapter.submitList(viewModel.listOfContacts)
                binding.cancelSearching.visibility = VISIBLE
                binding.textInputLayout.setStartIconTintList(
                    ContextCompat.getColorStateList(
                        requireContext(), R.color.teal_700
                    )
                )
            } else {
                // show chats list and hide contacts list
                binding.contacts.visibility = GONE
                binding.chats.visibility = VISIBLE
                binding.noChatsTextView.visibility =
                    if (chatAdapter.currentList.isEmpty()) VISIBLE else GONE

                binding.cancelSearching.visibility = GONE
                binding.textInputLayout.setStartIconTintList(
                    ContextCompat.getColorStateList(
                        requireContext(), R.color.gray_500
                    )
                )
            }
        }
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.searchingContact = text.toString()
            contactsAdapter.submitList(viewModel.search(text.toString()))
        }

        binding.cancelSearching.setOnClickListener {
            binding.searchEditText.setText("")
            editFocusChange()
            viewModel.searchingContact = ""
        }
    }

    private fun editFocusChange() {
        // next for lines clear focus editText
        binding.searchEditText.isFocusable = false
        binding.searchEditText.isFocusableInTouchMode = false
        binding.searchEditText.isFocusable = true
        binding.searchEditText.isFocusableInTouchMode = true

        // hide key-board
        requireContext().hideKeyboard(requireView())
    }

    private fun getSavedData(){
        binding.searchEditText.setText(viewModel.searchingContact)
    }

    override fun onResume() {
        super.onResume()
        binding.searchEditText.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}