package com.arturmaslov.vismasound.viewmodel

import timber.log.Timber

class MainVM(
//    private val getLocalProductsUseCase: GetLocalProductsUseCase,
//    private val updateLocalWithRemoteUseCase: UpdateLocalWithRemoteUseCase
) : BaseVM() {

//    private var initialProductList: List<Product?>? = emptyList()
//    private val startProductList = MutableStateFlow<List<Product?>?>(emptyList())
//    private val finalProductList = MutableStateFlow<List<Product?>?>(emptyList())
//    private val productSortOption = MutableStateFlow(ProductSortOption.BRAND)

    init {
        // runs every time VM is created (not view created)
        Timber.i("MainVM created!")
//        setLocalProductList()
    }


}