package uz.texnopos.malbazar.ui.main.info.comments

import addLetterToWord
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import hideProgress
import org.koin.androidx.viewmodel.ext.android.viewModel
import showProgress
import textToString
import toast
import uz.texnopos.malbazar.R
import uz.texnopos.malbazar.core.Constants
import uz.texnopos.malbazar.core.ResourceState
import uz.texnopos.malbazar.core.imagehelper.setDrawableImage
import uz.texnopos.malbazar.core.preferences.isSignedIn
import uz.texnopos.malbazar.core.preferences.userId
import uz.texnopos.malbazar.data.model.AddComment
import uz.texnopos.malbazar.databinding.FragmentCommentsBinding

class CommentsFragment : Fragment(R.layout.fragment_comments) {

    private val viewModel: CommentsViewModel by viewModel()
    private val args: CommentsFragmentArgs by navArgs()
    private val adapter = CommentsAdapter()
    private lateinit var binding: FragmentCommentsBinding
    private var visible = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCommentsBinding.bind(view)
        viewModel.getComments(args.animalId)
        setUpObservable()
        binding.apply {

            ivLettersVisible.setOnClickListener {
                if (visible == 0) {
                    llLetters.isVisible = true
                    ivLettersVisible.setDrawableImage(R.drawable.ic_baseline_remove_red_eye_24)
                    visible = 1
                } else {
                    llLetters.isVisible = false
                    ivLettersVisible.setDrawableImage(R.drawable.not_show)
                    visible = 0
                }
            }

            swipe.setOnRefreshListener {
                viewModel.getComments(args.animalId)
            }

            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            tvI.setOnClickListener {
                etComment.setText(addLetterToWord(tvI.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            tvGg.setOnClickListener {
                etComment.setText(addLetterToWord(tvGg.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            tvK.setOnClickListener {
                etComment.setText(addLetterToWord(tvK.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            tvY.setOnClickListener {
                etComment.setText(addLetterToWord(tvY.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            tvX.setOnClickListener {
                etComment.setText(addLetterToWord(tvX.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            tvN.setOnClickListener {
                etComment.setText(addLetterToWord(tvN.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            tvU.setOnClickListener {
                etComment.setText(addLetterToWord(tvU.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            tvA.setOnClickListener {
                etComment.setText(addLetterToWord(tvA.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            tvG.setOnClickListener {
                etComment.setText(addLetterToWord(tvG.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            tvO.setOnClickListener {
                etComment.setText(addLetterToWord(tvO.textToString(), etComment.textToString()))
                etComment.setSelection(etComment.length())
            }
            ivSend.setOnClickListener {
                if (!isSignedIn()) {
                    toast("Сиз еле дизимнен отпедыныз")
                } else {
                    viewModel.addComments(
                        comment = AddComment(
                            args.animalId, etComment.textToString(),
                            userId!!
                        )
                    )
                }
            }
            rvComments.adapter = adapter
            rvComments.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun setUpObservable() {
        binding.apply {
            viewModel.getComment.observe(viewLifecycleOwner) {
                when (it.status) {
                    ResourceState.LOADING -> swipe.isRefreshing = true
                    ResourceState.SUCCESS -> {
                        swipe.isRefreshing = false
                        adapter.models = it.data!!
                    }
                    ResourceState.ERROR -> {
                        swipe.isRefreshing = false
                        it.message?.let { it1 -> toast(it1) }
                    }
                    ResourceState.NETWORK_ERROR -> {
                        hideProgress()
                        toast(Constants.NO_INTERNET)
                    }
                }
            }
        }
        viewModel.postComments.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    binding.etComment.setText("")
                    hideProgress()
                    viewModel.getComments(args.animalId)
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(Constants.NO_INTERNET)
                }
            }
        }
    }
}