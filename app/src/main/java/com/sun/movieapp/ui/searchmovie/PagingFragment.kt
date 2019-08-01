package com.sun.movieapp.ui.searchmovie

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sun.movieapp.R
import com.sun.movieapp.base.BaseFragment
import com.sun.movieapp.databinding.FragmentPagingBinding
import com.sun.movieapp.utils.extensions.click
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_paging.*

class PagingFragment: BaseFragment() {
    private lateinit var mBinding: FragmentPagingBinding
    private lateinit var mViewModel: PagingViewModel
    val currentPage: BehaviorSubject<Int> = BehaviorSubject.create()

    companion object {
        fun newInstance(): PagingFragment {
            return PagingFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       mBinding = DataBindingUtil.inflate(
           inflater,
           R.layout.fragment_paging,
           container,
           false
       )
        mViewModel = ViewModelProviders.of(this, PagingViewModelFactory(currentPage)).get(PagingViewModel::class.java)
        mBinding.viewModel = mViewModel
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val input = PagingViewModel.Input(
            btnFirst.click(),
            btnLast.click(),
            btnNext.click(),
            btnPrevious.click(),
            btnOne.click(),
            btnTwo.click(),
            btnThree.click()
        )
        mViewModel.transform(input)
        mViewModel.highlightIndex.observe(this, Observer {
            listOf(btnOne, btnTwo, btnThree).forEach {
                it.setBackgroundColor(Color.WHITE)
            }
            when(it) {
                0 -> btnThree.setBackgroundResource(R.color.darkGray)
                1 -> btnOne.setBackgroundResource(R.color.darkGray)
                2 -> btnTwo.setBackgroundResource(R.color.darkGray)
                else -> btnOne.setBackgroundResource(R.color.darkGray)
            }
        })
    }

    fun updateTotalPages(totalPages: Int) {
        mViewModel.updateTotalPages(totalPages)
    }
}
