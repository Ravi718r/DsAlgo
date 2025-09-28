package com.example.dsalgo.model

data class Algorithms(
    val id: String = "",
    val topic: String = "",
    val category: String = "",
    val description: String = "",
    val time_complexity: TimeComplexity = TimeComplexity(),
    val space_complexity: String = "",
    val stable: Boolean? = null,
    val in_place: Boolean? = null,
    val pseudo_code: String = "",
    val visualization: Visualization = Visualization(),
    val code: Code = Code(),
    val tags: List<String> = emptyList()
)


data class TimeComplexity(
    val best: String = "",
    val average: String = "",
    val worst: String = ""
)

data class Visualization(
    val type: String = "",
    val description: String = "",
    val example_steps: List<String> = emptyList()
)

data class Code(
    val python: String = "",
    val cpp : String = "",
    val java : String = "",
    val javascript : String = "",
    val rust : String = "",
    val kotlin :String = "",

)