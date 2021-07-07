package com.buslaev.monitoringcryptocurrency.models.profile

data class Data(
    val _internal_temp_agora_id: String,
    val background: String,
    val category: String,
    val consensus_algorithm: String,
    val contract_addresses: List<Any>,
    val id: String,
    val is_verified: Boolean,
    val name: String,
    val organizations: List<Organization>,
    val overview: String,
    val people: People,
    val relevant_resources: List<RelevantResource>,
    val sector: String,
    val sfarScore: Int,
    val slug: String,
    val symbol: String,
    val tag: String,
    val tagline: String,
    val technology: String,
    val token_details: TokenDetails,
    val token_distribution: TokenDistribution
)