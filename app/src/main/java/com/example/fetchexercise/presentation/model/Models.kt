package com.example.fetchexercise.presentation.model

import com.example.fetchexercise.data.model.HiringModel

data class Group(val listId: Int, val items: List<HiringModel>, var isExpanded: Boolean)