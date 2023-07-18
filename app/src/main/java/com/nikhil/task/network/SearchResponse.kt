package com.nikhil.task.network

data class SearchResponseItem(
    val description: String,
    val details: List<Detail>,
    val email_id: String,
    val id: String,
    val mobile_number: String,
    val score: Double
)

data class Detail(
    val aws_path: String,
    val candidate_name: String,
    val created_date: String,
    val id: Int,
    val resume_id: String
)
